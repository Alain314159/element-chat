/*
 * Copyright (c) 2026 Element Chat Romántico
 *
 * Hub principal de funciones románticas - Versión simplificada
 */

package im.vector.app.features.romantic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Hub principal de funciones románticas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RomanticHubScreen(
    onNavigateToDays: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToGifts: () -> Unit,
    onNavigateToChallenges: () -> Unit,
    onNavigateToTimeCapsules: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAlbum: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "💕 Nuestro Espacio Romántico",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Celebra vuestro amor",
                            fontSize = 14.sp,
                            color = Color(0xFFFF6B6B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5),
                    titleContentColor = Color(0xFF333333),
                    navigationIconContentColor = Color(0xFFFF6B6B)
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFFFF0F5))
                .padding(16.dp)
        ) {
            // Días Juntos
            item {
                RomanticFeatureCard(
                    title = "📅 Días Juntos",
                    description = "Tiempo juntos",
                    onClick = onNavigateToDays
                )
            }

            // Notas de Amor
            item {
                RomanticFeatureCard(
                    title = "💌 Notas de Amor",
                    description = "Mensajes románticos",
                    onClick = onNavigateToNotes
                )
            }

            // Regalos
            item {
                RomanticFeatureCard(
                    title = "🎁 Regalos",
                    description = "Detalles especiales",
                    onClick = onNavigateToGifts
                )
            }

            // Desafíos
            item {
                RomanticFeatureCard(
                    title = "🎯 Desafíos",
                    description = "Juegos en pareja",
                    onClick = onNavigateToChallenges
                )
            }

            // Cápsulas del Tiempo
            item {
                RomanticFeatureCard(
                    title = "⏳ Cápsulas",
                    description = "Mensajes futuros",
                    onClick = onNavigateToTimeCapsules
                )
            }

            // Álbum
            item {
                RomanticFeatureCard(
                    title = "📷 Álbum",
                    description = "Vuestros recuerdos",
                    onClick = { onNavigateToAlbum("") }
                )
            }

            // Configuración
            item {
                RomanticFeatureCard(
                    title = "⚙️ Ajustes",
                    description = "Configuración",
                    onClick = onNavigateToSettings
                )
            }
        }
    }
}

/**
 * Tarjeta de función romántica
 */
@Composable
fun RomanticFeatureCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 32.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = title.split(" ").firstOrNull() ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
