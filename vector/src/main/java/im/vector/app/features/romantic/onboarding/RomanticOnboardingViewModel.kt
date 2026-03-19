/*
 * Copyright 2024 Cerdita App
 *
 * RomanticOnboardingViewModel - ViewModel para gestionar el estado del onboarding
 */

package im.vector.app.features.romantic.onboarding

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "romantic_onboarding_prefs")

/**
 * Claves para DataStore
 */
object OnboardingPreferencesKeys {
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    val ONBOARDING_VERSION = booleanPreferencesKey("onboarding_version_2")
}

/**
 * Estado del Onboarding
 */
data class OnboardingState(
    val currentPage: Int = 0,
    val isOnboardingCompleted: Boolean = false,
    val isLoading: Boolean = true
)

/**
 * Eventos del Onboarding
 */
sealed class OnboardingEvent {
    object OnboardingComplete : OnboardingEvent()
    data class PageChanged(val page: Int) : OnboardingEvent()
}

/**
 * ViewModel para el Onboarding Romántico
 *
 * Gestiona:
 * - Estado de las páginas del onboarding
 * - Persistencia del estado de completado con DataStore
 * - Navegación entre páginas
 */
class RomanticOnboardingViewModel(
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<OnboardingEvent>()
    val events: SharedFlow<OnboardingEvent> = _events.asSharedFlow()

    // Número total de páginas
    val totalPages = 3

    init {
        checkOnboardingStatus()
    }

    /**
     * Verifica si el onboarding ya fue completado
     */
    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            context.dataStore.data
                .map { preferences ->
                    preferences[OnboardingPreferencesKeys.ONBOARDING_COMPLETED] ?: false
                }
                .first()
                .let { isCompleted ->
                    _state.value = OnboardingState(
                        currentPage = 0,
                        isOnboardingCompleted = isCompleted,
                        isLoading = false
                    )
                }
        }
    }

    /**
     * Navega a la siguiente página
     */
    fun nextPage() {
        val currentPage = _state.value.currentPage
        if (currentPage < totalPages - 1) {
            _state.value = _state.value.copy(currentPage = currentPage + 1)
            viewModelScope.launch {
                _events.emit(OnboardingEvent.PageChanged(currentPage + 1))
            }
        } else {
            completeOnboarding()
        }
    }

    /**
     * Navega a la página anterior
     */
    fun previousPage() {
        val currentPage = _state.value.currentPage
        if (currentPage > 0) {
            _state.value = _state.value.copy(currentPage = currentPage - 1)
            viewModelScope.launch {
                _events.emit(OnboardingEvent.PageChanged(currentPage - 1))
            }
        }
    }

    /**
     * Salta el onboarding y va directamente a la última página
     */
    fun skipOnboarding() {
        _state.value = _state.value.copy(currentPage = totalPages - 1)
        viewModelScope.launch {
            _events.emit(OnboardingEvent.PageChanged(totalPages - 1))
        }
    }

    /**
     * Marca el onboarding como completado y guarda en DataStore
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[OnboardingPreferencesKeys.ONBOARDING_COMPLETED] = true
                preferences[OnboardingPreferencesKeys.ONBOARDING_VERSION] = true
            }
            _state.value = _state.value.copy(isOnboardingCompleted = true)
            _events.emit(OnboardingEvent.OnboardingComplete)
        }
    }

    /**
     * Actualiza la página actual
     */
    fun setCurrentPage(page: Int) {
        if (page in 0 until totalPages) {
            _state.value = _state.value.copy(currentPage = page)
            viewModelScope.launch {
                _events.emit(OnboardingEvent.PageChanged(page))
            }
        }
    }

    /**
     * Reinicia el onboarding (para testing o reset)
     */
    fun resetOnboarding() {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
            _state.value = OnboardingState(
                currentPage = 0,
                isOnboardingCompleted = false,
                isLoading = false
            )
        }
    }

    /**
     * Factory para crear el ViewModel con Context
     */
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RomanticOnboardingViewModel::class.java)) {
                return RomanticOnboardingViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

/**
 * Funciones de utilidad para verificar el estado del onboarding
 */
suspend fun isOnboardingCompleted(context: Context): Boolean {
    return context.dataStore.data
        .map { preferences ->
            preferences[OnboardingPreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }
        .first()
}

/**
 * Flow para observar el estado del onboarding
 */
fun onboardingStatusFlow(context: Context): Flow<Boolean> {
    return context.dataStore.data
        .map { preferences ->
            preferences[OnboardingPreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }
}
