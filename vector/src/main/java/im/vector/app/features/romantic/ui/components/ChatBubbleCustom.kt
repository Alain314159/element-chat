/*
 * Copyright 2024 Cerdita App
 *
 * Burbujas de Chat Personalizables - Componente Jetpack Compose
 * Permite personalizar la apariencia de las burbujas de mensaje
 */

package im.vector.app.features.romantic.ui.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.path.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import im.vector.app.core.di.DefaultPreferences
import javax.inject.Inject

/**
 * Formas disponibles para las burbujas de chat
 */
enum class BubbleShape {
    ROUNDED,    // Esquinas redondeadas tradicionales
    RECTANGLE,  // Rectángulo con esquinas cuadradas
    OVAL,       // Forma ovalada/elíptica
    CLOUD,      // Forma de nube con ondulaciones
    HEART       // Forma de corazón
}

/**
 * Estilo de burbuja personalizable
 *
 * @param color Color de fondo de la burbuja
 * @param shape Forma de la burbuja
 * @param cornerRadius Radio de las esquinas (para formas que lo soporten)
 * @param borderColor Color del borde (opcional, null para sin borde)
 * @param borderWidth Grosor del borde (0.dp para sin borde)
 */
data class BubbleStyle(
    val color: Color,
    val shape: BubbleShape,
    val cornerRadius: Dp = 16.dp,
    val borderColor: Color? = null,
    val borderWidth: Dp = 0.dp
)

/**
 * Preferencias de burbuja por usuario
 *
 * @param userId Identificador único del usuario
 * @param bubbleStyle Estilo de burbuja preferido
 * @param savedAt Timestamp de cuando se guardaron las preferencias
 */
data class UserBubblePreferences(
    val userId: String,
    val bubbleStyle: BubbleStyle,
    val savedAt: Long
)

/**
 * Gestor de preferencias de burbujas de chat
 * Usa SharedPreferences para almacenar las preferencias
 */
class BubblePreferencesManager @Inject constructor(
    @DefaultPreferences private val preferences: SharedPreferences
) {
    companion object {
        private const val PREF_PREFIX = "bubble_prefs_"
        private const val KEY_SHAPE = "_shape"
        private const val KEY_COLOR = "_color"
        private const val KEY_CORNER_RADIUS = "_corner_radius"
        private const val KEY_BORDER_COLOR = "_border_color"
        private const val KEY_BORDER_WIDTH = "_border_width"
        private const val KEY_SAVED_AT = "_saved_at"
    }

    /**
     * Guarda las preferencias de burbuja para un usuario
     */
    fun saveBubblePreferences(userId: String, bubbleStyle: BubbleStyle) {
        preferences.edit {
            val prefix = "$PREF_PREFIX$userId"
            putString("${prefix}${KEY_SHAPE}", bubbleStyle.shape.name)
            putInt("${prefix}${KEY_COLOR}", bubbleStyle.color.toArgb())
            putFloat("${prefix}${KEY_CORNER_RADIUS}", bubbleStyle.cornerRadius.value)
            
            bubbleStyle.borderColor?.let {
                putInt("${prefix}${KEY_BORDER_COLOR}", it.toArgb())
            } ?: remove("${prefix}${KEY_BORDER_COLOR}")
            
            putFloat("${prefix}${KEY_BORDER_WIDTH}", bubbleStyle.borderWidth.value)
            putLong("${prefix}${KEY_SAVED_AT}", System.currentTimeMillis())
        }
    }

    /**
     * Carga las preferencias de burbuja para un usuario
     * @return UserBubblePreferences o null si no existen preferencias guardadas
     */
    fun loadBubblePreferences(userId: String): UserBubblePreferences? {
        val prefix = "$PREF_PREFIX$userId"
        
        val shapeName = preferences.getString("${prefix}${KEY_SHAPE}", null) ?: return null
        val colorInt = preferences.getInt("${prefix}${KEY_COLOR}", 0)
        val cornerRadius = preferences.getFloat("${prefix}${KEY_CORNER_RADIUS}", 16f)
        val borderWidth = preferences.getFloat("${prefix}${KEY_BORDER_WIDTH}", 0f)
        val savedAt = preferences.getLong("${prefix}${KEY_SAVED_AT}", 0L)
        
        val borderColorInt = preferences.getInt("${prefix}${KEY_BORDER_COLOR}", -1)
        val borderColor = if (borderColorInt != -1) Color(borderColorInt) else null
        
        return UserBubblePreferences(
            userId = userId,
            bubbleStyle = BubbleStyle(
                color = Color(colorInt),
                shape = BubbleShape.valueOf(shapeName),
                cornerRadius = cornerRadius.dp,
                borderColor = borderColor,
                borderWidth = borderWidth.dp
            ),
            savedAt = savedAt
        )
    }

    /**
     * Elimina las preferencias de burbuja para un usuario
     */
    fun clearBubblePreferences(userId: String) {
        val prefix = "$PREF_PREFIX$userId"
        preferences.edit {
            remove("${prefix}${KEY_SHAPE}")
            remove("${prefix}${KEY_COLOR}")
            remove("${prefix}${KEY_CORNER_RADIUS}")
            remove("${prefix}${KEY_BORDER_COLOR}")
            remove("${prefix}${KEY_BORDER_WIDTH}")
            remove("${prefix}${KEY_SAVED_AT}")
        }
    }

    /**
     * Obtiene todos los IDs de usuario con preferencias guardadas
     */
    fun getAllUserIdsWithPreferences(): Set<String> {
        return preferences.all
            .keys
            .filter { it.startsWith(PREF_PREFIX) && it.endsWith(KEY_SAVED_AT) }
            .map { it.removePrefix(PREF_PREFIX).removeSuffix(KEY_SAVED_AT) }
            .toSet()
    }
}

