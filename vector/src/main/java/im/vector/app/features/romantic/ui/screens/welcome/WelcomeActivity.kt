/*
 * Copyright 2024 Cerdita App
 *
 * WelcomeActivity - Actividad contenedora para el WelcomeScreen
 */

package im.vector.app.features.romantic.ui.screens.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import im.vector.app.features.romantic.onboarding.RomanticOnboardingActivity

/**
 * WelcomeActivity - Punto de entrada inicial de la app romántica
 *
 * Muestra el WelcomeScreen y navega al Onboarding cuando el usuario presiona "Comenzar"
 */
class WelcomeActivity : ComponentActivity() {

    companion object {
        const val EXTRA_SKIP_WELCOME = "extra_skip_welcome"

        fun newIntent(context: Context, skipWelcome: Boolean = false): Intent {
            return Intent(context, WelcomeActivity::class.java).apply {
                putExtra(EXTRA_SKIP_WELCOME, skipWelcome)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val skipWelcome = intent.getBooleanExtra(EXTRA_SKIP_WELCOME, false)

        if (skipWelcome) {
            navigateToOnboarding()
            return
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WelcomeScreenWithPlaceholder(
                        onBeginClick = ::navigateToOnboarding,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    private fun navigateToOnboarding() {
        startActivity(RomanticOnboardingActivity.newIntent(this))
        finish()
    }
}
