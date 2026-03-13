/*
 * Copyright 2024 Cerdita App
 * 
 * Sistema de Mascotas - Cerdita y Koalita
 * Implementación completa con estados, niveles y expresiones
 */

package im.vector.app.features.romantic.ui.mascots

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Estados de ánimo de las mascotas
 */
enum class MascotMood(val emoji: String, val color: Color) {
    HAPPY("😊", Color(0xFFFFB6C1)),
    LOVED("😍", Color(0xFFFF69B4)),
    SHY("😊", Color(0xFFFFC0CB)),
    EXCITED("🤩", Color(0xFFFF1493)),
    SAD("😢", Color(0xFF87CEEB)),
    ANGRY("😠", Color(0xFFFF6347)),
    SURPRISED("😲", Color(0xFFFFD700)),
    SLEEPY("😴", Color(0xFF9370DB)),
    PLAYFUL("🥰", Color(0xFFFF6B6B)),
    RELAXED("😌", Color(0xFF98FB98))
}

/**
 * Tipos de mascotas
 */
enum class MascotType(val name: String, val defaultOutfit: String) {
    CERDITA("Cerdita", "bow_pink"),
    KOALITA("Koalita", "eucalyptus_green")
}

/**
 * Modelo de Mascota
 */
data class Mascot(
    val id: String,
    val type: MascotType,
    val name: String,
    val level: Int = 1,
    val experience: Int = 0,
    val mood: MascotMood = MascotMood.HAPPY,
    val outfit: String? = null,
    val accessories: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getNextLevelXp(): Int = level * 100
    fun getProgressToNextLevel(): Float = (experience % 100) / 100f
}

/**
 * Componente de Mascota Animada
 */
@Composable
fun AnimatedMascot(
    mascot: Mascot,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showLevel: Boolean = true
) {
    Card(
        modifier = modifier
            .size(150.dp)
            .clip(RoundedCornerShape(20.dp)),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = mascot.mood.color.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header con nombre y nivel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mascot.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                
                if (showLevel) {
                    Surface(
                        color = Color(0xFFFFD700),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Lv.${mascot.level}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            // Imagen de la mascota (placeholder - reemplazar con sprite real)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (mascot.type) {
                        MascotType.CERDITA -> "🐷"
                        MascotType.KOALITA -> "🐨"
                    },
                    fontSize = 48.sp
                )
            }
            
            // Estado de ánimo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = mascot.mood.emoji,
                    fontSize = 16.sp
                )
                
                Text(
                    text = mascot.mood.name,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            
            // Barra de experiencia
            if (showLevel) {
                Column {
                    LinearProgressIndicator(
                        progress = mascot.getProgressToNextLevel(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = Color(0xFFFF6B6B),
                        trackColor = Color(0xFFFFE0E0)
                    )
                    
                    Text(
                        text = "${mascot.experience % 100}/${mascot.getNextLevelXp()} XP",
                        fontSize = 8.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * Componente de Pareja de Mascotas
 */
@Composable
fun MascotCouple(
    cerdita: Mascot,
    koalita: Mascot,
    onCerditaClick: () -> Unit,
    onKoalitaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedMascot(
            mascot = cerdita,
            onClick = onCerditaClick,
            modifier = Modifier.weight(1f)
        )
        
        // Corazón entre las mascotas
        Text(
            text = "💕",
            fontSize = 32.sp
        )
        
        AnimatedMascot(
            mascot = koalita,
            onClick = onKoalitaClick,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Sistema de interacciones con mascotas
 */
object MascotInteractions {
    
    fun feed(mascot: Mascot): Mascot {
        // Implementar lógica de alimentar
        return mascot.copy(
            mood = MascotMood.HAPPY,
            experience = mascot.experience + 10
        )
    }
    
    fun play(mascot: Mascot): Mascot {
        // Implementar lógica de jugar
        return mascot.copy(
            mood = MascotMood.PLAYFUL,
            experience = mascot.experience + 20
        )
    }
    
    fun hug(mascot: Mascot): Mascot {
        // Implementar lógica de abrazar
        return mascot.copy(
            mood = MascotMood.LOVED,
            experience = mascot.experience + 15
        )
    }
    
    fun sleep(mascot: Mascot): Mascot {
        // Implementar lógica de dormir
        return mascot.copy(
            mood = MascotMood.SLEEPY
        )
    }
}
