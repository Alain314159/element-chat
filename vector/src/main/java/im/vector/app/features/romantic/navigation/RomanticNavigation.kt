/*
 * Copyright (c) 2026 Element Chat Romántico
 *
 * Navegación para features románticos
 */

package im.vector.app.features.romantic.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
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
