/*
 * Copyright 2024 Cerdita App
 *
 * Botón de Abrazo Mágico - Componente Jetpack Compose
 * Permite enviar abrazos virtuales a tu pareja con sistema de reacciones
 */

package im.vector.app.features.romantic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import im.vector.app.R

/**
 * Botón de Abrazo Mágico con Reacciones
 *
 * COMPORTAMIENTO:
 * - Al presionar: Muestra animación de corazones
 * - Envía mensaje automático: "Te envío un abrazo 🐷🤗🐨"
 * - 4 tipos de abrazos disponibles
 * - Sistema de reacciones a abrazos recibidos (❤️, 🥰, 😊)
 *
 * UBICACIÓN: Esquina inferior derecha del chat
 */
@Composable
fun HugButton(
    onClick: (HugType) -> Unit,
    modifier: Modifier = Modifier,
    hugType: HugType = HugType.NORMAL,
    isEnabled: Boolean = true,
    onReactToHug: ((Int, HugReactionType) -> Unit)? = null,
    lastHugId: Int? = null
) {
    var showAnimation by remember { mutableStateOf(false) }
    var showReactionPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Intentar cargar animación, fallback a null si no existe
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            when (hugType) {
                HugType.NORMAL -> R.raw.anim_hug_normal
                HugType.LONG -> R.raw.anim_hug_long
                HugType.TIGHT -> R.raw.anim_hug_tight
                HugType.SPIN -> R.raw.anim_hug_spin
            }
        )
    )

    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = {
                if (isEnabled) {
                    showAnimation = true
                    onClick(hugType)
                }
            },
            containerColor = Color(0xFFFFB6C1), // Rosa pastel
            enabled = isEnabled
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Enviar abrazo",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }

        // Animación overlay
        if (showAnimation && composition != null) {
            HugAnimationOverlay(
                composition = composition!!,
                onFinished = { showAnimation = false }
            )
        }

        // Selector de reacciones (se muestra cuando hay un abrazo recibido)
        if (showReactionPicker && lastHugId != null && onReactToHug != null) {
            HugReactionPicker(
                onReactionSelected = { reaction ->
                    onReactToHug(lastHugId, reaction)
                    showReactionPicker = false
                },
                onDismiss = { showReactionPicker = false }
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
 * Selector de reacciones para abrazos
 * Permite reaccionar con ❤️, 🥰, 😊
 */
@Composable
fun HugReactionPicker(
    onReactionSelected: (HugReactionType) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            HugReactionButton(
                emoji = "❤️",
                onClick = { onReactionSelected(HugReactionType.HEART) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            HugReactionButton(
                emoji = "🥰",
                onClick = { onReactionSelected(HugReactionType.LOVE_FACE) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            HugReactionButton(
                emoji = "😊",
                onClick = { onReactionSelected(HugReactionType.SMILE) }
            )
        }
    }
}

/**
 * Botón individual de reacción
 */
@Composable
fun HugReactionButton(
    emoji: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 28.sp,
            modifier = Modifier
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
 * Tipos de reacción a abrazos
 */
enum class HugReactionType {
    HEART,      // ❤️
    LOVE_FACE,  // 🥰
    SMILE,      // 😊
    FIRE,       // 🔥
    HUG_BACK    // 🤗
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

    fun getReactionEmoji(reactionType: HugReactionType): String {
        return when (reactionType) {
            HugReactionType.HEART -> "❤️"
            HugReactionType.LOVE_FACE -> "🥰"
            HugReactionType.SMILE -> "😊"
            HugReactionType.FIRE -> "🔥"
            HugReactionType.HUG_BACK -> "🤗"
        }
    }
}
