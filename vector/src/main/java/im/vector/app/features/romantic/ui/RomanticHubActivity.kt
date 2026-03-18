/*
 * Copyright 2024 Cerdita App
 *
 * Activity principal del Hub Romántico
 * Punto de entrada para todas las features románticas
 */

package im.vector.app.features.romantic.ui

import android.content.Intent
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
import im.vector.app.features.romantic.RelationshipDaysManager
import im.vector.app.features.romantic.games.AffectionCounter
import im.vector.app.features.romantic.features.LoveNotesManager
import im.vector.app.features.romantic.settings.RomanticSettingsActivity

/**
 * RomanticHubActivity - Main entry point for romantic features
 *
 * Provides access to:
 * - Days Together Counter
 * - Love Cards Game
 * - Kiss Counter
 * - Hugs Counter
 * - Love Notes
 * - Relationship Tree
 * - Romantic Effects
 * - Romantic Settings
 */
class RomanticHubActivity : ComponentActivity() {

    private lateinit var daysManager: RelationshipDaysManager
    private lateinit var affectionCounter: AffectionCounter
    private lateinit var notesManager: LoveNotesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize managers
        daysManager = RelationshipDaysManager(this)
        affectionCounter = AffectionCounter(this)
        notesManager = LoveNotesManager(this)

        setContent {
            RomanticHubScreen(
                daysManager = daysManager,
                affectionCounter = affectionCounter,
                notesManager = notesManager,
                onNavigateToFeature = { feature -> navigateToFeature(feature) }
            )
        }
    }

    /**
     * Maneja la navegación a cada feature romántico
     */
    private fun navigateToFeature(feature: RomanticFeature) {
        val intent = when (feature) {
            is RomanticFeature.DaysTogether -> {
                // Navega al contador de días (puede ser la misma pantalla o settings)
                Intent(this, RomanticSettingsActivity::class.java).apply {
                    putExtra(RomanticSettingsActivity.EXTRA_SHOW_DATE_PICKER, true)
                }
            }
            is RomanticFeature.LoveCards -> {
                // El juego LoveCardsGame es un composable, se puede mostrar en esta actividad
                // o en una actividad dedicada si se prefiere
                Intent(this, LoveCardsGameActivity::class.java)
            }
            is RomanticFeature.KissCounter -> {
                // AffectionCounter es un widget, mostramos pantalla de affection
                Intent(this, AffectionCounterActivity::class.java).apply {
                    putExtra(AffectionCounterActivity.EXTRA_FOCUS_MODE, AffectionCounterActivity.MODE_KISS)
                }
            }
            is RomanticFeature.HugsCounter -> {
                Intent(this, AffectionCounterActivity::class.java).apply {
                    putExtra(AffectionCounterActivity.EXTRA_FOCUS_MODE, AffectionCounterActivity.MODE_HUG)
                }
            }
            is RomanticFeature.LoveNotes -> {
                Intent(this, LoveNotesActivity::class.java)
            }
            is RomanticFeature.RelationshipTree -> {
                Intent(this, RelationshipTreeActivity::class.java)
            }
            is RomanticFeature.RomanticEffects -> {
                // Efectos románticos - puede ir a settings o a una pantalla dedicada
                Intent(this, RomanticEffectsActivity::class.java)
            }
            is RomanticFeature.Settings -> {
                Intent(this, RomanticSettingsActivity::class.java)
            }
        }
        startActivity(intent)
    }
}

/**
 * Actividad para mostrar el juego de LoveCardsGame
 */
class LoveCardsGameActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoveCardsGameScreen(
                onBackClick = { finish() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveCardsGameScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🎴 Tarjetas de Amor",
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
            LoveCardsGame(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                onCardDrawn = { card ->
                    // Manejar carta robada (analytics, etc.)
                }
            )
        }
    }
}

/**
 * Actividad para mostrar el contador de afecto (besos/abrazos)
 */
class AffectionCounterActivity : ComponentActivity() {

    companion object {
        const val EXTRA_FOCUS_MODE = "focus_mode"
        const val MODE_KISS = "kiss"
        const val MODE_HUG = "hug"
    }

    private lateinit var affectionCounter: AffectionCounter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        affectionCounter = AffectionCounter(this)

        val focusMode = intent.getStringExtra(EXTRA_FOCUS_MODE)

        setContent {
            AffectionCounterScreen(
                affectionCounter = affectionCounter,
                focusMode = focusMode,
                onBackClick = { finish() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffectionCounterScreen(
    affectionCounter: AffectionCounter,
    focusMode: String?,
    onBackClick: () -> Unit
) {
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
                onSendKiss = { /* Manejar envío de beso */ },
                onSendHug = { /* Manejar envío de abrazo */ },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Actividad para efectos románticos
 */
class RomanticEffectsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RomanticEffectsScreen(
                onBackClick = { finish() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RomanticEffectsScreen(
    onBackClick: () -> Unit
) {
    var effectsEnabled by remember { mutableStateOf(true) }
    var showHeartRain by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "✨ Efectos Románticos",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Switch para activar/desactivar efectos
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFF0F5),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Activar efectos románticos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Muestra corazones y besos al detectar palabras románticas",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = effectsEnabled,
                            onCheckedChange = { effectsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFFFFB6C1)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de prueba
                Button(
                    onClick = { showHeartRain = true },
                    enabled = effectsEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Probar lluvia de corazones")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de efectos disponibles
                Text(
                    text = "Efectos disponibles:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                listOf(
                    "💕 Lluvia de corazones",
                    "💋 Besos voladores",
                    "🌸 Flores bloom",
                    "✨ Estrellas brillantes"
                ).forEach { effect ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = effect,
                            fontSize = 14.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
            }

            // Efecto de lluvia de corazones
            if (showHeartRain && effectsEnabled) {
                HeartRainEffect(
                    isActive = true,
                    onFinished = { showHeartRain = false }
                )
            }
        }
    }
}
