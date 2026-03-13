/*
 * Copyright 2024 Cerdita App
 * 
 * Botón de Abrazo Mágico - Componente Jetpack Compose
 * Permite enviar abrazos virtuales a tu pareja
 */

package im.vector.app.features.romantic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Botón de Abrazo Mágico
 * 
 * COMPORTAMIENTO:
 * - Al presionar: Muestra animación de corazones
 * - Envía mensaje automático: "Te envío un abrazo 🐷🤗🐨"
 * - 4 tipos de abrazos disponibles
 * 
 * UBICACIÓN: Esquina inferior derecha del chat
 */
@Composable
fun HugButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hugType: HugType = HugType.NORMAL
) {
    var showAnimation by remember { mutableStateOf(false) }
    
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            when (hugType) {
                HugType.NORMAL -> im.vector.app.R.raw.anim_hug_normal
                HugType.LONG -> im.vector.app.R.raw.anim_hug_long
                HugType.TIGHT -> im.vector.app.R.raw.anim_hug_tight
                HugType.SPIN -> im.vector.app.R.raw.anim_hug_spin
            }
        )
    )
    
    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = {
                showAnimation = true
                onClick()
            },
            containerColor = Color(0xFFFFB6C1) // Rosa pastel
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Enviar abrazo",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }
        
        // Animación overlay (se mostraría como Dialog en implementación completa)
        if (showAnimation) {
            HugAnimationOverlay(
                composition = composition,
                onFinished = { showAnimation = false }
            )
        }
    }
}

/**
 * Overlay de animación de abrazo
 */
@Composable
fun HugAnimationOverlay(
    composition: com.airbnb.lottie.compose.LottieComposition,
    onFinished: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(200.dp)
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateOnce,
            onAnimationEnd = onFinished
        )
    }
}

/**
 * Tipos de abrazos disponibles
 */
enum class HugType {
    NORMAL,     // Abrazo normal
    LONG,       // Abrazo largo
    TIGHT,      // Abrazo fuerte
    SPIN        // Abrazo con giro
}

/**
 * Mensajes automáticos para cada tipo de abrazo
 */
object HugMessages {
    fun getMessage(hugType: HugType): String {
        return when (hugType) {
            HugType.NORMAL -> "Te envío un abrazo 🐷🤗🐨"
            HugType.LONG -> "Te mando un abrazo bien largo 💕"
            HugType.TIGHT -> "Te abrazo muy fuerte 🤗💖"
            HugType.SPIN -> "¡Abrazo con giro! 🐷🤗🐨💫"
        }
    }
}
