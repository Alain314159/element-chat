/*
 * Copyright 2024 Cerdita App
 *
 * Activity para el Sistema de Mascotas con Personalización de Outfits
 */

package im.vector.app.features.romantic.ui.mascots

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.vector.app.features.romantic.data.database.MascotOutfitEntity
import im.vector.app.features.romantic.data.database.OutfitRarity
import im.vector.app.features.romantic.data.database.OutfitType

/**
 * MascotSystemActivity - Screen for love mascots system with outfit customization
 */
class MascotSystemActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MascotSystemScreen(
                onBackClick = { finish() }
            )
        }
    }
}

/**
 * Composable screen for Mascot System
 */
@Composable
fun MascotSystemScreen(
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Mascota", "Inventario", "Tienda")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🐾 Mascotas del Amor",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
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
                0 -> MascotViewTab()
                1 -> InventoryTab()
                2 -> ShopTab()
            }
        }
    }
}

/**
 * Pestaña de vista de mascota
 */
@Composable
fun MascotViewTab() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🐾 Tu Mascota",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cerdita o Koalita con outfits personalizados",
                style = MaterialTheme.typography.bodyMedium
            )
            // Aquí iría el canvas con la mascota y el outfit equipado
        }
    }
}

/**
 * Pestaña de inventario de outfits
 */
@Composable
fun InventoryTab() {
    val outfits = remember { emptyList<MascotOutfitEntity>() } // Se cargaría del repository

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "🎨 Tus Outfits",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (outfits.isEmpty()) {
            Text(
                text = "No tienes outfits aún. ¡Visita la tienda!",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(outfits) { outfit ->
                    OutfitCard(outfit = outfit)
                }
            }
        }
    }
}

/**
 * Pestaña de tienda de outfits
 */
@Composable
fun ShopTab() {
    val allOutfits = remember { emptyList<MascotOutfitEntity>() } // Se cargaría del repository

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "🛒 Tienda de Outfits",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allOutfits) { outfit ->
                OutfitShopCard(outfit = outfit)
            }
        }
    }
}

/**
 * Tarjeta de outfit en inventario
 */
@Composable
fun OutfitCard(
    outfit: MascotOutfitEntity
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = outfit.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = outfit.type,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getRarityEmoji(outfit.rarity),
                    fontSize = 12.sp
                )
                if (outfit.coinCost > 0) {
                    Text(
                        text = "🪙 ${outfit.coinCost}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta de outfit en tienda
 */
@Composable
fun OutfitShopCard(
    outfit: MascotOutfitEntity
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = outfit.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = outfit.description ?: "",
                fontSize = 10.sp,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getRarityEmoji(outfit.rarity),
                    fontSize = 12.sp
                )
                if (outfit.isUnlocked) {
                    Text(
                        text = "✅ Desbloqueado",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "🪙 ${outfit.coinCost}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Obtiene el emoji de rareza
 */
fun getRarityEmoji(rarity: String): String {
    return when (rarity) {
        "COMMON" -> "⚪"
        "RARE" -> "🔵"
        "EPIC" -> "🟣"
        "LEGENDARY" -> "🟡"
        else -> "⚪"
    }
}
