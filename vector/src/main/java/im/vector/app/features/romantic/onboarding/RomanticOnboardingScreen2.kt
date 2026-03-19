/*
 * Copyright 2024 Cerdita App
 *
 * RomanticOnboardingScreen2 - Segunda pantalla: "Comparte momentos"
 */

package im.vector.app.features.romantic.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import im.vector.app.R

/**
 * Onboarding Screen 2: Comparte momentos
 *
 * Muestra:
 * - Animación Lottie de Corazón con efectos románticos
 * - Título: "Comparte momentos"
 * - Descripción: "Envía abrazos, besos y efectos románticos..."
 */
@Composable
fun RomanticOnboardingScreen2(
    modifier: Modifier = Modifier
) {
    // Intentar cargar animación Lottie (usará placeholder si no existe)
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_onboarding_share)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF0F5), // Lavanda muy suave
                        Color(0xFFFFB6C1), // Rosa cerdita
                        Color(0xFFFFD700)  // Dorado
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Número de página
            Text(
                text = "2/3",
                fontSize = 16.sp,
                color = Color(0xFF999999),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Animación Lottie
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Título
            Text(
                text = "Comparte momentos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Envía abrazos, besos y efectos románticos. Haz que cada mensaje sea especial.",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            // Iconos decorativos
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🤗",
                    fontSize = 48.sp
                )
                Text(
                    text = " 💋 ",
                    fontSize = 42.sp
                )
                Text(
                    text = "✨",
                    fontSize = 48.sp
                )
            }

            // Features destacadas
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf(
                    "💕 Abrazos mágicos",
                    "💋 Besos románticos",
                    "✨ Efectos especiales"
                ).forEach { feature ->
                    Text(
                        text = feature,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Versión con placeholder si la animación no existe
 */
@Composable
fun RomanticOnboardingScreen2Placeholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF0F5),
                        Color(0xFFFFB6C1),
                        Color(0xFFFFD700)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Número de página
            Text(
                text = "2/3",
                fontSize = 16.sp,
                color = Color(0xFF999999),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder para animación
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💕✨",
                    fontSize = 100.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Título
            Text(
                text = "Comparte momentos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Envía abrazos, besos y efectos románticos. Haz que cada mensaje sea especial.",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🤗",
                    fontSize = 48.sp
                )
                Text(
                    text = " 💋 ",
                    fontSize = 42.sp
                )
                Text(
                    text = "✨",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf(
                    "💕 Abrazos mágicos",
                    "💋 Besos románticos",
                    "✨ Efectos especiales"
                ).forEach { feature ->
                    Text(
                        text = feature,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
