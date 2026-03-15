/*
 * Copyright 2024 Cerdita App
 * 
 * Notas de Amor - Mensajes secretos para tu pareja
 * Permite dejar notas sorpresa que se desbloquean en momentos especiales
 */

package im.vector.app.features.romantic.features

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Notas de Amor
 * 
 * FUNCIONALIDAD:
 * - Crear notas románticas
 * - Programar entrega en fechas especiales
 * - Notas sorpresa aleatorias
 * - Notas con contraseña/pista
 * - Adjuntar fotos, audios, videos
 */

data class LoveNote(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val scheduledFor: LocalDate? = null,
    val isLocked: Boolean = false,
    val lockHint: String? = null,
    val noteType: NoteType = NoteType.NORMAL,
    val isRead: Boolean = false,
    val attachmentCount: Int = 0
)

enum class NoteType {
    NORMAL,         // Nota normal
    SURPRISE,       // Sorpresa aleatoria
    ANNIVERSARY,    // Aniversario
    BIRTHDAY,       // Cumpleaños
    ENCOURAGEMENT,  // Ánimo
    GOOD_MORNING,   // Buenos días
    GOOD_NIGHT,     // Buenas noches
    SORRY,          // Disculpa
    THANK_YOU       // Agradecimiento
}

class LoveNotesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "cerdita_love_notes"
        private const val KEY_NOTES = "love_notes_json"
    }

    fun getAllNotes(): List<LoveNote> {
        val notesJson = prefs.getString(KEY_NOTES, null) ?: return getSampleNotes()
        val type = object : TypeToken<List<LoveNote>>() {}.type
        return try {
            gson.fromJson(notesJson, type) ?: getSampleNotes()
        } catch (e: Exception) {
            getSampleNotes()
        }
    }

    fun saveNote(note: LoveNote) {
        val notes = getAllNotes().toMutableList()
        val existingIndex = notes.indexOfFirst { it.id == note.id }
        if (existingIndex >= 0) {
            notes[existingIndex] = note
        } else {
            notes.add(note)
        }
        val notesJson = gson.toJson(notes)
        prefs.edit().putString(KEY_NOTES, notesJson).apply()
    }

    fun deleteNote(noteId: String) {
        val notes = getAllNotes().filter { it.id != noteId }
        val notesJson = gson.toJson(notes)
        prefs.edit().putString(KEY_NOTES, notesJson).apply()
    }

    fun getUnreadNotes(): List<LoveNote> {
        return getAllNotes().filter { !it.isRead }
    }

    fun getNotesForToday(): List<LoveNote> {
        val today = LocalDate.now()
        return getAllNotes().filter { it.scheduledFor == today }
    }
    
    private fun getSampleNotes(): List<LoveNote> {
        return listOf(
            LoveNote(
                id = "1",
                title = "Buenos días mi amor ☀️",
                content = "Espero que tengas un día tan hermoso como tú...",
                noteType = NoteType.GOOD_MORNING,
                isRead = false
            ),
            LoveNote(
                id = "2",
                title = "Te extraño 💕",
                content = "Cada momento sin ti es un segundo eterno...",
                noteType = NoteType.NORMAL,
                isRead = true
            ),
            LoveNote(
                id = "3",
                title = "¡Feliz Aniversario! 🎉",
                content = "Un año más juntos y te amo más que nunca...",
                noteType = NoteType.ANNIVERSARY,
                scheduledFor = LocalDate.now().plusDays(30),
                isLocked = true,
                lockHint = "La fecha de nuestra primera cita"
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveNotesScreen(
    notesManager: LoveNotesManager,
    onNoteClick: (LoveNote) -> Unit,
    onCreateNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notes by remember { mutableStateOf(notesManager.getAllNotes()) }
    val unreadCount = notes.count { !it.isRead }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "💌 Notas de Amor",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B6B)
                )
                
                if (unreadCount > 0) {
                    Text(
                        text = "$unreadCount notas sin leer",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            
            FloatingActionButton(
                onClick = onCreateNote,
                containerColor = Color(0xFFFF6B6B),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear nota",
                    tint = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Pestañas de tipos
        ScrollableTabRow(
            selectedTabIndex = 0,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            divider = {}
        ) {
            listOf(
                "Todas" to Icons.Default.Inbox,
                "Sin Leer" to Icons.Default.Email,
                "Programadas" to Icons.Default.Schedule,
                "Especiales" to Icons.Default.Favorite
            ).forEach { (label, icon) ->
                Tab(
                    selected = false,
                    onClick = { },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(label)
                        }
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Grid de notas
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay notas aún",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "¡Crea tu primera nota de amor!",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes) { note ->
                    LoveNoteCard(
                        note = note,
                        onClick = { onNoteClick(note) }
                    )
                }
            }
        }
    }
}

@Composable
fun LoveNoteCard(
    note: LoveNote,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = when (note.noteType) {
        NoteType.GOOD_MORNING -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFFD700), Color(0xFFFFA500))
        )
        NoteType.GOOD_NIGHT -> Brush.verticalGradient(
            colors = listOf(Color(0xFF191970), Color(0xFF4B0082))
        )
        NoteType.ANNIVERSARY -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF1493))
        )
        NoteType.BIRTHDAY -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFF69B4), Color(0xFFFF1493))
        )
        NoteType.ENCOURAGEMENT -> Brush.verticalGradient(
            colors = listOf(Color(0xFF90EE90), Color(0xFF32CD32))
        )
        NoteType.SORRY -> Brush.verticalGradient(
            colors = listOf(Color(0xFFD3D3D3), Color(0xFFA9A9A9))
        )
        NoteType.THANK_YOU -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFFB6C1), Color(0xFFFF69B4))
        )
        else -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFFB6C1), Color(0xFFFF6B6B))
        )
    }
    
    Card(
        modifier = modifier
            .aspectRatio(0.8f)
            .clip(RoundedCornerShape(16.dp)),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.noteType.icon,
                        fontSize = 24.sp
                    )
                    
                    if (note.isLocked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Bloqueada",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    if (!note.isRead) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(24.dp)
                        ) { }
                    }
                }
                
                Column {
                    Text(
                        text = note.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = note.content.take(50) + if (note.content.length > 50) "..." else "",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 3
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.createdAt.format(DateTimeFormatter.ofPattern("dd/MM")),
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    
                    if (note.attachmentCount > 0) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

fun NoteType.icon(): String = when (this) {
    NoteType.GOOD_MORNING -> "☀️"
    NoteType.GOOD_NIGHT -> "🌙"
    NoteType.ANNIVERSARY -> "🎉"
    NoteType.BIRTHDAY -> "🎂"
    NoteType.ENCOURAGEMENT -> "💪"
    NoteType.SORRY -> "🙏"
    NoteType.THANK_YOU -> "🙏"
    NoteType.SURPRISE -> "🎁"
    NoteType.NORMAL -> "💕"
}

@Composable
fun CreateLoveNoteDialog(
    onDismiss: () -> Unit,
    onSave: (LoveNote) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(NoteType.NORMAL) }
    var scheduledDate by remember { mutableStateOf<LocalDate?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("💌 Crear Nota de Amor") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Mensaje") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )
                
                // Selector de tipo
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = selectedType.name,
                        onValueChange = { },
                        label = { Text("Tipo de nota") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val note = LoveNote(
                        id = System.currentTimeMillis().toString(),
                        title = title,
                        content = content,
                        noteType = selectedType,
                        scheduledFor = scheduledDate
                    )
                    onSave(note)
                    onDismiss()
                },
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Guardar Nota")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
