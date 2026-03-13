/*
 * Copyright 2024 Cerdita App
 * 
 * Configuración de Funcionalidades Románticas
 * Permite personalizar la experiencia romántica
 */

package im.vector.app.features.romantic.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import java.time.LocalDate

/**
 * Activity de Configuración Romántica
 * 
 * OPCIONES DE CONFIGURACIÓN:
 * 1. Fecha de inicio de relación
 * 2. Activar/desactivar efectos románticos
 * 3. Activar recordatorio de aniversario
 * 4. Palabras románticas personalizadas
 * 5. Resetear contador
 */
class RomanticSettingsActivity : ComponentActivity() {
    
    private lateinit var daysManager: RelationshipDaysManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        daysManager = RelationshipDaysManager(this)
        
        setContent {
            RomanticSettingsScreen(
                daysManager = daysManager,
                onBackClick = { finish() }
            )
        }
    }
    
    companion object {
        const val EXTRA_SHOW_DATE_PICKER = "show_date_picker"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RomanticSettingsScreen(
    daysManager: RelationshipDaysManager,
    onBackClick: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var romanticEffectsEnabled by remember { mutableStateOf(true) }
    var anniversaryReminderEnabled by remember { mutableStateOf(daysManager.isAnniversaryReminderEnabled()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "💕 Configuración Romántica",
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Sección: Contador de Días
            RomanticSettingsSection(
                title = "📅 Contador de Días Juntos",
                icon = Icons.Default.CalendarToday
            ) {
                var startDateStr by remember { 
                    mutableStateOf(daysManager.getStartDate()?.toString() ?: "No configurada") 
                }
                
                Text(
                    text = "Fecha de inicio: $startDateStr",
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB6C1)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cambiar fecha")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = daysManager.getFormattedTimeTogether(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B6B)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sección: Efectos Románticos
            RomanticSettingsSection(
                title = "✨ Efectos Románticos",
                icon = Icons.Default.AutoAwesome
            ) {
                SwitchSetting(
                    title = "Activar efectos románticos",
                    subtitle = "Mostrar corazones, besos, etc. al detectar palabras románticas",
                    checked = romanticEffectsEnabled,
                    onCheckedChange = { romanticEffectsEnabled = it }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sección: Recordatorios
            RomanticSettingsSection(
                title = "🔔 Recordatorios",
                icon = Icons.Default.Notifications
            ) {
                SwitchSetting(
                    title = "Recordatorio de aniversario",
                    subtitle = "Recibir notificación en tu aniversario",
                    checked = anniversaryReminderEnabled,
                    onCheckedChange = { 
                        anniversaryReminderEnabled = it
                        daysManager.setAnniversaryReminder(it)
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sección: Peligro
            RomanticSettingsSection(
                title = "⚠️ Zona de Peligro",
                icon = Icons.Default.Warning,
                color = Color(0xFFFF6B6B)
            ) {
                Button(
                    onClick = {
                        daysManager.clearStartDate()
                        // Mostrar confirmación
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF0000)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resetear contador de días")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Esta acción no se puede deshacer",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        
        // Dialog para seleccionar fecha
        if (showDatePicker) {
            SetStartDateDialog(
                onDateSelected = { date ->
                    daysManager.setStartDate(date)
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

/**
 * Sección de configuración romántica
 */
@Composable
fun RomanticSettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color = Color(0xFFFF6B6B),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFFF0F5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            content()
        }
    }
}

/**
 * Switch setting component
 */
@Composable
fun SwitchSetting(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFFFB6C1)
            )
        )
    }
}
