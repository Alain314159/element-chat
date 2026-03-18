/*
 * Copyright 2024 Cerdita App
 *
 * Activity para el Árbol de la Relación con Hitos Personalizables
 */

package im.vector.app.features.romantic.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.vector.app.features.romantic.data.database.CustomMilestoneEntity

/**
 * RelationshipTreeActivity - Screen for relationship tree with customizable milestones
 */
class RelationshipTreeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RelationshipTreeScreen(
                onBackClick = { finish() }
            )
        }
    }
}

/**
 * Composable screen for Relationship Tree
 */
@Composable
fun RelationshipTreeScreen(
    onBackClick: () -> Unit
) {
    var showAddMilestoneDialog by remember { mutableStateOf(false) }
    val customMilestones = remember { emptyList<CustomMilestoneEntity>() } // Se cargaría del repository

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🌳 Árbol de la Relación",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddMilestoneDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar hito")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddMilestoneDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar hito personalizado")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Vista del árbol (placeholder)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🌳 Tu Árbol del Amor",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cada hito hace crecer tu árbol",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Divider()

            // Lista de hitos personalizados
            Text(
                text = "📍 Hitos Personalizados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            if (customMilestones.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay hitos personalizados aún",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(customMilestones) { milestone ->
                        CustomMilestoneCard(milestone = milestone)
                    }
                }
            }
        }
    }

    // Diálogo para agregar hito personalizado
    if (showAddMilestoneDialog) {
        AddCustomMilestoneDialog(
            onConfirm = { title, description, date ->
                // Aquí se crearía el hito con el repository
                showAddMilestoneDialog = false
            },
            onDismiss = { showAddMilestoneDialog = false }
        )
    }
}

/**
 * Tarjeta de hito personalizado
 */
@Composable
fun CustomMilestoneCard(
    milestone: CustomMilestoneEntity
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = milestone.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (!milestone.isSynced) {
                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = "No sincronizado",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = milestone.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📅 ${formatDate(milestone.date)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (milestone.photoUri != null) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "Con foto",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (milestone.audioNoteUri != null) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Con audio",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            // Tags
            if (milestone.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    milestone.tags.split("|").forEach { tag ->
                        AssistChip(
                            onClick = { },
                            label = { Text("#$tag", fontSize = 10.sp) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Diálogo para agregar hito personalizado
 */
@Composable
fun AddCustomMilestoneDialog(
    onConfirm: (String, String, Long) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Hito Personalizado") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Aquí iría un DatePicker para seleccionar la fecha
                Text(
                    text = "Fecha: ${formatDate(selectedDate)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📸 Puedes agregar foto y audio después",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, description, selectedDate) },
                enabled = title.isNotBlank()
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

/**
 * Formatea una fecha timestamp a string legible
 */
fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}
