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

// Nota: Las siguientes activities ahora están en archivos separados:
// - LoveCardsGameActivity -> features/romantic/games/LoveCardsGameActivity.kt
// - RomanticEffectsActivity -> features/romantic/ui/RomanticEffectsActivity.kt
// - AffectionCounterActivity -> (pendiente de implementación completa)
