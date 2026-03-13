/*
 * Copyright 2024 Cerdita App
 * 
 * Panel de Funcionalidades Románticas
 * Se integra en la pantalla de chat para acceso rápido
 */

package im.vector.app.features.romantic.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.vector.app.features.romantic.RelationshipDaysManager
import im.vector.app.features.romantic.HugType
import im.vector.app.features.romantic.HugMessages

/**
 * Panel de Funcionalidades Románticas
 * 
 * Se muestra como botón flotante expandible en el chat
 * Contiene:
 * - Contador de días juntos (compacto)
 * - Botón de abrazo rápido
 * - Acceso a configuración romántica
 */
@Composable
fun RomanticFeaturesPanel(
    daysManager: RelationshipDaysManager,
    onHugSent: (HugType) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onToggleExpand: () -> Unit
) {
    Column(
        modifier = modifier
            .width(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFF0F5)) // Rosa lavanda
            .padding(12.dp)
    ) {
        // Header con toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💕 Funcionalidades Románticas",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            )
            
            IconButton(
                onClick = onToggleExpand,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Contraer" else "Expandir",
                    tint = Color(0xFFFF6B6B),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Contador de días
            CompactRelationshipDaysCounter(
                daysManager = daysManager,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botones de abrazos
            Text(
                text = "Enviar abrazo:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HugType.values().forEach { hugType ->
                    HugTypeButton(
                        hugType = hugType,
                        onClick = {
                            onHugSent(hugType)
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botón de settings
            Button(
                onClick = onSettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB6C1)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Configuración Romántica",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Botón para cada tipo de abrazo
 */
@Composable
fun HugTypeButton(
    hugType: HugType,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalIconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = Color(0xFFFFB6C1)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = when (hugType) {
                HugType.NORMAL -> "Normal"
                HugType.LONG -> "Largo"
                HugType.TIGHT -> "Fuerte"
                HugType.SPIN -> "Giro"
            },
            fontSize = 10.sp,
            color = Color.DarkGray
        )
    }
}

/**
 * Estado del panel romántico
 */
class RomanticPanelState {
    var isExpanded by mutableStateOf(false)
        private set
    
    fun toggle() {
        isExpanded = !isExpanded
    }
    
    fun expand() {
        isExpanded = true
    }
    
    fun collapse() {
        isExpanded = false
    }
}

/**
 * Crea el estado del panel romántico
 */
@Composable
fun rememberRomanticPanelState(): RomanticPanelState {
    return remember { RomanticPanelState() }
}
