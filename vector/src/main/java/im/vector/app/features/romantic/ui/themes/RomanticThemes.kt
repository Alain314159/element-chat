/*
 * Copyright 2024 Cerdita App
 * 
 * Sistema de Temas Románticos
 * Implementación con gradientes, colores y efectos personalizados
 */

package im.vector.app.features.romantic.ui.themes

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Temas disponibles para la app
 */
enum class RomanticTheme(
    val name: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color,
    val backgroundGradient: Brush,
    val bubbleColor: Color,
    val textColor: Color
) {
    CERDITA_PINK(
        name = "Cerdita Rosa",
        primaryColor = Color(0xFFFFB6C1),
        secondaryColor = Color(0xFFFFC0CB),
        accentColor = Color(0xFFFF69B4),
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFF0F5), Color(0xFFFFB6C1))
        ),
        bubbleColor = Color(0xFFFFE4E1),
        textColor = Color(0xFF333333)
    ),
    
    KOALITA_GREEN(
        name = "Koalita Verde",
        primaryColor = Color(0xFF90EE90),
        secondaryColor = Color(0xFF98FB98),
        accentColor = Color(0xFF32CD32),
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFF0FFF0), Color(0xFF90EE90))
        ),
        bubbleColor = Color(0xFFE0FFE0),
        textColor = Color(0xFF333333)
    ),
    
    COUPLE_LOVE(
        name = "Amor de Pareja",
        primaryColor = Color(0xFFFF6B6B),
        secondaryColor = Color(0xFFFFB6C1),
        accentColor = Color(0xFFFF1493),
        backgroundGradient = Brush.linearGradient(
            colors = listOf(Color(0xFFFF6B6B), Color(0xFFFFB6C1))
        ),
        bubbleColor = Color(0xFFFFE4E1),
        textColor = Color(0xFFFFFFFF)
    ),
    
    ROSE_RED(
        name = "Rosa Roja",
        primaryColor = Color(0xFFFF0000),
        secondaryColor = Color(0xFFDC143C),
        accentColor = Color(0xFF8B0000),
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFF0F5), Color(0xFFFF0000))
        ),
        bubbleColor = Color(0xFFFFC0CB),
        textColor = Color(0xFFFFFFFF)
    ),
    
    LAVENDER_CALM(
        name = "Lavanda Calma",
        primaryColor = Color(0xFFE6E6FA),
        secondaryColor = Color(0xFFDDA0DD),
        accentColor = Color(0xFFBA55D3),
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFAF0FF), Color(0xFFE6E6FA))
        ),
        bubbleColor = Color(0xFFF0E6FF),
        textColor = Color(0xFF333333)
    ),
    
    SUNFLOWER_JOY(
        name = "Girasol Alegre",
        primaryColor = Color(0xFFFFD700),
        secondaryColor = Color(0xFFFFA500),
        accentColor = Color(0xFFFF8C00),
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFFFF0), Color(0xFFFFD700))
        ),
        bubbleColor = Color(0xFFFFFACD),
        textColor = Color(0xFF333333)
    ),
    
    SAKURA_DREAM(
        name = "Sakura Ensueño",
        primaryColor = Color(0xFFFFB7C5),
        secondaryColor = Color(0xFFFF69B4),
        accentColor = Color(0xFFFF1493),
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFF0F5), Color(0xFFFFB7C5))
        ),
        bubbleColor = Color(0xFFFFE4E1),
        textColor = Color(0xFF333333)
    ),
    
    MOONLIGHT_ROMANCE(
        name = "Romance Nocturno",
        primaryColor = Color(0xFF191970),
        secondaryColor = Color(0xFF4B0082),
        accentColor = Color(0xFF8A2BE2),
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF000033), Color(0xFF191970))
        ),
        bubbleColor = Color(0xFF4B0082),
        textColor = Color(0xFFFFFFFF)
    ),
    
    GOLDEN_ANNIVERSARY(
        name = "Aniversario Dorado",
        primaryColor = Color(0xFFFFD700),
        secondaryColor = Color(0xFFFFE4B5),
        accentColor = Color(0xFFDAA520),
        backgroundGradient = Brush.linearGradient(
            colors = listOf(Color(0xFFFFD700), Color(0xFFFFE4B5))
        ),
        bubbleColor = Color(0xFFFFF8DC),
        textColor = Color(0xFF333333)
    ),
    
    PURE_WHITE(
        name = "Amor Puro",
        primaryColor = Color(0xFFFFFFFF),
        secondaryColor = Color(0xFFF0F0F0),
        accentColor = Color(0xFFE0E0E0),
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F0F0))
        ),
        bubbleColor = Color(0xFFFAFAFA),
        textColor = Color(0xFF333333)
    )
}

/**
 * Manager para gestión de temas
 */
object ThemeManager {
    
    private var currentTheme: RomanticTheme = RomanticTheme.CERDITA_PINK
    
    fun getCurrentTheme(): RomanticTheme = currentTheme
    
    fun setTheme(theme: RomanticTheme) {
        currentTheme = theme
    }
    
    fun getAllThemes(): List<RomanticTheme> = RomanticTheme.values().toList()
    
    fun getThemeByName(name: String): RomanticTheme? {
        return RomanticTheme.values().find { it.name == name }
    }
}

/**
 * Efectos visuales para temas
 */
object ThemeEffects {
    
    /**
     * Efecto de partículas para el fondo
     */
    data class ParticleEffect(
        val type: ParticleType,
        val color: Color,
        val size: Int,
        val speed: Float,
        val count: Int
    )
    
    enum class ParticleType {
        HEARTS,
        STARS,
        FLOWERS,
        SPARKLES,
        BUBBLES
    }
    
    fun getDefaultEffectForTheme(theme: RomanticTheme): ParticleEffect {
        return when (theme) {
            RomanticTheme.CERDITA_PINK, RomanticTheme.COUPLE_LOVE -> 
                ParticleEffect(ParticleType.HEARTS, Color(0xFFFF6B6B), 20, 0.5f, 30)
            RomanticTheme.SAKURA_DREAM -> 
                ParticleEffect(ParticleType.FLOWERS, Color(0xFFFFB7C5), 15, 0.3f, 50)
            RomanticTheme.MOONLIGHT_ROMANCE -> 
                ParticleEffect(ParticleType.STARS, Color(0xFFFFD700), 10, 0.2f, 100)
            else -> 
                ParticleEffect(ParticleType.SPARKLES, Color(0xFFFFD700), 8, 0.4f, 40)
        }
    }
}
