/*
 * Copyright 2024 Cerdita App
 *
 * ViewModel para gestionar la configuración de efectos románticos
 */

package im.vector.app.features.romantic.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Claves para DataStore de preferencias de efectos
 */
object PreferencesKeys {
    val EFFECTS_ENABLED = booleanPreferencesKey("romantic_effects_enabled")
    val HEART_RAIN_ENABLED = booleanPreferencesKey("heart_rain_enabled")
    val KISS_EFFECT_ENABLED = booleanPreferencesKey("kiss_effect_enabled")
    val FLOWER_EFFECT_ENABLED = booleanPreferencesKey("flower_effect_enabled")
    val CONFETTI_EFFECT_ENABLED = booleanPreferencesKey("confetti_effect_enabled")
    val EFFECT_INTENSITY = intPreferencesKey("effect_intensity")
}

/**
 * DataStore extension para preferencias de efectos románticos
 */
val Context.romanticEffectsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "romantic_effects_preferences"
)

/**
 * Estado del ViewModel de efectos románticos
 */
data class RomanticEffectsState(
    val effectsEnabled: Boolean = true,
    val heartRainEnabled: Boolean = true,
    val kissEffectEnabled: Boolean = true,
    val flowerEffectEnabled: Boolean = false,
    val confettiEffectEnabled: Boolean = false,
    val effectIntensity: Int = 50, // 0-100
    val isLoading: Boolean = false
)

/**
 * Eventos de UI para el ViewModel de efectos
 */
sealed class RomanticEffectsEvent {
    object SettingsSaved : RomanticEffectsEvent()
    data class TestEffect(val effectType: RomanticEffectType) : RomanticEffectsEvent()
    data class Error(val message: String) : RomanticEffectsEvent()
}

/**
 * ViewModel para gestionar la configuración de efectos románticos
 * 
 * Características:
 * - Activar/desactivar efectos globales
 * - Configurar efectos individuales
 * - Controlar intensidad de efectos
 * - Vista previa de efectos
 * - Persistencia con DataStore
 */
class RomanticEffectsViewModel(
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(RomanticEffectsState())
    val state: StateFlow<RomanticEffectsState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<RomanticEffectsEvent>()
    val events: SharedFlow<RomanticEffectsEvent> = _events.asSharedFlow()

    init {
        loadPreferences()
    }

    /**
     * Carga las preferencias guardadas desde DataStore
     */
    private fun loadPreferences() {
        viewModelScope.launch {
            context.romanticEffectsDataStore.data
                .catch { e ->
                    _events.emit(RomanticEffectsEvent.Error("Error al cargar preferencias: ${e.message}"))
                }
                .collect { preferences ->
                    _state.value = RomanticEffectsState(
                        effectsEnabled = preferences[PreferencesKeys.EFFECTS_ENABLED] ?: true,
                        heartRainEnabled = preferences[PreferencesKeys.HEART_RAIN_ENABLED] ?: true,
                        kissEffectEnabled = preferences[PreferencesKeys.KISS_EFFECT_ENABLED] ?: true,
                        flowerEffectEnabled = preferences[PreferencesKeys.FLOWER_EFFECT_ENABLED] ?: false,
                        confettiEffectEnabled = preferences[PreferencesKeys.CONFETTI_EFFECT_ENABLED] ?: false,
                        effectIntensity = preferences[PreferencesKeys.EFFECT_INTENSITY] ?: 50
                    )
                }
        }
    }

    /**
     * Activa o desactiva todos los efectos
     */
    fun toggleGlobalEffects(enabled: Boolean) {
        updateState { it.copy(effectsEnabled = enabled) }
        savePreference(PreferencesKeys.EFFECTS_ENABLED, enabled)
    }

    /**
     * Activa o desactiva un efecto específico
     */
    fun toggleEffect(
        effectType: RomanticEffectType,
        enabled: Boolean
    ) {
        when (effectType) {
            RomanticEffectType.HEARTS -> {
                updateState { it.copy(heartRainEnabled = enabled) }
                savePreference(PreferencesKeys.HEART_RAIN_ENABLED, enabled)
            }
            RomanticEffectType.KISS -> {
                updateState { it.copy(kissEffectEnabled = enabled) }
                savePreference(PreferencesKeys.KISS_EFFECT_ENABLED, enabled)
            }
            RomanticEffectType.FLOWERS -> {
                updateState { it.copy(flowerEffectEnabled = enabled) }
                savePreference(PreferencesKeys.FLOWER_EFFECT_ENABLED, enabled)
            }
            RomanticEffectType.CONFETTI -> {
                updateState { it.copy(confettiEffectEnabled = enabled) }
                savePreference(PreferencesKeys.CONFETTI_EFFECT_ENABLED, enabled)
            }
            else -> { /* Otros efectos no tienen toggle individual */ }
        }
    }

    /**
     * Actualiza la intensidad de los efectos
     */
    fun updateEffectIntensity(intensity: Int) {
        val clampedIntensity = intensity.coerceIn(0, 100)
        updateState { it.copy(effectIntensity = clampedIntensity) }
        savePreference(PreferencesKeys.EFFECT_INTENSITY, clampedIntensity)
    }

    /**
     * Dispara una vista previa del efecto especificado
     */
    fun testEffect(effectType: RomanticEffectType) {
        viewModelScope.launch {
            _events.emit(RomanticEffectsEvent.TestEffect(effectType))
        }
    }

    /**
     * Guarda una preferencia en DataStore
     */
    private fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        viewModelScope.launch {
            try {
                context.romanticEffectsDataStore.edit { preferences ->
                    preferences[key] = value
                }
                _events.emit(RomanticEffectsEvent.SettingsSaved)
            } catch (e: Exception) {
                _events.emit(RomanticEffectsEvent.Error("Error al guardar: ${e.message}"))
            }
        }
    }

    /**
     * Actualiza el estado inmutablemente
     */
    private fun updateState(update: (RomanticEffectsState) -> RomanticEffectsState) {
        _state.value = update(_state.value)
    }

    /**
     * Restablece todas las configuraciones a valores por defecto
     */
    fun resetToDefaults() {
        viewModelScope.launch {
            context.romanticEffectsDataStore.clear()
            loadPreferences()
            _events.emit(RomanticEffectsEvent.SettingsSaved)
        }
    }
}

/**
 * Tipos de efectos románticos disponibles
 */
enum class RomanticEffectType(
    val displayName: String,
    val description: String,
    val icon: String
) {
    HEARTS("Lluvia de Corazones", "Corazones cayendo por la pantalla", "💕"),
    KISS("Besos Voladores", "Besos que cruzan la pantalla", "💋"),
    FLOWERS("Flores Bloom", "Flores que se abren", "🌸"),
    CONFETTI("Confeti Romántico", "Confeti de colores", "🎉"),
    SUNRISE("Amanecer", "Fondo degradado cálido", "🌅"),
    MOON("Luna Romántica", "Ambiente nocturno", "🌙"),
    CLOUDS("Nubes Rosadas", "Nubes suaves", "☁️"),
    HUG("Abrazo Virtual", "Animación de abrazo", "🤗"),
    SPARKLES("Brillos", "Destellos brillantes", "✨"),
    NONE("Sin Efecto", "Efectos desactivados", "❌")
}
