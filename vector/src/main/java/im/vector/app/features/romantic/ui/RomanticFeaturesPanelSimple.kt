/*
 * Copyright (c) 2026 Element Chat Romántico
 *
 * Panel de funcionalidades románticas - Versión simplificada para navegación
 */

package im.vector.app.features.romantic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Panel de funcionalidades románticas - Versión simplificada
 * Para usar con navegación
 */
@Composable
fun RomanticFeaturesPanel(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color(0xFFFF6B6B)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "💕 Próximamente",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Esta funcionalidad romántica está en desarrollo.\n¡Muy pronto podrás disfrutarla!",
                fontSize = 16.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF0F5))
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF0F5)
                )
            ) {
                Column {
                    Text(
                        text = "🚧 En Desarrollo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B6B)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = """
                            Estamos trabajando en esta característica para ofrecerte:
                            
                            • Una experiencia romántica única
                            • Integración perfecta con tu chat
                            • Funcionalidades exclusivas para parejas
                            
                            ¡Gracias por tu paciencia!
                        """.trimIndent(),
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}
