/*
 * Copyright 2024 Cerdita App
 *
 * Activity para el Juego de Tarjetas de Amor
 */

package im.vector.app.features.romantic.games

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * LoveCardsGameActivity - Pantalla para el juego de Tarjetas de Amor
 *
 * Muestra un juego interactivo donde los usuarios pueden robar cartas con:
 * - Preguntas románticas
 * - Retos divertidos
 * - Mensajes de amor
 * - Recuerdos especiales
 * - Deseos románticos
 */
class LoveCardsGameActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoveCardsGameScreen(
                onBackClick = { finish() }
            )
        }
    }
}

/**
 * Composable screen para el Juego de Tarjetas de Amor
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveCardsGameScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🎴 Tarjetas de Amor",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5),
                    titleContentColor = Color(0xFFFF6B6B),
                    navigationIconContentColor = Color(0xFFFF6B6B)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LoveCardsGame(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                onCardDrawn = { card ->
                    // Manejar carta robada (analytics, estadísticas, etc.)
                    // Se puede expandir para guardar historial de cartas robadas
                }
            )
        }
    }
}
