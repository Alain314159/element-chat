/*
 * Copyright (c) 2026 Element Chat Romántico
 *
 * Navegación para features románticos
 */

package im.vector.app.features.romantic.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import im.vector.app.features.romantic.games.AffectionCounterScreen
import im.vector.app.features.romantic.games.AffectionCounter
import im.vector.app.features.romantic.games.LoveCardsGame
import im.vector.app.features.romantic.games.LoveCardsGameActivity
import im.vector.app.features.romantic.ui.RomanticEffectsScreen
import im.vector.app.features.romantic.ui.RomanticHubScreen
import im.vector.app.features.romantic.ui.RomanticFeaturesPanel

/**
 * Rutas de navegación para features románticos
 */
object RomanticNavRoutes {
    const val HUB = "romantic_hub"
    const val DAYS = "romantic_days"
    const val NOTES = "romantic_notes"
    const val GIFTS = "romantic_gifts"
    const val CHALLENGES = "romantic_challenges"
    const val TIME_CAPSULES = "romantic_time_capsules"
    const val SETTINGS = "romantic_settings"
    const val ALBUM = "romantic_album"
    const val ALBUM_DETAIL = "romantic_album/{albumId}"
    
    // Rutas para juegos y efectos
    const val LOVE_CARDS_GAME = "romantic_love_cards_game"
    const val ROMANTIC_EFFECTS = "romantic_effects"
    const val AFFECTION_COUNTER = "romantic_affection_counter"
    const val LOVE_ACHIEVEMENTS = "romantic_love_achievements"
}

/**
 * Añade el gráfico de navegación romántico al NavHost.
 */
fun NavGraphBuilder.romanticNavGraph(
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    onOpenAlbum: (String) -> Unit
) {
    // Hub principal romántico
    composable(route = RomanticNavRoutes.HUB) {
        RomanticHubScreen(
            onNavigateToDays = { navController.navigate(RomanticNavRoutes.DAYS) },
            onNavigateToNotes = { navController.navigate(RomanticNavRoutes.NOTES) },
            onNavigateToGifts = { navController.navigate(RomanticNavRoutes.GIFTS) },
            onNavigateToChallenges = { navController.navigate(RomanticNavRoutes.CHALLENGES) },
            onNavigateToTimeCapsules = { navController.navigate(RomanticNavRoutes.TIME_CAPSULES) },
            onNavigateToSettings = { navController.navigate(RomanticNavRoutes.SETTINGS) },
            onNavigateToAlbum = { onOpenAlbum(it) },
            onNavigateBack = onNavigateBack
        )
    }

    // Días de relación
    composable(route = RomanticNavRoutes.DAYS) {
        // TODO: Implementar pantalla de días
        RomanticFeaturesPanel(
            title = "📅 Días Juntos",
            onNavigateBack = onNavigateBack
        )
    }

    // Notas de amor
    composable(route = RomanticNavRoutes.NOTES) {
        // TODO: Implementar pantalla de notas
        RomanticFeaturesPanel(
            title = "💌 Notas de Amor",
            onNavigateBack = onNavigateBack
        )
    }

    // Regalos románticos
    composable(route = RomanticNavRoutes.GIFTS) {
        // TODO: Implementar pantalla de regalos
        RomanticFeaturesPanel(
            title = "🎁 Regalos Románticos",
            onNavigateBack = onNavigateBack
        )
    }

    // Desafíos de pareja
    composable(route = RomanticNavRoutes.CHALLENGES) {
        // TODO: Implementar pantalla de desafíos
        RomanticFeaturesPanel(
            title = "🎯 Desafíos de Pareja",
            onNavigateBack = onNavigateBack
        )
    }

    // Cápsulas del tiempo
    composable(route = RomanticNavRoutes.TIME_CAPSULES) {
        // TODO: Implementar pantalla de cápsulas
        RomanticFeaturesPanel(
            title = "⏳ Cápsulas del Tiempo",
            onNavigateBack = onNavigateBack
        )
    }

    // Configuración romántica
    composable(route = RomanticNavRoutes.SETTINGS) {
        // TODO: Implementar pantalla de settings
        RomanticFeaturesPanel(
            title = "⚙️ Configuración Romántica",
            onNavigateBack = onNavigateBack
        )
    }

    // Detalle de álbum
    composable(
        route = RomanticNavRoutes.ALBUM_DETAIL,
        arguments = listOf(
            androidx.navigation.navArgument("albumId") {
                type = androidx.navigation.NavType.StringType
            }
        )
    ) {
        // TODO: Implementar pantalla de detalle de álbum
        RomanticFeaturesPanel(
            title = "📷 Álbum",
            onNavigateBack = onNavigateBack
        )
    }

    // Juego de Tarjetas de Amor
    composable(route = RomanticNavRoutes.LOVE_CARDS_GAME) {
        LoveCardsGame(
            onBackClick = onNavigateBack
        )
    }

    // Configuración de Efectos Románticos
    composable(route = RomanticNavRoutes.ROMANTIC_EFFECTS) {
        RomanticEffectsScreen(
            onBackClick = onNavigateBack
        )
    }

    // Contador de Afecto (Besos y Abrazos)
    composable(
        route = RomanticNavRoutes.AFFECTION_COUNTER,
        arguments = listOf(
            androidx.navigation.navArgument("focusMode") {
                type = androidx.navigation.NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val focusMode = backStackEntry.arguments.getString("focusMode")
        // Nota: AffectionCounter requiere una instancia del contador
        // Se debe pasar desde la Activity o usar Hilt para inyección
        RomanticFeaturesPanel(
            title = when (focusMode) {
                "kiss" -> "💋 Contador de Besos"
                "hug" -> "🤗 Contador de Abrazos"
                else -> "💕 Contador de Amor"
            },
            onNavigateBack = onNavigateBack
        )
    }

    // Logros de Amor
    composable(route = RomanticNavRoutes.LOVE_ACHIEVEMENTS) {
        RomanticFeaturesPanel(
            title = "🏆 Logros de Amor",
            onNavigateBack = onNavigateBack
        )
    }
}

/**
 * Extensión para navegar al hub romántico desde cualquier pantalla.
 */
fun NavHostController.navigateRomanticHub() {
    navigate(RomanticNavRoutes.HUB) {
        popUpTo(0) { inclusive = false }
        launchSingleTop = true
    }
}

/**
 * Extensiones de navegación para juegos y efectos románticos
 */
fun NavHostController.navigateRomanticEffects() {
    navigate(RomanticNavRoutes.ROMANTIC_EFFECTS) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateLoveCardsGame() {
    navigate(RomanticNavRoutes.LOVE_CARDS_GAME) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateAffectionCounter(focusMode: String? = null) {
    val route = if (focusMode != null) {
        "${RomanticNavRoutes.AFFECTION_COUNTER}/$focusMode"
    } else {
        RomanticNavRoutes.AFFECTION_COUNTER
    }
    navigate(route) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateLoveAchievements() {
    navigate(RomanticNavRoutes.LOVE_ACHIEVEMENTS) {
        launchSingleTop = true
    }
}
