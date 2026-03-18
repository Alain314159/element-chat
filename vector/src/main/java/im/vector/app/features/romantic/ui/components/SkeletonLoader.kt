/*
 * Copyright 2024 Cerdita App
 *
 * Skeleton Loaders - Componentes Jetpack Compose
 * Loaders con animación shimmer para estados de carga
 * Colores rosa/amarillo del tema romántico
 */

package im.vector.app.features.romantic.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Colores del tema romántico para skeleton loaders
 */
object SkeletonColors {
    val pinkLight = Color(0xFFFFE4E1)      // Rosa muy claro - base
    val pinkMedium = Color(0xFFFFB6C1)     // Rosa pastel - brillo
    val pinkDark = Color(0xFFFF69B4)       // Rosa intenso - acento
    val yellowLight = Color(0xFFFFFACD)    // Amarillo claro - base alternativa
    val yellowMedium = Color(0xFFFFD700)   // Dorado - brillo alternativo
    val yellowDark = Color(0xFFFFA500)     // Naranja - acento alternativo
}

/**
 * SkeletonBox - Contenedor básico con efecto shimmer
 *
 * @param modifier Modificador para personalizar el layout
 * @param width Ancho del skeleton (null para wrap content)
 * @param height Alto del skeleton (null para wrap content)
 * @param cornerRadius Radio de las esquinas
 * @param usePinkTheme true para tema rosa, false para amarillo
 */
@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp? = null,
    cornerRadius: Dp = 8.dp,
    usePinkTheme: Boolean = true
) {
    val shimmerColors = if (usePinkTheme) {
        listOf(
            SkeletonColors.pinkLight,
            SkeletonColors.pinkMedium,
            SkeletonColors.pinkLight
        )
    } else {
        listOf(
            SkeletonColors.yellowLight,
            SkeletonColors.yellowMedium,
            SkeletonColors.yellowLight
        )
    }

    val transition = rememberInfiniteTransition(label = "skeleton_shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnim - 500f, y = 0f),
        end = Offset(x = translateAnim + 500f, y = 0f)
    )

    Box(
        modifier = modifier
            .then(
                if (width != null && height != null) {
                    Modifier.size(width, height)
                } else if (width != null) {
                    Modifier.width(width)
                } else if (height != null) {
                    Modifier.height(height)
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

/**
 * SkeletonText - Skeleton para líneas de texto
 *
 * @param modifier Modificador para personalizar el layout
 * @param lines Número de líneas de texto a simular
 * @param lineSpacing Espacio entre líneas
 * @param maxLength Ancho máximo de la última línea (null para igual que las demás)
 * @param usePinkTheme true para tema rosa, false para amarillo
 */
@Composable
fun SkeletonText(
    modifier: Modifier = Modifier,
    lines: Int = 1,
    lineSpacing: Dp = 8.dp,
    maxLength: Float? = null,
    usePinkTheme: Boolean = true
) {
    Column(modifier = modifier) {
        for (i in 0 until lines) {
            val isLastLine = i == lines - 1
            val lineModifier = if (isLastLine && maxLength != null) {
                Modifier.fillMaxWidth(maxLength)
            } else {
                Modifier.fillMaxWidth()
            }

            SkeletonBox(
                modifier = lineModifier
                    .height(16.dp),
                cornerRadius = 4.dp,
                usePinkTheme = usePinkTheme
            )

            if (i < lines - 1) {
                Spacer(modifier = Modifier.height(lineSpacing))
            }
        }
    }
}

/**
 * SkeletonCircle - Skeleton para avatares circulares
 *
 * @param modifier Modificador para personalizar el layout
 * @param size Tamaño del círculo
 * @param usePinkTheme true para tema rosa, false para amarillo
 */
@Composable
fun SkeletonCircle(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    usePinkTheme: Boolean = true
) {
    val shimmerColors = if (usePinkTheme) {
        listOf(
            SkeletonColors.pinkLight,
            SkeletonColors.pinkMedium,
            SkeletonColors.pinkLight
        )
    } else {
        listOf(
            SkeletonColors.yellowLight,
            SkeletonColors.yellowMedium,
            SkeletonColors.yellowLight
        )
    }

    val transition = rememberInfiniteTransition(label = "skeleton_circle_shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "circle_shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnim - 500f, y = 0f),
        end = Offset(x = translateAnim + 500f, y = 0f)
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(brush)
    )
}

/**
 * SkeletonChatBubble - Skeleton para mensajes de chat
 *
 * @param modifier Modificador para personalizar el layout
 * @param isFromUser true si el mensaje es del usuario (alineado a la derecha)
 * @param showAvatar true para mostrar avatar junto al mensaje
 * @param bubbleWidth Ancho máximo del bubble (0..1f)
 * @param usePinkTheme true para tema rosa, false para amarillo
 */
@Composable
fun SkeletonChatBubble(
    modifier: Modifier = Modifier,
    isFromUser: Boolean = true,
    showAvatar: Boolean = false,
    bubbleWidth: Float = 0.7f,
    usePinkTheme: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isFromUser && showAvatar) {
            SkeletonCircle(
                size = 40.dp,
                usePinkTheme = usePinkTheme
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isFromUser) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(bubbleWidth)
        ) {
            // Línea 1 - más larga
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(14.dp),
                cornerRadius = 8.dp,
                usePinkTheme = usePinkTheme
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Línea 2 - más corta
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp),
                cornerRadius = 8.dp,
                usePinkTheme = usePinkTheme
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Línea 3 - muy corta (simula final del mensaje)
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(14.dp),
                cornerRadius = 8.dp,
                usePinkTheme = usePinkTheme
            )
        }

        if (isFromUser && showAvatar) {
            Spacer(modifier = Modifier.width(8.dp))
            SkeletonCircle(
                size = 40.dp,
                usePinkTheme = usePinkTheme
            )
        }
    }
}

