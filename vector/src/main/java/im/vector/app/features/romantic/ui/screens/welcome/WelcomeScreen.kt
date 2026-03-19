/*
 * Copyright 2024 Cerdita App
 *
 * WelcomeScreen - Pantalla de bienvenida con animación Lottie
 */

package im.vector.app.features.romantic.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import im.vector.app.R

/**
 * WelcomeScreen - Pantalla de bienvenida de Cerdita
 *
 * Muestra:
 * - Animación Lottie de Cerdita + Koalita saludando (3s loop)
 * - Logo de la app
 * - Título: "Cerdita 💕"
 * - Subtítulo: "Chat romántico para ustedes dos"
 * - Botón: "Comenzar"
 * - Background: Gradiente rosa-amarillo
 */
@Composable
fun WelcomeScreen(
    onBeginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Cargar animación Lottie (usa placeholder si no existe)
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_welcome)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        restartOnPlay = true
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFB6C1), // Rosa cerdita
                        Color(0xFFFFFACD), // Amarillo suave
                        Color(0xFFFFE4B5)  // Amarillo más intenso
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animación Lottie
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(280.dp)
                    .padding(bottom = 32.dp)
            )

            // Título
            Text(
                text = "Cerdita 💕",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo
            Text(
                text = "Chat romántico para ustedes dos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón Comenzar
            Button(
                onClick = onBeginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    text = "Comenzar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nota al pie
            Text(
                text = "Un espacio solo para ustedes dos 💝",
                fontSize = 14.sp,
                color = Color(0xFF777777),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * WelcomeScreen con placeholder si la animación no existe
 */
@Composable
fun WelcomeScreenWithPlaceholder(
    onBeginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFB6C1), // Rosa cerdita
                        Color(0xFFFFFACD), // Amarillo suave
                        Color(0xFFFFE4B5)  // Amarillo más intenso
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder para la animación (emoji de Cerdita + Koalita)
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🐷💕🐨",
                    fontSize = 120.sp
                )
            }

            // Título
            Text(
                text = "Cerdita 💕",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo
            Text(
                text = "Chat romántico para ustedes dos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón Comenzar
            Button(
                onClick = onBeginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    text = "Comenzar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nota al pie
            Text(
                text = "Un espacio solo para ustedes dos 💝",
                fontSize = 14.sp,
                color = Color(0xFF777777),
                textAlign = TextAlign.Center
            )
        }
    }
}