/**
 * Convierte Color a Int ARGB para almacenamiento en SharedPreferences
 */
private fun Color.toArgb(): Int = this.toArgb()

/**
 * Burbuja de chat personalizada con soporte para múltiples formas
 *
 * @param message Texto del mensaje a mostrar
 * @param sender Remitente del mensaje (true = usuario, false = pareja)
 * @param bubbleStyle Estilo de la burbuja
 * @param modifier Modificador para personalizar el layout
 * @param onBubbleClick Callback para cuando se hace click en la burbuja
 */
@Composable
fun CustomChatBubble(
    message: String,
    sender: String,
    bubbleStyle: BubbleStyle,
    modifier: Modifier = Modifier,
    onBubbleClick: (() -> Unit)? = null
) {
    // Animación de entrada
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bubble_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "bubble_alpha"
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    val isFromUser = sender == "user"
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = if (isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
                .then(
                    if (onBubbleClick != null) {
                        Modifier.clickable(onClick = onBubbleClick)
                    } else {
                        Modifier
                    }
                )
        ) {
            // Dibujar la forma personalizada
            CustomBubbleBackground(
                bubbleStyle = bubbleStyle,
                isFromUser = isFromUser,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

/**
 * Dibuja el fondo personalizado de la burbuja según la forma especificada
 */
@Composable
private fun CustomBubbleBackground(
    bubbleStyle: BubbleStyle,
    isFromUser: Boolean,
    modifier: Modifier = Modifier
) {
    when (bubbleStyle.shape) {
        BubbleShape.ROUNDED -> RoundedBubbleBackground(
            bubbleStyle = bubbleStyle,
            isFromUser = isFromUser,
            modifier = modifier
        )
        BubbleShape.RECTANGLE -> RectangleBubbleBackground(
            bubbleStyle = bubbleStyle,
            isFromUser = isFromUser,
            modifier = modifier
        )
        BubbleShape.OVAL -> OvalBubbleBackground(
            bubbleStyle = bubbleStyle,
            modifier = modifier
        )
        BubbleShape.CLOUD -> CloudBubbleBackground(
            bubbleStyle = bubbleStyle,
            isFromUser = isFromUser,
            modifier = modifier
        )
        BubbleShape.HEART -> HeartBubbleBackground(
            bubbleStyle = bubbleStyle,
            modifier = modifier
        )
    }
}

/**
 * Fondo con esquinas redondeadas tradicionales
 */
@Composable
private fun RoundedBubbleBackground(
    bubbleStyle: BubbleStyle,
    isFromUser: Boolean,
    modifier: Modifier = Modifier
) {
    val cornerRadius = bubbleStyle.cornerRadius.value.dp
    
    Box(
        modifier = modifier
            .background(
                color = bubbleStyle.color,
                shape = RoundedCornerShape(
                    topStart = cornerRadius,
                    topEnd = cornerRadius,
                    bottomStart = if (isFromUser) cornerRadius else 4.dp,
                    bottomEnd = if (isFromUser) 4.dp else cornerRadius
                )
            )
    ) {
        // Borde opcional
        if (bubbleStyle.borderWidth > 0.dp && bubbleStyle.borderColor != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(
                            topStart = cornerRadius,
                            topEnd = cornerRadius,
                            bottomStart = if (isFromUser) cornerRadius else 4.dp,
                            bottomEnd = if (isFromUser) 4.dp else cornerRadius
                        ),
                        border = Stroke(
                            width = bubbleStyle.borderWidth.value,
                            color = bubbleStyle.borderColor
                        )
                    )
            )
        }
    }
}

/**
 * Fondo rectangular con esquinas cuadradas
 */
@Composable
private fun RectangleBubbleBackground(
    bubbleStyle: BubbleStyle,
    isFromUser: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = bubbleStyle.color)
    ) {
        // Borde opcional
        if (bubbleStyle.borderWidth > 0.dp && bubbleStyle.borderColor != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawBehind {
                        drawRect(
                            color = bubbleStyle.borderColor!!,
                            style = Stroke(width = bubbleStyle.borderWidth.value * density)
                        )
                    }
            )
        }
        
        // Triángulo decorativo en la esquina
        Canvas(
            modifier = Modifier
                .size(12.dp)
                .align(
                    if (isFromUser) Alignment.TopEnd else Alignment.TopStart
                )
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(0f, size.height)
                close()
            }
            drawPath(
                path = path,
                color = bubbleStyle.color
            )
        }
    }
}

