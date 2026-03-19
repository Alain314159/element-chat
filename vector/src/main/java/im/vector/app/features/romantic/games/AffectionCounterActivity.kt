/*
 * Copyright 2024 Cerdita App
 *
 * Activity para el Contador de Afecto (Besos y Abrazos)
 */

package im.vector.app.features.romantic.games

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * AffectionCounterActivity - Pantalla para el contador de besos y abrazos
 *
 * Permite:
 * - Enviar besos y abrazos a la pareja
 * - Ver estadísticas de afecto
 * - Ver racha actual y mejor racha
 * - Ver logros desbloqueados
 */
class AffectionCounterActivity : ComponentActivity() {

    companion object {
        const val EXTRA_FOCUS_MODE = "focus_mode"
        const val MODE_KISS = "kiss"
        const val MODE_HUG = "hug"
        const val MODE_DEFAULT = "default"
    }

    private lateinit var affectionCounter: AffectionCounter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        affectionCounter = AffectionCounter(this)

        val focusMode = intent.getStringExtra(EXTRA_FOCUS_MODE) ?: MODE_DEFAULT

        setContent {
            AffectionCounterScreen(
                affectionCounter = affectionCounter,
                focusMode = focusMode,
                onBackClick = { finish() }
            )
        }
    }
}

/**
 * Composable screen para el Contador de Afecto
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffectionCounterScreen(
    affectionCounter: AffectionCounter,
    focusMode: String?,
    onBackClick: () -> Unit
) {
    val stats by remember { mutableStateOf(affectionCounter.getStats()) }
    var kissCount by remember { mutableIntStateOf(stats.kissesSent) }
    var hugCount by remember { mutableIntStateOf(stats.hugsSent) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (focusMode) {
                            AffectionCounterActivity.MODE_KISS -> "💋 Contador de Besos"
                            AffectionCounterActivity.MODE_HUG -> "🤗 Contador de Abrazos"
                            else -> "💕 Contador de Amor"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5),
                    titleContentColor = Color(0xFFFF6B6B),
                    navigationIconContentColor = Color(0xFFFF6B6B)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AffectionCounterWidget(
                affectionCounter = affectionCounter,
                onSendKiss = { kissCount++ },
                onSendHug = { hugCount++ },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Pantalla de logros del contador de afecto
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveAchievementsScreen(
    affectionCounter: AffectionCounter,
    onBackClick: () -> Unit
) {
    val stats by remember { mutableStateOf(affectionCounter.getStats()) }
    val achievements by remember {
        mutableStateOf(LoveAchievements.checkAchievements(stats))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🏆 Logros de Amor",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5),
                    titleContentColor = Color(0xFFFF6B6B),
                    navigationIconContentColor = Color(0xFFFF6B6B)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements.size) { index ->
                val achievement = achievements[index]
                AchievementCard(achievement = achievement)
            }
        }
    }
}

/**
 * Tarjeta individual para cada logro
 */
@Composable
fun AchievementCard(
    achievement: LoveAchievements.Achievement,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (achievement.isUnlocked) Color(0xFFFFF0F5) else Color(0xFFF5F5F5),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = achievement.icon,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = achievement.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (achievement.isUnlocked) Color(0xFF333333) else Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = achievement.description,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            if (achievement.isUnlocked) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Desbloqueado",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Bloqueado",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
