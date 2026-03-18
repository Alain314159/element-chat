/*
 * Copyright 2024 Cerdita App
 *
 * ViewModel para gestionar efectos visuales románticos en el chat
 */

package im.vector.app.features.romantic.ui

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.withState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import im.vector.app.core.di.MavericksAssistedViewModelFactory
import im.vector.app.core.di.hiltMavericksViewModelFactory
import im.vector.app.core.platform.VectorViewModel
import im.vector.app.features.home.room.detail.RoomDetailViewState
import im.vector.app.features.romantic.RomanticWordDetector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent

/**
 * Estado del ViewModel de efectos románticos
 */
data class RomanticEffectState(
    val activeEffect: RomanticEffectType? = null,
    val isEffectActive: Boolean = false,
    val effectMessage: String? = null,
    val detectedCategory: String? = null
)

/**
 * Acciones para el ViewModel de efectos románticos
 */
sealed class RomanticEffectAction {
    data class CheckMessageForRomanticWords(val event: TimelineEvent) : RomanticEffectAction()
    data class TriggerEffect(val effectType: RomanticEffectType, val category: String) : RomanticEffectAction()
    object StopEffect : RomanticEffectAction()
    object ClearEffect : RomanticEffectAction()
}

/**
 * Eventos de vista para efectos románticos
 */
sealed class RomanticEffectViewEvents {
    data class StartRomanticEffect(val effectType: RomanticEffectType) : RomanticEffectViewEvents()
    object StopRomanticEffect : RomanticEffectViewEvents()
}

/**
 * ViewModel para gestionar efectos románticos en el chat
 * 
 * Detecta palabras románticas en los mensajes y dispara efectos visuales
 * correspondientes basados en la categoría detectada
 */
class RomanticEffectViewModel @AssistedInject constructor(
    @Assisted initialState: RomanticEffectState,
    private val romanticWordDetector: RomanticWordDetector
) : VectorViewModel<RomanticEffectState, RomanticEffectAction, RomanticEffectViewEvents>(initialState) {

    private val _romanticEffectFlow = MutableSharedFlow<RomanticEffectType>(extraBufferCapacity = 1)
    val romanticEffectFlow: SharedFlow<RomanticEffectType> = _romanticEffectFlow

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<RomanticEffectViewModel, RomanticEffectState> {
        override fun create(initialState: RomanticEffectState): RomanticEffectViewModel
    }

    companion object : MavericksViewModelFactory<RomanticEffectViewModel, RomanticEffectState> by hiltMavericksViewModelFactory()

    override fun handle(action: RomanticEffectAction) {
        when (action) {
            is RomanticEffectAction.CheckMessageForRomanticWords -> {
                checkMessageForRomanticWords(action.event)
            }
            is RomanticEffectAction.TriggerEffect -> {
                triggerEffect(action.effectType, action.category)
            }
            is RomanticEffectAction.StopEffect -> {
                stopEffect()
            }
            is RomanticEffectAction.ClearEffect -> {
                clearEffect()
            }
        }
    }

    /**
     * Verifica si un mensaje contiene palabras románticas
     * y dispara el efecto correspondiente
     */
    private fun checkMessageForRomanticWords(event: TimelineEvent) {
        val messageBody = event.root.getClearContent()?.get("body")?.asString ?: return
        
        // Solo procesar mensajes enviados (no locales/en cola)
        if (!event.root.sendState.isSent()) return
        
        val category = romanticWordDetector.detectCategory(messageBody)
        
        if (category != null) {
            // Evitar efectos duplicados si ya hay uno activo
            withState { state ->
                if (!state.isEffectActive) {
                    triggerEffect(category.effectType, category.name)
                }
            }
        }
    }

    /**
     * Dispara un efecto romántico específico
     */
    private fun triggerEffect(effectType: RomanticEffectType, category: String) {
        setState {
            copy(
                activeEffect = effectType,
                isEffectActive = true,
                detectedCategory = category
            )
        }
        
        // Notificar a la vista para iniciar el efecto
        _viewEvents.post(RomanticEffectViewEvents.StartRomanticEffect(effectType))
        
        // También emitir en el flow para otros observadores
        viewModelScope.launch {
            _romanticEffectFlow.emit(effectType)
        }
        
        // Programar la detención automática del efecto
        viewModelScope.launch {
            kotlinx.coroutines.delay(getEffectDuration(effectType))
            stopEffect()
        }
    }

    /**
     * Detiene el efecto actual
     */
    private fun stopEffect() {
        setState {
            copy(
                isEffectActive = false
            )
        }
        _viewEvents.post(RomanticEffectViewEvents.StopRomanticEffect)
    }

    /**
     * Limpia completamente el estado del efecto
     */
    private fun clearEffect() {
        setState {
            copy(
                activeEffect = null,
                isEffectActive = false,
                effectMessage = null,
                detectedCategory = null
            )
        }
    }

    /**
     * Obtiene la duración del efecto en milisegundos según el tipo
     */
    private fun getEffectDuration(effectType: RomanticEffectType): Long {
        return when (effectType) {
            RomanticEffectType.HEARTS -> 4000L
            RomanticEffectType.KISS -> 3000L
            RomanticEffectType.SUNRISE -> 3500L
            RomanticEffectType.MOON -> 3500L
            RomanticEffectType.CONFETTI -> 5000L
            RomanticEffectType.CLOUDS -> 4000L
            RomanticEffectType.FLOWERS -> 4000L
            RomanticEffectType.HUG -> 3000L
            RomanticEffectType.SPARKLES -> 3000L
            RomanticEffectType.NONE -> 0L
        }
    }

    /**
     * Verifica si un mensaje debe disparar un efecto
     * Método síncrono para uso rápido
     */
    fun shouldTriggerEffect(message: String): Boolean {
        return romanticWordDetector.hasRomanticEffect(message)
    }

    /**
     * Obtiene el tipo de efecto para un mensaje
     */
    fun getEffectTypeForMessage(message: String): RomanticEffectType {
        return romanticWordDetector.getEffectType(message)
    }
}
