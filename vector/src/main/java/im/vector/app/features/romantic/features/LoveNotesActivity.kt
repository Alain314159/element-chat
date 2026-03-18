/*
 * Copyright 2024 Cerdita App
 *
 * Activity para Notas de Amor con Desbloqueo por Ubicación y Notas Colaborativas
 */

package im.vector.app.features.romantic.features

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
import im.vector.app.features.romantic.data.database.*

/**
 * LoveNotesActivity - Screen for managing love notes with location triggers and collaborative notes
 */
class LoveNotesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoveNotesScreen(
                onBackClick = { finish() }
            )
        }
    }
}

/**
 * Composable screen for Love Notes
 */
@Composable
fun LoveNotesScreen(
    onBackClick: () -> Unit
) {
    var showCreateNoteDialog by remember { mutableStateOf(false) }
    var showLocationTriggerDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Mis Notas", "Colaborativas", "Por Ubicacion")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "💌 Notas de Amor",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showLocationTriggerDialog = true }) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Triggers de ubicación")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateNoteDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear nota")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Pestañas
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Contenido de pestañas
            when (selectedTab) {
                0 -> MyNotesTab()
                1 -> CollaborativeNotesTab()
                2 -> LocationNotesTab()
            }
        }
    }

    // Diálogos
    if (showCreateNoteDialog) {
        CreateNoteDialog(
            onConfirm = { title, content, noteType ->
                // Aquí se crearía la nota con el repository
                showCreateNoteDialog = false
            },
            onDismiss = { showCreateNoteDialog = false }
        )
    }

    if (showLocationTriggerDialog) {
        LocationTriggersDialog(
            onDismiss = { showLocationTriggerDialog = false }
        )
    }
}

/**
 * Pestaña de notas personales
 */
@Composable
fun MyNotesTab() {
    val notes = remember { emptyList<LoveNoteEntity>() } // Se cargaría del repository

    if (notes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No hay notas aún",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notes) { note ->
                NoteCard(note = note)
            }
        }
    }
}

/**
 * Pestaña de notas colaborativas
 */
@Composable
fun CollaborativeNotesTab() {
    val collaborativeNotes = remember { emptyList<CollaborativeNoteEntity>() }

    if (collaborativeNotes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "📝 Notas Colaborativas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Escriban juntos en la misma nota",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(collaborativeNotes) { note ->
                CollaborativeNoteCard(note = note)
            }
        }
    }
}

/**
 * Pestaña de notas por ubicación
 */
@Composable
fun LocationNotesTab() {
    val locationTriggers = remember { emptyList<LocationTriggerEntity>() }

    if (locationTriggers.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Notas por Ubicación",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Las notas se desbloquean al llegar a lugares especiales",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(locationTriggers) { trigger ->
                LocationTriggerCard(trigger = trigger)
            }
        }
    }
}

/**
 * Tarjeta de nota personal
 */
@Composable
fun NoteCard(
    note: LoveNoteEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
                    text = note.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (note.isRead) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Leída",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content,
                fontSize = 14.sp,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📅 ${formatDate(note.createdAt)}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (note.attachmentCount > 0) {
                    Text(
                        text = "📎 ${note.attachmentCount} adjuntos",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta de nota colaborativa
 */
@Composable
fun CollaborativeNoteCard(
    note: CollaborativeNoteEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (note.isLocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueada",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "👥 Colaborativa - ${formatDate(note.lastModified)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Tarjeta de trigger de ubicación
 */
@Composable
fun LocationTriggerCard(
    trigger: LocationTriggerEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = trigger.locationName ?: "Ubicación sin nombre",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📍 Radio: ${trigger.radiusMeters}m",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (trigger.isTriggered) {
                AssistChip(
                    onClick = { },
                    label = { Text("Desbloqueada", fontSize = 10.sp) }
                )
            } else {
                AssistChip(
                    onClick = { },
                    label = { Text("Pendiente", fontSize = 10.sp) }
                )
            }
        }
    }
}

/**
 * Diálogo para crear nota
 */
@Composable
fun CreateNoteDialog(
    onConfirm: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var noteType by remember { mutableStateOf("MESSAGE") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Nota de Amor") },
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
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Contenido") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📍 Puedes agregar ubicación para desbloqueo después",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, content, noteType) },
                enabled = title.isNotBlank() && content.isNotBlank()
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
 * Diálogo de triggers de ubicación
 */
@Composable
fun LocationTriggersDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("📍 Triggers de Ubicación") },
        text = {
            Column {
                Text(
                    text = "Configura ubicaciones para desbloquear notas automáticamente cuando tu pareja llegue a lugares especiales.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ejemplos:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Su restaurante favorito",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• El parque donde se conocieron",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Su casa",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Entendido")
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
