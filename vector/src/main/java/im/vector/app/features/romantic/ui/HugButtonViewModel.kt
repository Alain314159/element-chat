/*
 * Copyright 2024 Cerdita App
 *
 * ViewModel para el HugButton
 * Maneja la lógica de envío de abrazos románticos
 */

package im.vector.app.features.romantic.ui

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import im.vector.app.core.di.MavericksAssistedViewModelFactory
import im.vector.app.core.di.hiltMavericksViewModelFactory
import im.vector.app.core.platform.VectorViewModel
import im.vector.app.features.romantic.data.repository.RomanticRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.room.getRoom
import org.matrix.android.sdk.api.session.room.model.message.MessageTextContent
import org.matrix.android.sdk.api.session.room.model.message.MessageType

/**
 * Estados posibles del HugButton
 */
sealed class HugButtonState {
    object Idle : HugButtonState()
    object Sending : HugButtonState()
    data class Success(val message: String) : HugButtonState()
    data class Error(val errorMessage: String) : HugButtonState()
}

/**
 * Estado del HugButton
 */
data class HugButtonViewState(
    val state: HugButtonState = HugButtonState.Idle,
    val hugsSentCount: Int = 0,
    val affectionLevel: Int = 1,
    val isEnabled: Boolean = true,
    val roomId: String? = null
)

/**
 * Acciones del HugButton
 */
sealed class HugButtonAction {
    data class SendHug(val hugType: HugType) : HugButtonAction()
    object ResetState : HugButtonAction()
}

/**
 * ViewModel para el HugButton
 * 
 * Responsabilidades:
 * - Enviar mensaje de abrazo al chat
 * - Registrar el abrazo en la base de datos romántica
 * - Manejar estados de éxito/error
 */
class HugButtonViewModel @AssistedInject constructor(
    @Assisted initialState: HugButtonViewState,
    private val romanticRepository: RomanticRepository,
    private val session: Session
) : VectorViewModel<HugButtonViewState, HugButtonAction, Unit>(initialState) {

    @AssistedFactory
    interface Factory {
        fun create(initialState: HugButtonViewState): HugButtonViewModel
    }

    companion object {
        val factory: MavericksAssistedViewModelFactory<HugButtonViewModel, HugButtonViewState> =
            hiltMavericksViewModelFactory()
    }

    init {
        // Cargar estadísticas iniciales
        loadAffectionStats()
    }

    /**
     * Maneja las acciones del ViewModel
     */
    override fun handle(action: HugButtonAction) {
        when (action) {
            is HugButtonAction.SendHug -> sendHug(action.hugType)
            is HugButtonAction.ResetState -> resetState()
        }
    }

    /**
     * Envía un abrazo del tipo especificado
     */
    private fun sendHug(hugType: HugType) {
        viewModelScope.launch {
            try {
                setState { copy(state = HugButtonState.Sending, isEnabled = false) }

                val roomId = initialState.roomId
                val room = roomId?.let { session.getRoom(it) }
                
                if (room == null) {
                    setState { 
                        copy(
                            state = HugButtonState.Error("Sala no encontrada"),
                            isEnabled = true
                        ) 
                    }
                    return@launch
                }

                // Obtener mensaje del tipo de abrazo
                val message = HugMessages.getMessage(hugType)

                // Enviar mensaje al chat
                sendTextMessage(room, message)

                // Registrar en la base de datos romántica
                romanticRepository.registerHug()

                // Actualizar contador
                val stats = romanticRepository.getAffectionStats()
                val affectionLevel = romanticRepository.getAffectionLevel()

                setState { 
                    copy(
                        state = HugButtonState.Success(message),
                        hugsSentCount = stats.totalHugs,
                        affectionLevel = affectionLevel,
                        isEnabled = true
                    ) 
                }

            } catch (e: Exception) {
                setState { 
                    copy(
                        state = HugButtonState.Error(e.message ?: "Error al enviar abrazo"),
                        isEnabled = true
                    ) 
                }
            }
        }
    }

    /**
     * Carga las estadísticas de cariño
     */
    private fun loadAffectionStats() {
        viewModelScope.launch {
            try {
                val stats = romanticRepository.getAffectionStats()
                val level = romanticRepository.getAffectionLevel()
                setState { 
                    copy(
                        hugsSentCount = stats.totalHugs,
                        affectionLevel = level
                    ) 
                }
            } catch (e: Exception) {
                // Error silencioso, usa valores por defecto
            }
        }
    }

    /**
     * Reinicia el estado a Idle
     */
    private fun resetState() {
        setState { copy(state = HugButtonState.Idle) }
    }

    /**
     * Obtiene el nivel de cariño actual
     */
    suspend fun getAffectionLevel(): Int {
        return romanticRepository.getAffectionLevel()
    }
}

/**
 * Función para enviar mensaje de texto
 */
private fun sendTextMessage(
    room: org.matrix.android.sdk.api.session.room.Room,
    text: String
) {
    val textContent = MessageTextContent(
        type = MessageType.MSGTYPE_TEXT,
        body = text,
        format = null,
        formattedBody = null
    )
    room.sendService().send(textContent)
}
