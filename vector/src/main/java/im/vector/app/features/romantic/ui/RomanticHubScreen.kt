/*
 * Copyright 2024 Cerdita App
 *
 * Menú Principal de Funcionalidades Románticas
 * Integra todas las features románticas en un solo lugar
 */

package im.vector.app.features.romantic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.vector.app.features.romantic.RelationshipDaysManager
import im.vector.app.features.romantic.games.AffectionCounter
import im.vector.app.features.romantic.games.LoveCardsGame
import im.vector.app.features.romantic.features.LoveNotesManager

/**
 * Sell class para representar todas las características románticas navegables
 */
sealed class RomanticFeature {
    object DaysTogether : RomanticFeature()
    object LoveCards : RomanticFeature()
    object KissCounter : RomanticFeature()
    object HugsCounter : RomanticFeature()
    object LoveNotes : RomanticFeature()
    object RelationshipTree : RomanticFeature()
    object RomanticEffects : RomanticFeature()
    object Settings : RomanticFeature()
}

/**
 * Menú Principal Romántico
 * 
 * ACCESOS RÁPIDOS:
 * 1. Contador de Días
 * 2. Tarjetas de Amor (Juego)
 * 3. Contador de Besos/Abrazos
 * 4. Notas de Amor
 * 5. Árbol de Relación
 * 6. Configuración Romántica
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RomanticHubScreen(
    daysManager: RelationshipDaysManager,
    affectionCounter: AffectionCounter,
    notesManager: LoveNotesManager,
    onNavigateToFeature: (RomanticFeature) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateNoteDialog by remember { mutableStateOf(false) }

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
                            text = daysManager.getFormattedTimeTogether(),
                            fontSize = 14.sp,
                            color = Color(0xFFFF6B6B)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5),
                    titleContentColor = Color(0xFF333333)
                ),
                actions = {
                    IconButton(onClick = { onNavigateToFeature(RomanticFeature.Settings) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Contador de Días
            item {
                RomanticHubCard(
                    title = "📅 Días Juntos",
                    subtitle = daysManager.getFormattedTimeTogether(),
                    icon = Icons.Default.Favorite,
                    gradient = listOf(Color(0xFFFF6B6B), Color(0xFFFFB6C1)),
                    onClick = { onNavigateToFeature(RomanticFeature.DaysTogether) }
                )
            }

            // Tarjetas de Amor
            item {
                RomanticHubCard(
                    title = "🎴 Tarjetas de Amor",
                    subtitle = "Juego de preguntas",
                    icon = Icons.Default.CardGiftcard,
                    gradient = listOf(Color(0xFFFF69B4), Color(0xFFFF1493)),
                    onClick = { onNavigateToFeature(RomanticFeature.LoveCards) }
                )
            }

            // Contador de Besos
            item {
                RomanticHubCard(
                    title = "💋 Contador de Besos",
                    subtitle = "${affectionCounter.getStats().kissesSent} besos dados",
                    icon = Icons.Default.Favorite,
                    gradient = listOf(Color(0xFFFF1493), Color(0xFFDC143C)),
                    onClick = { onNavigateToFeature(RomanticFeature.KissCounter) }
                )
            }

            // Contador de Abrazos
            item {
                RomanticHubCard(
                    title = "🤗 Contador de Abrazos",
                    subtitle = "${affectionCounter.getStats().hugsSent} abrazos dados",
                    icon = Icons.Default.Favorite,
                    gradient = listOf(Color(0xFFFFB347), Color(0xFFFF8C00)),
                    onClick = { onNavigateToFeature(RomanticFeature.HugsCounter) }
                )
            }

            // Notas de Amor
            item {
                val unreadNotes = notesManager.getUnreadNotes().size
                RomanticHubCard(
                    title = "💌 Notas de Amor",
                    subtitle = if (unreadNotes > 0) "$unreadNotes sin leer" else "Sin notas nuevas",
                    icon = Icons.Default.Mail,
                    gradient = listOf(Color(0xFFDDA0DD), Color(0xFFBA55D3)),
                    badge = if (unreadNotes > 0) unreadNotes else null,
                    onClick = { onNavigateToFeature(RomanticFeature.LoveNotes) }
                )
            }

            // Árbol de Relación
            item {
                val treeLevel = RelationshipTree.getTreeLevel(daysManager.getDaysTogether())
                RomanticHubCard(
                    title = "🌳 Árbol de Amor",
                    subtitle = "Nivel: ${treeLevel.name}",
                    icon = Icons.Default.Park,
                    gradient = listOf(Color(0xFF90EE90), Color(0xFF228B22)),
                    onClick = { onNavigateToFeature(RomanticFeature.RelationshipTree) }
                )
            }

            // Efectos Románticos
            item {
                RomanticHubCard(
                    title = "✨ Efectos Románticos",
                    subtitle = "Activa efectos especiales",
                    icon = Icons.Default.AutoAwesome,
                    gradient = listOf(Color(0xFFFFD700), Color(0xFFFFA500)),
                    onClick = { onNavigateToFeature(RomanticFeature.RomanticEffects) }
                )
            }

            // Configuración
            item {
                RomanticHubCard(
                    title = "⚙️ Configuración",
                    subtitle = "Personaliza tu experiencia",
                    icon = Icons.Default.Settings,
                    gradient = listOf(Color(0xFF708090), Color(0xFF778899)),
                    onClick = { onNavigateToFeature(RomanticFeature.Settings) }
                )
            }
        }
    }
    
    if (showCreateNoteDialog) {
        // Dialog para crear nota
    }
}

@Composable
fun RomanticHubCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: Int? = null
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp)),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(colors = gradient)
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    
                    if (badge != null && badge > 0) {
                        Surface(
                            color = Color.Red,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = badge.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                
                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

/**
 * Botón flotante romántico para acceso rápido
 */
@Composable
fun RomanticFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(64.dp),
        containerColor = Color(0xFFFF6B6B),
        shape = RoundedCornerShape(20.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Espacio Romántico",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}
