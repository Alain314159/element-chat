/*
 * Copyright 2024 Cerdita App
 *
 * RomanticOnboardingActivity - Actividad contenedora para el Onboarding con ViewPager2
 */

package im.vector.app.features.romantic.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import im.vector.app.features.romantic.ui.RomanticHubActivity
import kotlinx.coroutines.launch

/**
 * RomanticOnboardingActivity - Actividad de Onboarding con 3 pantallas
 *
 * Usa ViewPager2 (HorizontalPager de Compose) para navegación entre pantallas
 */
class RomanticOnboardingActivity : ComponentActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RomanticOnboardingActivity::class.java)
        }
    }

    private val viewModel: RomanticOnboardingViewModel by viewModels {
        RomanticOnboardingViewModel.Factory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RomanticOnboardingScreen(
                viewModel = viewModel,
                onComplete = ::navigateToHub,
                modifier = Modifier.background(Color.White)
            )
        }

        // Observar eventos del ViewModel
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is RomanticOnboardingViewModel.OnboardingComplete -> {
                            navigateToHub()
                        }
                        else -> { /* Manejar otros eventos si es necesario */ }
                    }
                }
            }
        }
    }

    private fun navigateToHub() {
        startActivity(RomanticHubActivity.newIntent(this))
        finish()
    }
}

/**
 * Extension para crear Intent desde RomanticHubActivity
 */
fun RomanticHubActivity.newIntent(context: Context): Intent {
    return Intent(context, RomanticHubActivity::class.java)
}

/**
 * Pantalla principal del Onboarding con ViewPager2
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RomanticOnboardingScreen(
    viewModel: RomanticOnboardingViewModel,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        viewModel.totalPages
    }

    // Sincronizar pager con el estado del ViewModel
    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }

    // Escuchar cambios de página
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                viewModel.setCurrentPage(page)
            }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Pager con las 3 pantallas
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> RomanticOnboardingScreen1Placeholder()
                    1 -> RomanticOnboardingScreen2Placeholder()
                    2 -> RomanticOnboardingScreen3Placeholder()
                }
            }

            // Indicadores de página (puntos)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(viewModel.totalPages) { index ->
                    val isSelected = index == state.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color(0xFFFF6B6B) else Color(0xFFCCCCCC)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de navegación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Saltar (solo en primeras 2 páginas)
                if (state.currentPage < viewModel.totalPages - 1) {
                    TextButton(
                        onClick = { viewModel.skipOnboarding() }
                    ) {
                        Text(
                            text = "Saltar",
                            fontSize = 16.sp,
                            color = Color(0xFF777777)
                        )
                    }
                } else {
                    // Espaciador en la última página
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // Botón Siguiente / Comenzar
                Button(
                    onClick = {
                        if (state.currentPage < viewModel.totalPages - 1) {
                            viewModel.nextPage()
                        } else {
                            viewModel.completeOnboarding()
                            onComplete()
                        }
                    },
                    modifier = Modifier
                        .width(140.dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text(
                        text = if (state.currentPage < viewModel.totalPages - 1) {
                            "Siguiente"
                        } else {
                            "Comenzar"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
