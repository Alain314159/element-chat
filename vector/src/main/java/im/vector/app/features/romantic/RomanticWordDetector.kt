/*
 * Copyright 2024 Cerdita App
 * 
 * Detector de Palabras Románticas para Efectos Visuales
 * Detecta automáticamente palabras románticas y activa efectos especiales
 */

package im.vector.app.features.romantic

/**
 * Tipos de efectos románticos disponibles
 */
enum class RomanticEffectType {
    HEARTS,           // Lluvia de corazones para "te amo"
    KISS,             // Besos para "besos", "muacks"
    SUNRISE,          // Amanecer para "buenos días"
    MOON,             // Luna para "buenas noches"
    CONFETTI,         // Confeti para "feliz cumpleaños"
    CLOUDS,           // Nubes para "te extraño"
    FLOWERS,          // Flores para "gracias"
    HUG,              // Abrazo para "abrazo"
    SPARKLES,         // Brillos para cumplidos
    NONE              // Sin efecto
}

/**
 * Categoría de palabra romántica
 */
data class RomanticCategory(
    val name: String,
    val words: List<String>,
    val effectType: RomanticEffectType
)

/**
 * Detector de palabras románticas
 * 
 * Detecta automáticamente palabras románticas en los mensajes
 * y activa efectos visuales especiales
 */
object RomanticWordDetector {
    
    val categories = listOf(
        RomanticCategory(
            name = "Amor",
            words = listOf(
                "te amo", "te quiero", "te adoro", "eres mi amor",
                "mi vida", "mi cielo", "mi corazón", "te amo mucho",
                "amor mio", "mi amor", "amor", "enamorado", "enamorada"
            ),
            effectType = RomanticEffectType.HEARTS
        ),
        RomanticCategory(
            name = "Besos",
            words = listOf(
                "besos", "beso", "muacks", "muac", "kisses",
                "te mando besos", "muchos besos", "besitos"
            ),
            effectType = RomanticEffectType.KISS
        ),
        RomanticCategory(
            name = "Buenos Días",
            words = listOf(
                "buenos días", "buen día", "feliz día",
                "que tengas lindo día", "buenos días amor",
                "que amanezcas bien", "feliz mañana"
            ),
            effectType = RomanticEffectType.SUNRISE
        ),
        RomanticCategory(
            name = "Buenas Noches",
            words = listOf(
                "buenas noches", "que descanses", "dulces sueños",
                "que sueñes bonito", "buenas noches amor",
                "hasta mañana", "a dormir"
            ),
            effectType = RomanticEffectType.MOON
        ),
        RomanticCategory(
            name = "Cumpleaños",
            words = listOf(
                "feliz cumpleaños", "feliz cumple", "que cumplas muchos más",
                "feliz cumpleaños amor", "felicidades en tu día"
            ),
            effectType = RomanticEffectType.CONFETTI
        ),
        RomanticCategory(
            name = "Extrañar",
            words = listOf(
                "te extraño", "te echo de menos", "me haces falta",
                "quiero verte", "extraño", "ya quiero verte",
                "cuando te vea", "necesito verte"
            ),
            effectType = RomanticEffectType.CLOUDS
        ),
        RomanticCategory(
            name = "Gracias",
            words = listOf(
                "gracias", "gracias mi vida", "mil gracias",
                "te agradezco", "gracias amor", "gracias cielo"
            ),
            effectType = RomanticEffectType.FLOWERS
        ),
        RomanticCategory(
            name = "Abrazo",
            words = listOf(
                "abrazo", "abrazame", "te abrazo", "abrazos",
                "abrazo fuerte", "abrazo grande", "achuchón",
                "apapacho", "consuelo"
            ),
            effectType = RomanticEffectType.HUG
        ),
        RomanticCategory(
            name = "Cumplidos",
            words = listOf(
                "eres hermoso", "eres hermosa", "te ves lindo",
                "te ves linda", "qué guapo", "qué bella",
                "me encantas", "eres increíble"
            ),
            effectType = RomanticEffectType.SPARKLES
        )
    )
    
    /**
     * Detecta si un mensaje contiene palabras románticas
     * @param message El mensaje a analizar
     * @return La categoría detectada o null si no hay coincidencia
     */
    fun detectCategory(message: String): RomanticCategory? {
        val lowerMessage = message.lowercase().trim()
        
        for (category in categories) {
            for (word in category.words) {
                if (lowerMessage.contains(word)) {
                    return category
                }
            }
        }
        
        return null
    }
    
    /**
     * Verifica si un mensaje tiene efecto romántico
     */
    fun hasRomanticEffect(message: String): Boolean {
        return detectCategory(message) != null
    }
    
    /**
     * Obtiene el tipo de efecto para un mensaje
     */
    fun getEffectType(message: String): RomanticEffectType {
        return detectCategory(message)?.effectType ?: RomanticEffectType.NONE
    }
}
