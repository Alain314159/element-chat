/*
 * Copyright 2024 Cerdita App
 *
 * RomanticOnboardingScreen3 - Tercera pantalla: "Hagan crecer su amor"
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
 * Onboarding Screen 3: Hagan crecer su amor
 *
 * Muestra:
 * - Animación Lottie de Árbol creciendo con hitos
 * - Título: "Hagan crecer su amor"
 * - Descripción: "Desbloquea logros, crea recuerdos juntos..."
 */
@Composable
fun RomanticOnboardingScreen3(
    modifier: Modifier = Modifier
) {
    // Intentar cargar animación Lottie (usará placeholder si no existe)
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_onboarding_growth)
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
                        Color(0xFFF0FFF0), // Verde muy suave
                        Color(0xFF90EE90), // Verde claro
                        Color(0xFFFFB6C1)  // Rosa cerdita
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
                text = "3/3",
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
                text = "Hagan crecer su amor",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Desbloquea logros, crea recuerdos juntos y vean florecer su árbol de amor.",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            // Iconos decorativos
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "🌳",
                fontSize = 80.sp
            )

            // Features destacadas
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf(
                    "🏆 Logros compartidos",
                    "📸 Recuerdos especiales",
                    "🌱 Árbol de amor"
                ).forEach { feature ->
                    Text(
                        text = feature,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // Mensaje final
            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                color = Color.White.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "¡Todo listo para comenzar su aventura romántica! 💝",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

/**
 * Versión con placeholder si la animación no existe
 */
@Composable
fun RomanticOnboardingScreen3Placeholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0FFF0),
                        Color(0xFF90EE90),
                        Color(0xFFFFB6C1)
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
                text = "3/3",
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
                    text = "🌳💕",
                    fontSize = 100.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Título
            Text(
                text = "Hagan crecer su amor",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Desbloquea logros, crea recuerdos juntos y vean florecer su árbol de amor.",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "🌳",
                fontSize = 80.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf(
                    "🏆 Logros compartidos",
                    "📸 Recuerdos especiales",
                    "🌱 Árbol de amor"
                ).forEach { feature ->
                    Text(
                        text = feature,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                color = Color.White.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "¡Todo listo para comenzar su aventura romántica! 💝",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