/**
 * SkeletonList - Skeleton para listas de elementos
 *
 * @param modifier Modificador para personalizar el layout
 * @param itemCount Número de elementos en la lista
 * @param showAvatar true para mostrar avatar en cada elemento
 * @param showMultipleLines true para mostrar múltiples líneas de texto
 * @param spacing Espacio entre elementos
 * @param usePinkTheme true para tema rosa, false para amarillo
 */
@Composable
fun SkeletonList(
    modifier: Modifier = Modifier,
    itemCount: Int = 5,
    showAvatar: Boolean = true,
    showMultipleLines: Boolean = true,
    spacing: Dp = 12.dp,
    usePinkTheme: Boolean = true
) {
    Column(modifier = modifier) {
        for (i in 0 until itemCount) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.top / 2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showAvatar) {
                    SkeletonCircle(
                        size = 48.dp,
                        usePinkTheme = usePinkTheme
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Línea principal
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(16.dp),
                        cornerRadius = 4.dp,
                        usePinkTheme = usePinkTheme
                    )

                    if (showMultipleLines) {
                        Spacer(modifier = Modifier.height(6.dp))

                        // Línea secundaria
                        SkeletonBox(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(14.dp),
                            cornerRadius = 4.dp,
                            usePinkTheme = usePinkTheme
                        )
                    }
                }

                // Icono o timestamp
                Spacer(modifier = Modifier.width(8.dp))
                SkeletonBox(
                    modifier = Modifier
                        .width(40.dp)
                        .height(14.dp),
                    cornerRadius = 4.dp,
                    usePinkTheme = usePinkTheme
                )
            }

            if (i < itemCount - 1) {
                Spacer(modifier = Modifier.height(spacing))
            }
        }
    }
}

/**
 * SkeletonGrid - Skeleton para grillas de elementos
 *
 * @param modifier Modificador para personalizar el layout
 * @param itemCount Número total de elementos
 * @param columns Número de columnas
 * @param itemHeight Alto de cada elemento
 * @param spacing Espacio entre elementos
 * @param usePinkTheme true para tema rosa, false para amarillo
 */
@Composable
fun SkeletonGrid(
    modifier: Modifier = Modifier,
    itemCount: Int = 6,
    columns: Int = 2,
    itemHeight: Dp = 100.dp,
    spacing: Dp = 12.dp,
    usePinkTheme: Boolean = true
) {
    val rows = (itemCount + columns - 1) / columns

    Column(modifier = modifier) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                for (col in 0 until columns) {
                    val index = row * columns + col
                    if (index < itemCount) {
                        SkeletonBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(itemHeight),
                            cornerRadius = 12.dp,
                            usePinkTheme = usePinkTheme
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            if (row < rows - 1) {
                Spacer(modifier = Modifier.height(spacing))
            }
        }
    }
}

/**
 * SkeletonCard - Skeleton para tarjetas de contenido
 *
 * @param modifier Modificador para personalizar el layout
 * @param showImage true para mostrar placeholder de imagen
 * @param showTitle true para mostrar título
 * @param showDescription true para mostrar descripción
 * @param showFooter true para mostrar footer
 * @param usePinkTheme true para tema rosa, false para amarillo
 */
@Composable
fun SkeletonCard(
    modifier: Modifier = Modifier,
    showImage: Boolean = true,
    showTitle: Boolean = true,
    showDescription: Boolean = true,
    showFooter: Boolean = false,
    usePinkTheme: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (showImage) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                cornerRadius = 12.dp,
                usePinkTheme = usePinkTheme
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (showTitle) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp),
                cornerRadius = 6.dp,
                usePinkTheme = usePinkTheme
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (showDescription) {
            SkeletonText(
                modifier = Modifier.fillMaxWidth(),
                lines = 2,
                lineSpacing = 6.dp,
                usePinkTheme = usePinkTheme
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (showFooter) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp),
                    cornerRadius = 4.dp,
                    usePinkTheme = usePinkTheme
                )

                SkeletonBox(
                    modifier = Modifier
                        .width(60.dp)
                        .height(12.dp),
                    cornerRadius = 4.dp,
                    usePinkTheme = usePinkTheme
                )
            }
        }
    }
}

/**
 * Ejemplo de uso en Preview (requiere Android Studio)
 *
 * @Preview
 * @Composable
 * fun SkeletonLoaderPreview() {
 *     Column(
 *         modifier = Modifier
 *             .fillMaxSize()
 *             .padding(16.dp)
 *     ) {
 *         Text("SkeletonText:", fontWeight = FontWeight.Bold)
 *         SkeletonText(lines = 3)
 *
 *         Spacer(modifier = Modifier.height(24.dp))
 *
 *         Text("SkeletonChatBubble:", fontWeight = FontWeight.Bold)
 *         SkeletonChatBubble(isFromUser = true)
 *         SkeletonChatBubble(isFromUser = false, showAvatar = true)
 *
 *         Spacer(modifier = Modifier.height(24.dp))
 *
 *         Text("SkeletonList:", fontWeight = FontWeight.Bold)
 *         SkeletonList(itemCount = 3)
 *
 *         Spacer(modifier = Modifier.height(24.dp))
 *
 *         Text("SkeletonGrid:", fontWeight = FontWeight.Bold)
 *         SkeletonGrid(itemCount = 4, columns = 2)
 *     }
 * }
 */