/**
 * Fondo ovalado/elíptico
 */
@Composable
private fun OvalBubbleBackground(
    bubbleStyle: BubbleStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = bubbleStyle.color,
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(horizontal = 24.dp) // Padding extra para la forma ovalada
    ) {
        // Borde opcional
        if (bubbleStyle.borderWidth > 0.dp && bubbleStyle.borderColor != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(percent = 50),
                        border = Stroke(
                            width = bubbleStyle.borderWidth.value,
                            color = bubbleStyle.borderColor
                        )
                    )
            )
        }
    }
}

/**
 * Fondo con forma de nube (ondulaciones)
 */
@Composable
private fun CloudBubbleBackground(
    bubbleStyle: BubbleStyle,
    isFromUser: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            val width = size.width
            val height = size.height
            val cornerRadius = bubbleStyle.cornerRadius.toPx()
            
            // Crear path de nube con ondulaciones
            val path = Path().apply {
                val bumpRadius = cornerRadius * 0.8f
                val numBumps = (width / (bumpRadius * 2)).toInt().coerceAtLeast(3)
                val bumpSpacing = width / numBumps
                
                // Borde superior con ondulaciones
                moveTo(0f, height * 0.3f)
                
                for (i in 0 until numBumps) {
                    val x = i * bumpSpacing
                    quadraticBezierTo(
                        x + bumpSpacing * 0.5f,
                        -bumpRadius * 0.5f,
                        x + bumpSpacing,
                        height * 0.3f
                    )
                }
                
                // Lado derecho
                lineTo(width, height * 0.5f)
                quadraticBezierTo(
                    width + cornerRadius,
                    height * 0.5f,
                    width,
                    height * 0.7f
                )
                
                // Borde inferior con ondulaciones (invertidas)
                for (i in numBumps downTo 0) {
                    val x = i * bumpSpacing
                    quadraticBezierTo(
                        x + bumpSpacing * 0.5f,
                        height + bumpRadius * 0.3f,
                        x,
                        height * 0.7f
                    )
                }
                
                // Lado izquierdo
                lineTo(0f, height * 0.5f)
                quadraticBezierTo(
                    -cornerRadius,
                    height * 0.5f,
                    0f,
                    height * 0.3f
                )
                
                close()
            }
            
            drawPath(path, bubbleStyle.color)
            
            // Borde opcional
            if (bubbleStyle.borderWidth > 0.dp && bubbleStyle.borderColor != null) {
                drawPath(
                    path = path,
                    color = bubbleStyle.borderColor!!,
                    style = Stroke(width = bubbleStyle.borderWidth.toPx())
                )
            }
        }
    }
}

/**
 * Fondo con forma de corazón
 */
