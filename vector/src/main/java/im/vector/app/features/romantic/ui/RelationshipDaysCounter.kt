/*
 * Copyright 2024 Cerdita App
 * 
 * Componente Contador de Días Juntos - Jetpack Compose
 * Muestra el tiempo que llevan como pareja en el chat
 */

package im.vector.app.features.romantic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import im.vector.app.features.romantic.RelationshipDaysManager
import java.time.LocalDate

/**
 * Widget del Contador de Días Juntos
 * 
 * Se muestra en la parte superior del chat o en el perfil de pareja
 */
@Composable
fun RelationshipDaysCounter(
    daysManager: RelationshipDaysManager,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var daysText by remember { mutableStateOf(daysManager.getFormattedTimeTogether()) }
    var isAnniversary by remember { mutableStateOf(daysManager.isAnniversary()) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F5) // Rosa lavanda
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFFF6B6B),
                    modifier = Modifier.size(24.dp)
                )
                
                Text(
                    text = daysText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B6B)
                )
                
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFFF6B6B),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            if (isAnniversary) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFD700)), // Dorado
                    color = Color.Transparent
                ) {
                    Text(
                        text = "🎉 ${daysManager.getAnniversaryMessage()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

/**
 * Widget compacto para mostrar en el header del chat
 */
@Composable
fun CompactRelationshipDaysCounter(
    daysManager: RelationshipDaysManager,
    modifier: Modifier = Modifier
) {
    var daysText by remember { mutableStateOf(daysManager.getFormattedTimeTogether()) }
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFF0F5)),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(16.dp)
            )
            
            Text(
                text = daysText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFF6B6B)
            )
        }
    }
}

/**
 * Dialog para configurar la fecha de inicio de la relación
 */
@Composable
fun SetStartDateDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("¿Cuándo comenzaron su relación? 💕")
        },
        text = {
            Column {
                Text("Selecciona la fecha especial cuando se hicieron pareja")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB6C1)
                    )
                ) {
                    Text("Seleccionar fecha")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fecha seleccionada: ${selectedDate.toString()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDateSelected(selectedDate)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B)
                )
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
