/*
 * Copyright 2024 Cerdita App
 * 
 * Contador de Besos y Abrazos - Juego para parejas
 * Lleva la cuenta de besos y abrazos dados/recibidos
 */

package im.vector.app.features.romantic.games

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Contador de Besos y Abrazos
 * 
 * FUNCIONALIDAD:
 * - Contar besos dados y recibidos
 * - Contar abrazos dados y recibidos
 * - Metas diarias/semanales
 * - Notificaciones cuando la pareja envía uno
 * - rachas y logros
 */

data class AffectionStats(
    val kissesSent: Int = 0,
    val kissesReceived: Int = 0,
    val hugsSent: Int = 0,
    val hugsReceived: Int = 0,
    val lastKissDate: LocalDate? = null,
    val lastHugDate: LocalDate? = null,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)

class AffectionCounter(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "cerdita_affection"
        private const val KEY_KISSES_SENT = "kisses_sent"
        private const val KEY_KISSES_RECEIVED = "kisses_received"
        private const val KEY_HUGS_SENT = "hugs_sent"
        private const val KEY_HUGS_RECEIVED = "hugs_received"
        private const val KEY_CURRENT_STREAK = "current_streak"
        private const val KEY_LONGEST_STREAK = "longest_streak"
        private const val KEY_LAST_KISS_DATE = "last_kiss_date"
        private const val KEY_LAST_HUG_DATE = "last_hug_date"
        private const val KEY_TOTAL_LOVE_POINTS = "total_love_points"
    }
    
    fun getStats(): AffectionStats {
        return AffectionStats(
            kissesSent = prefs.getInt(KEY_KISSES_SENT, 0),
            kissesReceived = prefs.getInt(KEY_KISSES_RECEIVED, 0),
            hugsSent = prefs.getInt(KEY_HUGS_SENT, 0),
            hugsReceived = prefs.getInt(KEY_HUGS_RECEIVED, 0),
            currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0),
            longestStreak = prefs.getInt(KEY_LONGEST_STREAK, 0)
        )
    }
    
    fun sendKiss() {
        val stats = getStats()
        prefs.edit {
            putInt(KEY_KISSES_SENT, stats.kissesSent + 1)
            putInt(KEY_TOTAL_LOVE_POINTS, getTotalLovePoints() + 10)
            if (stats.lastKissDate != LocalDate.now()) {
                putInt(KEY_CURRENT_STREAK, stats.currentStreak + 1)
                if (stats.currentStreak + 1 > stats.longestStreak) {
                    putInt(KEY_LONGEST_STREAK, stats.currentStreak + 1)
                }
            }
            putString(KEY_LAST_KISS_DATE, LocalDate.now().toString())
        }
    }
    
    fun sendHug() {
        val stats = getStats()
        prefs.edit {
            putInt(KEY_HUGS_SENT, stats.hugsSent + 1)
            putInt(KEY_TOTAL_LOVE_POINTS, getTotalLovePoints() + 10)
            if (stats.lastHugDate != LocalDate.now()) {
                putInt(KEY_CURRENT_STREAK, stats.currentStreak + 1)
                if (stats.currentStreak + 1 > stats.longestStreak) {
                    putInt(KEY_LONGEST_STREAK, stats.currentStreak + 1)
                }
            }
            putString(KEY_LAST_HUG_DATE, LocalDate.now().toString())
        }
    }
    
    fun receiveKiss() {
        val stats = getStats()
        prefs.edit {
            putInt(KEY_KISSES_RECEIVED, stats.kissesReceived + 1)
            putInt(KEY_TOTAL_LOVE_POINTS, getTotalLovePoints() + 10)
        }
    }
    
    fun receiveHug() {
        val stats = getStats()
        prefs.edit {
            putInt(KEY_HUGS_RECEIVED, stats.hugsReceived + 1)
            putInt(KEY_TOTAL_LOVE_POINTS, getTotalLovePoints() + 10)
        }
    }
    
    fun getTotalLovePoints(): Int {
        return prefs.getInt(KEY_TOTAL_LOVE_POINTS, 0)
    }
    
    fun resetDaily() {
        val today = LocalDate.now()
        val lastKiss = prefs.getString(KEY_LAST_KISS_DATE, null)?.let { LocalDate.parse(it) }
        val lastHug = prefs.getString(KEY_LAST_HUG_DATE, null)?.let { LocalDate.parse(it) }
        
        if (lastKiss != today && lastHug != today) {
            prefs.edit {
                putInt(KEY_CURRENT_STREAK, 0)
            }
        }
    }
    
    fun clearAll() {
        prefs.edit {
            clear()
        }
    }
}

