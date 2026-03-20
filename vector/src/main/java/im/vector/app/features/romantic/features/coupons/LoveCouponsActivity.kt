/*
 * Copyright 2024 Cerdita App
 *
 * Activity para Cupones de Amor con Cupones Personalizables
 */

package im.vector.app.features.romantic.features.coupons

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
import im.vector.app.features.romantic.data.database.CustomCouponEntity

/**
 * LoveCouponsActivity - Screen for managing love coupons with custom coupon creator
 */
class LoveCouponsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoveCouponsScreen(
                onBackClick = { finish() }
            )
        }
    }
}

/**
 * Composable screen for Love Coupons
 */
@Composable
fun LoveCouponsScreen(
    onBackClick: () -> Unit
) {
    var showCreateCouponDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    val customCoupons = remember { emptyList<CustomCouponEntity>() } // Se cargaría del repository

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🎟️ Cupones de Amor",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showHistoryDialog = true }) {
                        Icon(Icons.Default.History, contentDescription = "Historial")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateCouponDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear cupón personalizado")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Pestañas
            var selectedTab by remember { mutableStateOf(0) }
            val tabs = listOf("Mis Cupones", "Para Redimir", "Personalizados")

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Contenido
            if (customCoupons.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay cupones aún",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Crea tu primer cupón personalizado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(customCoupons) { coupon ->
                        CustomCouponCard(coupon = coupon)
                    }
                }
            }
        }
    }

    // Diálogo para crear cupón personalizado
    if (showCreateCouponDialog) {
        CreateCustomCouponDialog(
            onConfirm = { title, description, conditions, expirationDate ->
                // Aquí se crearía el cupón con el repository
                showCreateCouponDialog = false
            },
            onDismiss = { showCreateCouponDialog = false }
        )
    }

    // Diálogo de historial
    if (showHistoryDialog) {
        RedemptionHistoryDialog(
            onDismiss = { showHistoryDialog = false }
        )
    }
}

/**
 * Tarjeta de cupón personalizado
 */
@Composable
fun CustomCouponCard(
    coupon: CustomCouponEntity
) {
    val isExpired = coupon.expirationDate != null &&
            coupon.expirationDate < System.currentTimeMillis()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpired || coupon.isRedeemed) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
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
                    text = coupon.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (coupon.isRedeemed) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Redimido", fontSize = 10.sp) }
                    )
                } else if (isExpired) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Expirado", fontSize = 10.sp) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = coupon.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Condiciones
            if (coupon.conditions.isNotEmpty()) {
                Text(
                    text = "📋 Condiciones:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                coupon.conditions.split("|").forEach { condition ->
                    Text(
                        text = "• $condition",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (coupon.expirationDate != null) {
                    Text(
                        text = "⏰ Vence: ${coupon.expirationDate?.let { formatDate(it) } ?: "Sin vencimiento"}",
                        fontSize = 11.sp,
                        color = if (isExpired) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
                Text(
                    text = "🔄 ${coupon.redemptionCount}/${coupon.maxRedemptions} usos",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Botones de acción
            if (!coupon.isRedeemed && !isExpired) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { /* Redimir cupón */ }) {
                        Text("Redimir")
                    }
                    if (coupon.mediaAttachments.isNotEmpty()) {
                        IconButton(onClick = { /* Ver adjuntos */ }) {
                            Icon(Icons.Default.AttachFile, contentDescription = "Ver adjuntos")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Diálogo para crear cupón personalizado
 */
@Composable
fun CreateCustomCouponDialog(
    onConfirm: (String, String, String, Long?) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var conditions by remember { mutableStateOf("") }
    var hasExpiration by remember { mutableStateOf(false) }
    var selectedExpirationDate by remember { mutableStateOf<Long?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Cupón Personalizado") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título del cupón") },
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
                OutlinedTextField(
                    value = conditions,
                    onValueChange = { conditions = it },
                    label = { Text("Condiciones (separadas por |)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = hasExpiration,
                        onCheckedChange = { hasExpiration = it }
                    )
                    Text(
                        text = "Con fecha de vencimiento",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (hasExpiration) {
                    // Aquí iría un DatePicker
                    Text(
                        text = "📅 Selecciona fecha de vencimiento",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📸 Puedes agregar fotos/videos después",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(title, description, conditions, selectedExpirationDate)
                },
                enabled = title.isNotBlank()
            ) {
                Text("Crear")
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
 * Diálogo de historial de redención
 */
@Composable
fun RedemptionHistoryDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("📜 Historial de Redención") },
        text = {
            Column {
                Text(
                    text = "Aquí verás el historial de cupones redimidos con fecha y fotos del momento.",
                    style = MaterialTheme.typography.bodyMedium
                )
                // Aquí iría una LazyColumn con el historial del repository
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
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
