/*
 * Copyright 2024 Cerdita App
 * 
 * Árbol de la Relación - Visualización del crecimiento del amor
 * Muestra hitos y momentos especiales de la pareja
 */

package im.vector.app.features.romantic.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

/**
 * Árbol de la Relación
 * 
 * FUNCIONALIDAD:
 * - Visualiza la relación como un árbol que crece
 * - Cada hito es una rama/fruto del árbol
 * - El árbol crece con el tiempo
 * - Desbloquea decoraciones especiales
 */

data class RelationshipMilestone(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val type: MilestoneType,
    val icon: String
)

enum class MilestoneType {
    FIRST_DATE,       // Primera cita
    FIRST_KISS,       // Primer beso
    ANNIVERSARY,      // Aniversario
    TRIP,             // Viaje
    GIFT,             // Regalo especial
    MEMORY,           // Recuerdo especial
    ACHIEVEMENT,      // Logro
    OTHER             // Otro
}

object RelationshipTree {
    
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
    fun getMilestones(startDate: LocalDate): List<RelationshipMilestone> {
        val milestones = mutableListOf<RelationshipMilestone>()
        val today = LocalDate.now()
        
        // Primer mes
        val oneMonth = startDate.plusMonths(1)
        if (oneMonth.isBefore(today) || oneMonth.isEqual(today)) {
            milestones.add(
                RelationshipMilestone(
                    id = "first_month",
                    title = "Primer Mes 💕",
                    description = "¡Un mes juntos!",
                    date = oneMonth,
                    type = MilestoneType.ANNIVERSARY,
                    icon = "🌱"
                )
            )
        }
        
        // Primeros 3 meses
        val threeMonths = startDate.plusMonths(3)
        if (threeMonths.isBefore(today) || threeMonths.isEqual(today)) {
            milestones.add(
                RelationshipMilestone(
                    id = "three_months",
                    title = "3 Meses 💖",
                    description = "Tres meses de amor",
                    date = threeMonths,
                    type = MilestoneType.ANNIVERSARY,
                    icon = "🌿"
                )
            )
        }
        
        // Primeros 6 meses
        val sixMonths = startDate.plusMonths(6)
        if (sixMonths.isBefore(today) || sixMonths.isEqual(today)) {
            milestones.add(
                RelationshipMilestone(
                    id = "six_months",
                    title = "6 Meses 💗",
                    description = "Medio año juntos",
                    date = sixMonths,
                    type = MilestoneType.ANNIVERSARY,
                    icon = "🪴"
                )
            )
        }
        
        // Primer año
        val oneYear = startDate.plusYears(1)
        if (oneYear.isBefore(today) || oneYear.isEqual(today)) {
            milestones.add(
                RelationshipMilestone(
                    id = "first_year",
                    title = "Primer Año 💕",
                    description = "¡Un año de amor!",
                    date = oneYear,
                    type = MilestoneType.ANNIVERSARY,
                    icon = "🌳"
                )
            )
        }
        
        // Agregar más hitos personalizados aquí
        
        return milestones.sortedBy { it.date }
    }
    
    fun getTreeLevel(daysTogether: Long): TreeLevel {
        return when {
            daysTogether < 30 -> TreeLevel.SPROUT
            daysTogether < 90 -> TreeLevel.SEEDLING
            daysTogether < 180 -> TreeLevel.SAPLING
            daysTogether < 365 -> TreeLevel.YOUNG_TREE
            daysTogether < 730 -> TreeLevel.MATURE_TREE
            else -> TreeLevel.ANCIENT_TREE
        }
    }
}

enum class TreeLevel(
    val name: String,
    val icon: String,
    val color: Color
) {
    SPROUT("Brote", "🌱", Color(0xFF90EE90)),
    SEEDLING("Plántula", "🌿", Color(0xFF98FB98)),
    SAPLING("Arbolito", "🪴", Color(0xFF32CD32)),
    YOUNG_TREE("Árbol Joven", "🌳", Color(0xFF228B22)),
    MATURE_TREE("Árbol Maduro", "🌲", Color(0xFF006400)),
    ANCIENT_TREE("Árbol Ancestral", "🌴", Color(0xFF00FA9A))
}

@Composable
fun RelationshipTreeView(
    startDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val daysTogether = Period.between(startDate, LocalDate.now()).days.toLong()
    val treeLevel = RelationshipTree.getTreeLevel(daysTogether)
    val milestones = remember(startDate) { RelationshipTree.getMilestones(startDate) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header del árbol
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFFFF0F5),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = treeLevel.icon,
                    fontSize = 64.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Nuestro Árbol de Amor",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B6B)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Nivel: ${treeLevel.name}",
                    fontSize = 16.sp,
                    color = treeLevel.color
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "$daysTogether días creciendo juntos 💕",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Línea de tiempo de hitos
        Text(
            text = "🍎 Nuestros Hitos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B6B)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (milestones.isEmpty()) {
            Text(
                text = "Los hitos aparecerán aquí a medida que su relación crezca 🌱",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(milestones) { milestone ->
                    MilestoneCard(milestone = milestone)
                }
            }
        }
    }
}

@Composable
fun MilestoneCard(
    milestone: RelationshipMilestone,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFFFFF0F5),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFB6C1), Color(0xFFFF6B6B))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = milestone.icon,
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = milestone.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = milestone.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = milestone.date.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy")),
                    fontSize = 12.sp,
                    color = Color(0xFFFF6B6B)
                )
            }
        }
    }
}

@Composable
fun TreeGrowthChart(
    startDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val daysTogether = Period.between(startDate, LocalDate.now()).days.toFloat()
    val maxDays = 365 * 5f // 5 años máximo
    
    val progress = (daysTogether / maxDays).coerceIn(0f, 1f)
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "📈 Crecimiento de Nuestro Árbol",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF228B22)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Barra de progreso
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            // Línea de base
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val width = size.width
                val height = size.height
                
                // Eje Y
                drawLine(
                    color = Color.Gray,
                    start = Offset(50f, height - 20f),
                    end = Offset(50f, 20f),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
                
                // Eje X
                drawLine(
                    color = Color.Gray,
                    start = Offset(50f, height - 20f),
                    end = Offset(width - 20f, height - 20f),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
                
                // Línea de progreso
                val progressX = 50f + (width - 70f) * progress
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF90EE90), Color(0xFF228B22))
                    ),
                    start = Offset(50f, height - 20f),
                    end = Offset(progressX, height - 20f - (height - 40f) * progress),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Etiquetas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Inicio",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = "${(daysTogether / 30).toInt()} meses",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF228B22)
            )
            Text(
                text = "5 años",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
