/*
 * Copyright (c) 2024 Cerlita. All rights reserved.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package im.vector.app.features.romantic.milestones

import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.*
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import im.vector.app.features.romantic.data.database.RomanticDatabase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Categorías de primeras veces
 */
enum class FirstTimeCategory(val displayName: String, val icon: String) {
    FIRST_MESSAGE("Primer Mensaje", "💬"),
    FIRST_DATE("Primera Cita", "🍽️"),
    FIRST_KISS("Primer Beso", "💋"),
    FIRST_HUG("Primer Abrazo", "🤗"),
    FIRST_TRIP("Primer Viaje", "✈️"),
    FIRST_GIFT("Primer Regalo", "🎁"),
    FIRST_VIDEO_CALL("Primera Videollamada", "📹"),
    FIRST_MEETING_PARENTS("Conocer Padres", "👨‍👩‍👧"),
    FIRST_ANNIVERSARY("Primer Aniversario", "🎉"),
    CUSTOM("Personalizado", "⭐")
}

/**
 * Entidad Room para registrar primeras veces
 */
@Entity(tableName = "first_times_milestones")
data class FirstTimeMilestone(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val date: LocalDateTime,
    val category: FirstTimeCategory,
    val photoUris: String, // Lista serializada como JSON
    val audioNoteUri: String?,
    val locationName: String?,
    val locationLatitude: Double?,
    val locationLongitude: Double?,
    val tags: String, // Lista serializada como JSON
    val isPrivate: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val anniversaryReminders: Boolean = true
)

/**
 * DAO para primeras veces
 */
@Dao
interface FirstTimesDao {
    @Query("SELECT * FROM first_times_milestones ORDER BY date ASC")
    fun getAllMilestones(): Flow<List<FirstTimeMilestone>>

    @Query("SELECT * FROM first_times_milestones WHERE category = :category ORDER BY date ASC")
    fun getMilestonesByCategory(category: FirstTimeCategory): Flow<List<FirstTimeMilestone>>

    @Query("SELECT * FROM first_times_milestones WHERE id = :id")
    suspend fun getMilestoneById(id: String): FirstTimeMilestone?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: FirstTimeMilestone)

    @Update
    suspend fun updateMilestone(milestone: FirstTimeMilestone)

    @Delete
    suspend fun deleteMilestone(milestone: FirstTimeMilestone)

    @Query("SELECT * FROM first_times_milestones WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getMilestonesBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<FirstTimeMilestone>>

    @Query("SELECT COUNT(*) FROM first_times_milestones")
    fun getTotalMilestonesCount(): Flow<Int>
}

/**
 * Registro de primeras veces
 */
@ActivityScoped
class FirstTimesRegistry @Inject constructor(
    private val database: RomanticDatabase,
    @ApplicationContext private val context: Context
) {
    private val dao: FirstTimesDao = database.firstTimesDao()

    val allMilestones: Flow<List<FirstTimeMilestone>> = dao.getAllMilestones()
    val totalMilestonesCount: Flow<Int> = dao.getTotalMilestonesCount()

    /**
     * Agrega una nueva primera vez
     */
    suspend fun addMilestone(
        title: String,
        description: String,
        date: LocalDateTime,
        category: FirstTimeCategory,
        photoUris: List<String> = emptyList(),
        audioNoteUri: String? = null,
        locationName: String? = null,
        location: Location? = null,
        tags: List<String> = emptyList(),
        isPrivate: Boolean = false,
        anniversaryReminders: Boolean = true
    ) {
        val milestone = FirstTimeMilestone(
            id = java.util.UUID.randomUUID().toString(),
            title = title,
            description = description,
            date = date,
            category = category,
            photoUris = photoUris.joinToString("|"),
            audioNoteUri = audioNoteUri,
            locationName = locationName,
            locationLatitude = location?.latitude,
            locationLongitude = location?.longitude,
            tags = tags.joinToString("|"),
            isPrivate = isPrivate,
            anniversaryReminders = anniversaryReminders
        )
        dao.insertMilestone(milestone)
    }

    /**
     * Actualiza una primera vez existente
     */
    suspend fun updateMilestone(milestone: FirstTimeMilestone) {
        dao.updateMilestone(milestone)
    }

    /**
     * Elimina una primera vez
     */
    suspend fun deleteMilestone(milestone: FirstTimeMilestone) {
        dao.deleteMilestone(milestone)
    }

    /**
     * Obtiene milestones por categoría
     */
    fun getMilestonesByCategory(category: FirstTimeCategory): Flow<List<FirstTimeMilestone>> {
        return dao.getMilestonesByCategory(category)
    }

    /**
     * Obtiene milestones entre fechas
     */
    fun getMilestonesBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<FirstTimeMilestone>> {
        return dao.getMilestonesBetweenDates(startDate, endDate)
    }

    /**
     * Detecta automáticamente el primer mensaje
     */
    suspend fun detectFirstMessage(firstMessageDate: LocalDateTime) {
        val exists = allMilestones.first().any { it.category == FirstTimeCategory.FIRST_MESSAGE }
        if (!exists) {
            addMilestone(
                title = "Nuestro Primer Mensaje",
                description = "El mensaje que inició todo 💕",
                date = firstMessageDate,
                category = FirstTimeCategory.FIRST_MESSAGE,
                isPrivate = false
            )
        }
    }

    /**
     * Verifica aniversarios de primeras veces
     */
    suspend fun checkAnniversaries(): List<FirstTimeAnniversary> {
        val today = LocalDate.now()
        val milestones = allMilestones.first()
        val anniversaries = mutableListOf<FirstTimeAnniversary>()

        milestones.forEach { milestone ->
            if (milestone.anniversaryReminders) {
                val milestoneDate = milestone.date.toLocalDate()
                val yearsPassed = ChronoUnit.YEARS.between(milestoneDate, today)

                if (yearsPassed > 0 &&
                    milestoneDate.monthValue == today.monthValue &&
                    milestoneDate.dayOfMonth == today.dayOfMonth
                ) {
                    anniversaries.add(
                        FirstTimeAnniversary(
                            milestone = milestone,
                            yearsPassed = yearsPassed.toInt()
                        )
                    )
                }
            }
        }

        return anniversaries
    }

    /**
     * Obtiene días restantes para el próximo aniversario
     */
    fun getDaysUntilNextAnniversary(milestone: FirstTimeMilestone): Int {
        val today = LocalDate.now()
        val milestoneDate = milestone.date.toLocalDate()
        val nextAnniversary = LocalDate.of(today.year, milestoneDate.month, milestoneDate.day)

        return if (nextAnniversary.isAfter(today)) {
            ChronoUnit.DAYS.between(today, nextAnniversary).toInt()
        } else {
            ChronoUnit.DAYS.between(today, nextAnniversary.plusYears(1)).toInt()
        }
    }
}

