/*
 * Copyright 2024 Cerdita App
 * 
 * Integración de Cupones con Minijuegos
 * Sistema de recompensas con dificultad media
 */

package im.vector.app.features.romantic.games.coupons

import androidx.compose.foundation.background
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
import im.vector.app.features.romantic.features.coupons.*
import kotlin.random.Random

/**
 * Sistema de Recompensas de Cupones
 */
object CouponRewardSystem {
    
    /**
     * Determina si el jugador gana un cupón basado en su puntuación
     * Dificultad media: 70-85 puntos = cupón fácil/medio
     *                   85-95 puntos = cupón difícil
     *                   95+ puntos = cupón extremo
     */
    fun determineCouponReward(score: Int, maxScore: Int): CouponReward? {
        val percentage = (score.toFloat() / maxScore.toFloat()) * 100
        
        return when {
            percentage >= 95 -> {
                // 30% chance de cupón extremo
                if (Random.nextFloat() < 0.3f) {
                    val coupons = CouponBank.getAllCoupons()
                        .filter { it.difficulty == CouponDifficulty.EXTREME }
                    CouponReward(coupons.random(), CouponRarity.LEGENDARY)
                } else {
                    val coupons = CouponBank.getAllCoupons()
                        .filter { it.difficulty == CouponDifficulty.HARD }
                    CouponReward(coupons.random(), CouponRarity.EPIC)
                }
            }
            percentage >= 85 -> {
                // 50% chance de cupón difícil
                if (Random.nextFloat() < 0.5f) {
                    val coupons = CouponBank.getAllCoupons()
                        .filter { it.difficulty == CouponDifficulty.HARD }
                    CouponReward(coupons.random(), CouponRarity.EPIC)
                } else {
                    val coupons = CouponBank.getAllCoupons()
                        .filter { it.difficulty == CouponDifficulty.MEDIUM }
                    CouponReward(coupons.random(), CouponRarity.RARE)
                }
            }
            percentage >= 70 -> {
                // 70% chance de cupón medio
                if (Random.nextFloat() < 0.7f) {
                    val coupons = CouponBank.getAllCoupons()
                        .filter { it.difficulty == CouponDifficulty.MEDIUM }
                    CouponReward(coupons.random(), CouponRarity.RARE)
                } else {
                    val coupons = CouponBank.getAllCoupons()
                        .filter { it.difficulty == CouponDifficulty.EASY }
                    CouponReward(coupons.random(), CouponRarity.COMMON)
                }
            }
            else -> {
                // Menos de 70% = sin cupón, solo XP
                null
            }
        }
    }
    
    /**
     * Calcula la XP ganada basada en la dificultad
     */
    fun calculateXpReward(score: Int, maxScore: Int, baseXp: Int): Int {
        val percentage = (score.toFloat() / maxScore.toFloat())
        return (baseXp * percentage).toInt()
    }
}

data class CouponReward(
    val template: CouponTemplate,
    val rarity: CouponRarity
)

enum class CouponRarity(val color: Color, val emoji: String) {
    COMMON(Color(0xFF808080), "⚪"),
    RARE(Color(0xFF4169E1), "🔵"),
    EPIC(Color(0xFF9370DB), "🟣"),
    LEGENDARY(Color(0xFFFFD700), "🟡")
}

/**
 * Pantalla de Recompensa después del Juego
 */
@Composable
fun GameRewardScreen(
    score: Int,
    maxScore: Int,
    baseXp: Int,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reward = remember { CouponRewardSystem.determineCouponReward(score, maxScore) }
    val xpEarned = remember { CouponRewardSystem.calculateXpReward(score, maxScore, baseXp) }
    var showCouponDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Score
        Text(
            text = "🎮 ¡Juego Terminado!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B6B)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Puntuación
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Puntuación",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "$score / $maxScore",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                
                val percentage = (score.toFloat() / maxScore.toFloat()) * 100
                Text(
                    text = "${percentage.toInt()}% de precisión",
                    fontSize = 14.sp,
                    color = when {
                        percentage >= 90 -> Color(0xFF32CD32)
                        percentage >= 70 -> Color(0xFFFFA500)
                        else -> Color(0xFFFF6347)
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // XP Ganada
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(32.dp)
            )
            
            Text(
                text = "+$xpEarned XP",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Cupón Ganado
        if (reward != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(reward.rarity.color.copy(alpha = 0.2f)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎫 ¡Cupón Ganado!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = reward.rarity.color
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "${reward.rarity.emoji} ${reward.template.rarity.name}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = reward.template.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = reward.template.description,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 3
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "🎁 ¡Ganaste este cupón para REGALAR a tu pareja!",
                        fontSize = 11.sp,
                        color = Color(0xFFFF6B6B),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = { showCouponDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = reward.rarity.color
                        )
                    ) {
                        Text("Ver Cupón")
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE0E0)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "😔 No ganaste cupón esta vez",
                        fontSize = 16.sp,
                        color = Color(0xFF666666)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Necesitas al menos 70% de precisión para ganar un cupón",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "¡Sigue intentando! 💪",
                        fontSize = 14.sp,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Botón Continuar
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B6B)
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Continuar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
    
    // Dialog para ver detalles del cupón
    if (showCouponDialog && reward != null) {
        AlertDialog(
            onDismissRequest = { showCouponDialog = false },
            title = { Text("🎫 ${reward.template.title}") },
            text = {
                Column {
                    Text(
                        text = reward.template.description,
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Dificultad: ${reward.template.difficulty.name}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        
                        Text(
                            text = "Usos: ${reward.template.maxUses}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "💡 Cómo funciona:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "1. Ganas este cupón en el minijuego",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    
                    Text(
                        text = "2. Se lo regalas a tu pareja",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    
                    Text(
                        text = "3. ¡TÚ cumples el cupón para ell@!",
                        fontSize = 11.sp,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showCouponDialog = false }
                ) {
                    Text("¡Entendido!")
                }
            }
        )
    }
}
