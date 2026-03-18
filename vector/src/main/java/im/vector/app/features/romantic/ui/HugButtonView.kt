/*
 * Copyright 2024 Cerdita App
 *
 * Wrapper Android View para el HugButton de Jetpack Compose
 * Permite integrar el botón de abrazos en layouts XML tradicionales
 */

package im.vector.app.features.romantic.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import im.vector.app.R

/**
 * Wrapper View para el HugButton
 * 
 * Este View permite usar el HugButton (originalmente en Compose)
 * en layouts XML tradicionales de Android Views.
 * 
 * Uso en XML:
 * <im.vector.app.features.romantic.ui.HugButtonView
 *     android:id="@+id/hugButton"
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     android:layout_gravity="bottom|end"
 *     android:layout_margin="16dp" />
 */
class HugButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var onHugClickListener: ((HugType) -> Unit)? = null
    private var isEnabled: Boolean = true
    private var hugType: HugType = HugType.NORMAL

    init {
        setupComposeView()
    }

    private fun setupComposeView() {
        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HugButtonContent(
                    hugType = this@HugButtonView.hugType,
                    isEnabled = this@HugButtonView.isEnabled,
                    onClick = { hugType ->
                        onHugClickListener?.invoke(hugType)
                    }
                )
            }
        }

        addView(
            composeView,
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                android.view.Gravity.BOTTOM or android.view.Gravity.END
            )
        )
    }

    /**
     * Configura el listener para cuando se presiona el botón de abrazo
     */
    fun setOnHugClickListener(listener: (HugType) -> Unit) {
        onHugClickListener = listener
    }

    /**
     * Establece el tipo de abrazo
     */
    fun setHugType(type: HugType) {
        hugType = type
    }

    /**
     * Habilita o deshabilita el botón
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    /**
     * Muestra la animación de abrazo programáticamente
     */
    fun showAnimation() {
        // Esto podría expandirse para controlar la animación desde fuera
    }
}

/**
 * Contenido Compose para el HugButtonView
 */
@Composable
private fun HugButtonContent(
    hugType: HugType,
    isEnabled: Boolean,
    onClick: (HugType) -> Unit
) {
    var showAnimation by remember { mutableStateOf(false) }

    // Intentar cargar animación con fallback seguro
    val compositionResult = rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            when (hugType) {
                HugType.NORMAL -> R.raw.anim_hug_normal
                HugType.LONG -> R.raw.anim_hug_long
                HugType.TIGHT -> R.raw.anim_hug_tight
                HugType.SPIN -> R.raw.anim_hug_spin
            }
        )
    )

    Box(
        modifier = Modifier
    ) {
        FloatingActionButton(
            onClick = {
                if (isEnabled) {
                    showAnimation = true
                    onClick(hugType)
                }
            },
            containerColor = Color(0xFFFFB6C1),
            enabled = isEnabled,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Enviar abrazo",
                modifier = Modifier.size(28.dp),
                tint = Color.White
            )
        }

        // Animación overlay (solo si la composición se cargó correctamente)
        if (showAnimation && compositionResult.value != null) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            ) {
                LottieAnimation(
                    composition = compositionResult.value!!,
                    iterations = LottieConstants.IterateOnce,
                    onAnimationEnd = { showAnimation = false }
                )
            }
        }
    }
}