@Composable
private fun HeartBubbleBackground(
    bubbleStyle: BubbleStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(vertical = 16.dp) // Padding extra para la forma de corazón
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height / 2f
            
            // Crear path de corazón
            val path = Path().apply {
                moveTo(centerX, height * 0.3f)
                
                // Lóbulo izquierdo
                cubicTo(
                    x1 = centerX * 0.2f,
                    y1 = height * 0.1f,
                    x2 = 0f,
                    y2 = height * 0.4f,
                    x3 = centerX,
                    y3 = height * 0.8f
                )
                
                // Lóbulo derecho
                cubicTo(
                    x1 = width,
                    y1 = height * 0.4f,
                    x2 = width * 0.8f,
                    y1 = height * 0.1f,
                    x3 = centerX,
                    y3 = height * 0.3f
                )
                
                close()
            }
            
            drawPath(path, bubbleStyle.color)
            
            // Borde opcional
            if (bubbleStyle.borderWidth > 0.dp && bubbleStyle.borderColor != null) {
                drawPath(
                    path = path,
                    color = bubbleStyle.borderColor!!,
                    style = Stroke(width = bubbleStyle.borderWidth.toPx())
                )
            }
        }
        
        // Contenido del mensaje centrado
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        ) {
            // El texto se renderiza por separado en CustomChatBubble
        }
    }
}

/**
 * Estilos predefinidos de burbujas
 */
object BubbleStyles {
    // Estilos románticos
    val pinkRounded = BubbleStyle(
        color = Color(0xFFFFB6C1),
        shape = BubbleShape.ROUNDED,
        cornerRadius = 16.dp
    )
    
    val pinkHeart = BubbleStyle(
        color = Color(0xFFFF69B4),
        shape = BubbleShape.HEART,
        cornerRadius = 16.dp
    )
    
    val pinkCloud = BubbleStyle(
        color = Color(0xFFFFC0CB),
        shape = BubbleShape.CLOUD,
        cornerRadius = 12.dp
    )
    
    val yellowRounded = BubbleStyle(
        color = Color(0xFFFFD700),
        shape = BubbleShape.ROUNDED,
        cornerRadius = 16.dp
    )
    
    val purpleOval = BubbleStyle(
        color = Color(0xFFDDA0DD),
        shape = BubbleShape.OVAL,
        cornerRadius = 16.dp
    )
    
    // Estilos con borde
    val pinkRoundedWithBorder = BubbleStyle(
        color = Color(0xFFFFB6C1),
        shape = BubbleShape.ROUNDED,
        cornerRadius = 16.dp,
        borderColor = Color(0xFFFF69B4),
        borderWidth = 2.dp
    )
    
    val redHeart = BubbleStyle(
        color = Color(0xFFFF0000),
        shape = BubbleShape.HEART,
        cornerRadius = 16.dp
    )
}

/**
 * Ejemplo de uso en Preview (requiere Android Studio)
 *
 * @Preview
 * @Composable
 * fun ChatBubbleCustomPreview() {
 *     Column(
 *         modifier = Modifier
 *             .fillMaxSize()
 *             .padding(16.dp)
 *     ) {
 *         Text("Estilos de Burbujas:", fontWeight = FontWeight.Bold)
 *         
 *         Spacer(modifier = Modifier.height(16.dp))
 *         
 *         CustomChatBubble(
 *             message = "¡Te quiero! 💕",
 *             sender = "user",
 *             bubbleStyle = BubbleStyles.pinkRounded
 *         )
 *         
 *         CustomChatBubble(
 *             message = "Yo también te quiero 🐷",
 *             sender = "partner",
 *             bubbleStyle = BubbleStyles.pinkHeart
 *         )
 *         
 *         CustomChatBubble(
 *             message = "¿Vamos al cine?",
 *             sender = "user",
 *             bubbleStyle = BubbleStyles.pinkCloud
 *         )
 *         
 *         CustomChatBubble(
 *             message = "¡Sí! 🎬",
 *             sender = "partner",
 *             bubbleStyle = BubbleStyles.yellowRounded
 *         )
 *         
 *         CustomChatBubble(
 *             message = "Te amo 💖",
 *             sender = "user",
 *             bubbleStyle = BubbleStyles.pinkRoundedWithBorder
 *         )
 *     }
 * }
 */