/**
 * Data class para aniversarios
 */
data class FirstTimeAnniversary(
    val milestone: FirstTimeMilestone,
    val yearsPassed: Int
)

/**
 * Helpers para serialización
 */
object FirstTimesSerializers {
    fun serializePhotoUris(uris: List<String>): String = uris.joinToString("|")
    fun deserializePhotoUris(data: String): List<String> = if (data.isEmpty()) emptyList() else data.split("|")

    fun serializeTags(tags: List<String>): String = tags.joinToString("|")
    fun deserializeTags(data: String): List<String> = if (data.isEmpty()) emptyList() else data.split("|")
}

/**
 * UI Composable - Pantalla de Primeras Veces
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstTimesScreen(
    registry: FirstTimesRegistry,
    onAddMilestone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val milestones by registry.allMilestones.collectAsState(initial = emptyList())
    val formatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Nuestras Primeras Veces 💕") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFB6C1),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMilestone,
                containerColor = Color(0xFFFF6B6B)
            ) {
                Icon(Icons.Default.Add, "Agregar primera vez")
            }
        }
    ) { paddingValues ->
        if (milestones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🌱", fontSize = MaterialTheme.typography.displayLarge.fontSize)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Aún no hay primeras veces registradas",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "¡Comienza agregando tu primer momento especial!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(milestones, key = { it.id }) { milestone ->
                    FirstTimeMilestoneCard(
                        milestone = milestone,
                        formatter = formatter,
                        onEdit = { /* TODO */ },
                        onDelete = { /* TODO */ }
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta individual para cada primera vez
 */
@Composable
fun FirstTimeMilestoneCard(
    milestone: FirstTimeMilestone,
    formatter: DateTimeFormatter,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = milestone.category.icon,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = milestone.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF1A1A2E)
                    )
                    Text(
                        text = milestone.date.format(formatter),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Contraer" else "Expandir"
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = milestone.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (milestone.locationName != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Ubicación",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = milestone.locationName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                val photoUris = FirstTimesSerializers.deserializePhotoUris(milestone.photoUris)
                if (photoUris.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "📸 ${photoUris.size} foto(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                val tags = FirstTimesSerializers.deserializeTags(milestone.tags)
                if (tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        tags.forEach { tag ->
                            Surface(
                                color = Color(0xFFFFB6C1),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = "#$tag",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                    TextButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.Red)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Eliminar", color = Color.Red)
                    }
                }
            }
        }
    }
}

/**
 * Diálogo para agregar/editar primera vez
 */
@Composable
fun AddFirstTimeDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        description: String,
        date: LocalDateTime,
        category: FirstTimeCategory,
        isPrivate: Boolean
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDateTime.now()) }
    var selectedCategory by remember { mutableStateOf(FirstTimeCategory.CUSTOM) }
    var isPrivate by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Primera Vez") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                // Selector de categoría
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        FirstTimeCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text("${category.icon} ${category.displayName}") },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Privado:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Checkbox(
                        checked = isPrivate,
                        onCheckedChange = { isPrivate = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, description, selectedDate, selectedCategory, isPrivate)
                        onDismiss()
                    }
                },
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
