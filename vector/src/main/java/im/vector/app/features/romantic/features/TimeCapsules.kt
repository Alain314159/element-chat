/*
 * Copyright 2024 Cerdita App
 * 
 * Cápsulas del Tiempo y Mensajes Programados
 * Funcionalidad para enviar mensajes al futuro
 */

package im.vector.app.features.romantic.features

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Pantalla de Cápsulas del Tiempo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCapsulesScreen(
    onCreateCapsule: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⏳ Cápsulas del Tiempo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateCapsule,
                containerColor = Color(0xFFFF6B6B)
            ) {
                Icon(Icons.Default.Add, "Crear cápsula")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) { index ->
                TimeCapsuleCard(
                    title = "Nuestra Primera Cita",
                    openDate = LocalDate.now().plusMonths(index.toLong()),
                    isLocked = index > 2,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun TimeCapsuleCard(
    title: String,
    openDate: LocalDate,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isLocked) Color(0xFFE0E0E0) else Color(0xFFFFF0F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Se abre: ${openDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Icon(
                imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                contentDescription = null,
                tint = if (isLocked) Color.Gray else Color(0xFFFF6B6B)
            )
        }
    }
}

/**
 * Mensajes Programados
 */
@Composable
fun ScheduledMessagesScreen(
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💌 Mensajes Programados") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No hay mensajes programados",
                fontSize = 18.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B)
                )
            ) {
                Text("Programar mensaje")
            }
        }
    }
}

/**
 * Votos y Promesas de Pareja
 */
@Composable
fun LoveVowsScreen(
    modifier: Modifier = Modifier
) {
    val vows = remember { 
        listOf(
            "Te prometo hacerte sonreír cada día" to 15,
            "Siempre te escucharé con atención" to 30,
            "Celebraré tus logros como míos" to 7,
            "Te apoyaré en los momentos difíciles" to 45
        ) 
    }
    
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(vows.size) { index ->
            val vow = vows[index]
            LoveVowCard(
                promise = vow.first,
                completedCount = vow.second,
                onClick = { }
            )
        }
    }
}

@Composable
fun LoveVowCard(
    promise: String,
    completedCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = promise,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LinearProgressIndicator(
                    progress = (completedCount % 30) / 30f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFFFF6B6B),
                    trackColor = Color(0xFFFFE0E0)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Completada $completedCount veces",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

/**
 * Certificados de Amor
 */
@Composable
fun LoveCertificatesScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "📜 Certificados de Amor",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B6B)
        )
        
        LoveCertificate(
            title = "Certificado de Amor Verdadero",
            issuedTo = "Mi Amor",
            issuedDate = LocalDate.now(),
            validUntil = null // Válido para siempre
        )
        
        LoveCertificate(
            title = "Certificado de Mejor Pareja",
            issuedTo = "Nosotros",
            issuedDate = LocalDate.now().minusMonths(6),
            validUntil = LocalDate.now().plusYears(1)
        )
    }
}

@Composable
fun LoveCertificate(
    title: String,
    issuedTo: String,
    issuedDate: LocalDate,
    validUntil: LocalDate?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFD700), Color(0xFFFFE4B5))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏆",
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Otorgado a: $issuedTo",
                fontSize = 14.sp,
                color = Color(0xFF8B4513)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Fecha: ${issuedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                fontSize = 12.sp,
                color = Color(0xFF8B4513)
            )
            
            if (validUntil != null) {
                Text(
                    text = "Válido hasta: ${validUntil.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            } else {
                Text(
                    text = "✨ Válido por la eternidad ✨",
                    fontSize = 10.sp,
                    color = Color(0xFFFF6B6B)
                )
            }
        }
    }
}
