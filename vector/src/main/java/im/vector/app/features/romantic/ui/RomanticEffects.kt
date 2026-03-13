/*
 * Copyright 2024 Cerdita App
 * 
 * Efectos Visuales Románticos - Jetpack Compose
 * Lluvia de corazones, besos, flores, etc.
 */

package im.vector.app.features.romantic.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Efecto de lluvia de corazones
 * Se activa cuando se detectan palabras románticas
 */
@Composable
fun HeartRainEffect(
    isActive: Boolean,
    onFinished: () -> Unit
) {
    if (!isActive) return
    
    val hearts = remember { 
        List(20) { 
            Heart(
                x = Random.nextFloat(),
                delay = Random.nextLong(2000),
                size = Random.nextInt(20, 50),
                speed = Random.nextFloat() * 2000 + 1000
            ) 
        } 
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        hearts.forEach { heart ->
            AnimatedHeart(
                heart = heart,
                onFinished = onFinished
            )
        }
    }
}

data class Heart(
    val x: Float,
    val delay: Long,
    val size: Int,
    val speed: Long
)

@Composable
fun AnimatedHeart(
    heart: Heart,
    onFinished: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var offsetY by remember { mutableStateOf(-50f) }
    
    LaunchedEffect(heart.delay) {
        delay(heart.delay)
        visible = true
        
        val startTime = System.currentTimeMillis()
        while (offsetY < 800f) {
            val elapsed = System.currentTimeMillis() - startTime
            offsetY = (elapsed.toFloat() / heart.speed) * 800f
            awaitFrame()
        }
        
        visible = false
        onFinished()
    }
    
    if (visible) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color(0xFFFF6B6B).copy(alpha = 0.8f),
            modifier = Modifier
                .size(heart.size.dp)
                .translate(top = offsetY)
        )
    }
}

/**
 * Efecto de beso volador
 */
@Composable
fun FlyingKissEffect(
    isActive: Boolean,
    fromLeft: Boolean = true,
    onFinished: () -> Unit
) {
    if (!isActive) return
    
    var offsetX by remember { mutableStateOf(if (fromLeft) -100f else 100f) }
    var offsetY by remember { mutableStateOf(50f) }
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
        val startTime = System.currentTimeMillis()
        
        while (offsetX > -100f && offsetX < 500f) {
            val elapsed = System.currentTimeMillis() - startTime
            val progress = elapsed.toFloat() / 2000f
            
            offsetX = if (fromLeft) {
                -100f + (progress * 600f)
            } else {
                500f - (progress * 600f)
            }
            
            // Trayectoria parabólica
            offsetY = 50f + sin(progress * Math.PI).toFloat() * 100f
            
            if (progress >= 1f) break
            
            awaitFrame()
        }
        
        visible = false
        onFinished()
    }
    
    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .translate(
                    left = offsetX,
                    top = offsetY
                )
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFFF1493),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

/**
 * Efecto de flores
 */
@Composable
fun FlowerBloomEffect(
    isActive: Boolean,
    onFinished: () -> Unit
) {
    if (!isActive) return
    
    var scale by remember { mutableStateOf(0f) }
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
        val startTime = System.currentTimeMillis()
        
        while (scale < 1f) {
            val elapsed = System.currentTimeMillis() - startTime
            scale = (elapsed.toFloat() / 1000f).coerceIn(0f, 1f)
            awaitFrame()
        }
        
        delay(2000)
        visible = false
        onFinished()
    }
    
    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Favorite, // Reemplazar con icono de flor
                contentDescription = null,
                tint = Color(0xFFFF69B4),
                modifier = Modifier
                    .size(100.dp)
                    .translate(
                        left = 100f,
                        top = 100f
                    )
            )
        }
    }
}
