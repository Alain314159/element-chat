/*
 * Copyright 2024 Cerdita App
 *
 * Activity principal del Hub Romántico
 * Punto de entrada para todas las features románticas
 */

package im.vector.app.features.romantic.ui

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
 * RomanticHubActivity - Main entry point for romantic features
 * 
 * Provides access to:
 * - Love Coupons
 * - Love Notes
 * - Time Capsules
 * - Relationship Tree
 * - First Times Registry
 * - Love Statistics
 * - Couple Games
 * - Mascot System
 */
class RomanticHubActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RomanticHubScreen(
                onBackClick = { finish() },
                onNavigateToSettings = { /* Navigate to settings */ },
                onNavigateToCoupons = { /* Navigate to coupons */ },
                onNavigateToNotes = { /* Navigate to notes */ },
                onNavigateToTimeCapsules = { /* Navigate to time capsules */ },
                onNavigateToRelationshipTree = { /* Navigate to relationship tree */ },
                onNavigateToFirstTimes = { /* Navigate to first times */ },
                onNavigateToStatistics = { /* Navigate to statistics */ },
                onNavigateToGames = { /* Navigate to games */ },
                onNavigateToMascots = { /* Navigate to mascots */ }
            )
        }
    }
}