@Composable
fun AffectionCounterWidget(
    affectionCounter: AffectionCounter,
    onSendKiss: () -> Unit,
    onSendHug: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stats by remember { mutableStateOf(affectionCounter.getStats()) }
    var kissScale by remember { mutableStateOf(1f) }
    var hugScale by remember { mutableStateOf(1f) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header con puntos de amor
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💕 Contador de Amor",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            )

            Surface(
                color = Color(0xFFFFD700),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${affectionCounter.getTotalLovePoints()} pts",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de besar y abrazar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Botón de beso
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val kissAnimatedScale by animateFloatAsState(
                    targetValue = kissScale,
                    animationSpec = spring()
                )

                FloatingActionButton(
                    onClick = {
                        kissScale = 0.8f
                        onSendKiss()
                        affectionCounter.sendKiss()
                        scope.launch {
                            delay(100)
                            kissScale = 1f
                        }
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .scale(kissAnimatedScale),
                    containerColor = Color(0xFFFF6B9D),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Enviar beso",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Besos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "💋 ${stats.kissesSent} enviados",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "💋 ${stats.kissesReceived} recibidos",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Botón de abrazo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val hugAnimatedScale by animateFloatAsState(
                    targetValue = hugScale,
                    animationSpec = spring()
                )

                FloatingActionButton(
                    onClick = {
                        hugScale = 0.8f
                        onSendHug()
                        affectionCounter.sendHug()
                        scope.launch {
                            delay(100)
                            hugScale = 1f
                        }
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .scale(hugAnimatedScale),
                    containerColor = Color(0xFFFFB347),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Enviar abrazo",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Abrazos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "🤗 ${stats.hugsSent} enviados",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "🤗 ${stats.hugsReceived} recibidos",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Racha actual
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFFFF0F5),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "🔥 Racha Actual",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${stats.currentStreak} días",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B6B)
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "🏆 Mejor Racha",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${stats.longestStreak} días",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }
            }
        }
    }
}

/**
 * Logros del juego de amor
 */
object LoveAchievements {
    
    data class Achievement(
        val id: String,
        val title: String,
        val description: String,
        val icon: String,
        val isUnlocked: Boolean = false
    )
    
    val allAchievements = listOf(
        Achievement(
            id = "first_kiss",
            title = "Primer Beso",
            description = "Envía tu primer beso",
            icon = "💋"
        ),
        Achievement(
            id = "first_hug",
            title = "Primer Abrazo",
            description = "Envía tu primer abrazo",
            icon = "🤗"
        ),
        Achievement(
            id = "hundred_kisses",
            title = "Científico de Besos",
            description = "Envía 100 besos",
            icon = "💯"
        ),
        Achievement(
            id = "hundred_hugs",
            title = "Abrazador Experto",
            description = "Envía 100 abrazos",
            icon = "🏅"
        ),
        Achievement(
            id = "week_streak",
            title = "Semana Romántica",
            description = "Mantén una racha de 7 días",
            icon = "📅"
        ),
        Achievement(
            id = "month_streak",
            title = "Mes de Amor",
            description = "Mantén una racha de 30 días",
            icon = "🌙"
        ),
        Achievement(
            id = "thousand_points",
            title = "Mil Puntos de Amor",
            description = "Acumula 1000 puntos de amor",
            icon = "⭐"
        ),
        Achievement(
            id = "love_master",
            title = "Maestro del Amor",
            description = "Desbloquea todos los logros",
            icon = "👑"
        )
    )
    
    fun checkAchievements(stats: AffectionStats): List<Achievement> {
        return allAchievements.map { achievement ->
            val isUnlocked = when (achievement.id) {
                "first_kiss" -> stats.kissesSent >= 1
                "first_hug" -> stats.hugsSent >= 1
                "hundred_kisses" -> stats.kissesSent >= 100
                "hundred_hugs" -> stats.hugsSent >= 100
                "week_streak" -> stats.currentStreak >= 7
                "month_streak" -> stats.currentStreak >= 30
                "thousand_points" -> stats.kissesSent + stats.hugsSent >= 100
                "love_master" -> false // Se verifica dinámicamente
                else -> false
            }
            achievement.copy(isUnlocked = isUnlocked)
        }
    }
}
