/*
 * Copyright 2024 Cerdita App
 *
 * Integración del HugButton en el TimelineFragment
 * Maneja la configuración y eventos del botón de abrazos románticos
 */

package im.vector.app.features.home.room.detail

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import im.vector.app.features.romantic.ui.HugButtonView
import im.vector.app.features.romantic.ui.HugButtonViewModel
import im.vector.app.features.romantic.ui.HugButtonViewState
import im.vector.app.features.romantic.ui.HugButtonState
import im.vector.app.features.romantic.ui.HugButtonAction
import im.vector.app.features.romantic.ui.HugType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Clase de utilidad para integrar el HugButton en el TimelineFragment
 * 
 * Esta clase maneja:
 * - Inicialización del botón
 * - Configuración de listeners
 * - Manejo de estados (loading, error, éxito)
 * - Integración con el ViewModel romántico
 */
class HugButtonIntegration(
    private val fragment: Fragment,
    private val hugButtonView: HugButtonView,
    private val hugButtonViewModel: HugButtonViewModel,
    private val roomId: String
) {

    /**
     * Inicializa el HugButton
     * Debe llamarse después de que el view esté disponible
     */
    fun initialize() {
        setupVisibility()
        setupClickListener()
        observeViewModelState()
        
        Timber.d("HugButton initialized for room: $roomId")
    }

    /**
     * Configura la visibilidad del botón
     * Solo visible en rooms de tipo DM (pareja)
     */
    private fun setupVisibility() {
        // Por defecto visible, podría hacerse condicional según el tipo de room
        hugButtonView.visibility = View.VISIBLE
    }

    /**
     * Configura el listener para clicks en el botón
     */
    private fun setupClickListener() {
        hugButtonView.setOnHugClickListener { hugType ->
            onHugClicked(hugType)
        }
    }

    /**
     * Maneja el evento de click en el botón de abrazo
     */
    private fun onHugClicked(hugType: HugType) {
        Timber.d("Hug button clicked: $hugType")
        
        // Enviar abrazo a través del ViewModel
        hugButtonViewModel.handle(HugButtonAction.SendHug(hugType))
        
        // Feedback háptico (opcional)
        hugButtonView.performHapticFeedback()
    }

    /**
     * Observa los estados del ViewModel para mostrar feedback UI
     */
    private fun observeViewModelState() {
        hugButtonViewModel
            .select(HugButtonViewState::state)
            .onEach { state ->
                when (state) {
                    is HugButtonState.Idle -> {
                        // Estado normal
                        hugButtonView.isEnabled = true
                    }
                    is HugButtonState.Sending -> {
                        // Deshabilitar mientras se envía
                        hugButtonView.isEnabled = false
                    }
                    is HugButtonState.Success -> {
                        // Re-habilitar después de enviar
                        hugButtonView.isEnabled = true
                        Timber.d("Hug sent successfully: ${state.message}")
                        
                        // Resetear estado después de un delay
                        fragment.viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                            kotlinx.coroutines.delay(2000)
                            hugButtonViewModel.handle(HugButtonAction.ResetState)
                        }
                    }
                    is HugButtonState.Error -> {
                        // Re-habilitar y mostrar error
                        hugButtonView.isEnabled = true
                        Timber.e("Error sending hug: ${state.errorMessage}")
                        
                        // Resetear estado después de un delay
                        fragment.viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                            kotlinx.coroutines.delay(3000)
                            hugButtonViewModel.handle(HugButtonAction.ResetState)
                        }
                    }
                }
            }
            .launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Muestra el HugButton
     */
    fun show() {
        hugButtonView.visibility = View.VISIBLE
    }

    /**
     * Oculta el HugButton
     */
    fun hide() {
        hugButtonView.visibility = View.GONE
    }

    /**
     * Actualiza el roomId (útil cuando se cambia de room)
     */
    fun updateRoomId(newRoomId: String) {
        // El roomId es inmutable en esta implementación
        Timber.d("HugButton room update requested: $newRoomId")
    }
}

/**
 * Función de extensión para crear fácilmente la integración
 */
fun Fragment.createHugButtonIntegration(
    hugButtonView: HugButtonView,
    hugButtonViewModel: HugButtonViewModel,
    roomId: String
): HugButtonIntegration {
    return HugButtonIntegration(
        fragment = this,
        hugButtonView = hugButtonView,
        hugButtonViewModel = hugButtonViewModel,
        roomId = roomId
    )
}
