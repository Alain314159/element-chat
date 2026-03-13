/*
 * Copyright 2024 Cerdita App
 * 
 * Sistema de Cupones de Amor
 * Cupones ganados en minijuegos que se regalan a la pareja
 * El ganador del cupón lo da a su pareja para que lo redima
 */

package im.vector.app.features.romantic.features.coupons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Tipos de cupones por categoría
 */
enum class CouponCategory(val emoji: String, val color: Color, val gradient: Brush) {
    ROMANTIC(
        "💕",
        Color(0xFFFF6B6B),
        Brush.verticalGradient(listOf(Color(0xFFFFB6C1), Color(0xFFFF6B6B)))
    ),
    SWEET(
        "🍫",
        Color(0xFFFFA07A),
        Brush.verticalGradient(listOf(Color(0xFFFFE4E1), Color(0xFFFFA07A)))
    ),
    SERVICE(
        "💆",
        Color(0xFFDDA0DD),
        Brush.verticalGradient(listOf(Color(0xFFE6E6FA), Color(0xFFDDA0DD)))
    ),
    ADVENTURE(
        "🌟",
        Color(0xFFFFD700),
        Brush.verticalGradient(listOf(Color(0xFFFFFACD), Color(0xFFFFD700)))
    ),
    SPICY(
        "🌶️",
        Color(0xFFFF4500),
        Brush.verticalGradient(listOf(Color(0xFFFF6347), Color(0xFFFF4500)))
    ),
    INTIMATE(
        "🔥",
        Color(0xFF8B0000),
        Brush.verticalGradient(listOf(Color(0xFFDC143C), Color(0xFF8B0000)))
    )
}

/**
 * Modelo de Cupón de Amor
 */
data class LoveCoupon(
    val id: String,
    val title: String,
    val description: String,
    val category: CouponCategory,
    val difficulty: CouponDifficulty,
    val xpReward: Int,
    val isRedeemed: Boolean = false,
    val isExpired: Boolean = false,
    val wonAt: LocalDateTime? = null,
    val givenAt: LocalDateTime? = null,
    val redeemedAt: LocalDateTime? = null,
    val winnerId: String,
    val receiverId: String?, // null hasta que se regala
    val expiresAt: LocalDateTime? = null,
    val timesUsed: Int = 0,
    val maxUses: Int = 1
)

/**
 * Dificultad para ganar el cupón
 */
enum class CouponDifficulty(val multiplier: Float, val minScore: Int) {
    EASY(1.0f, 50),
    MEDIUM(1.5f, 75),
    HARD(2.0f, 90),
    EXTREME(3.0f, 95)
}

/**
 * Banco de Cupones disponibles
 */
object CouponBank {
    
    fun getRomanticCoupons(): List<CouponTemplate> = listOf(
        CouponTemplate(
            title = "Masaje Relajante",
            description = "Un masaje de espalda de 20 minutos con aceites esenciales",
            category = CouponCategory.SERVICE,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 100,
            maxUses = 3
        ),
        CouponTemplate(
            title = "Desayuno en la Cama",
            description = "Prepararé tu desayuno favorito y te lo llevaré a la cama",
            category = CouponCategory.SERVICE,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 120,
            maxUses = 2
        ),
        CouponTemplate(
            title = "Noche de Películas",
            description = "Elegirás la película y prepararé palomitas y snacks",
            category = CouponCategory.ROMANTIC,
            difficulty = CouponDifficulty.EASY,
            xpReward = 60,
            maxUses = 5
        ),
        CouponTemplate(
            title = "Carta de Amor",
            description = "Escribiré una carta expressing mis sentimientos más profundos",
            category = CouponCategory.ROMANTIC,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 80,
            maxUses = 10
        ),
        CouponTemplate(
            title = "Baño de Espuma",
            description = "Prepararé un baño relajante con velas y música suave",
            category = CouponCategory.SERVICE,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 100,
            maxUses = 3
        ),
        CouponTemplate(
            title = "Paseo de la Mano",
            description = "Un paseo romántico tomados de la mano al atardecer",
            category = CouponCategory.ROMANTIC,
            difficulty = CouponDifficulty.EASY,
            xpReward = 50,
            maxUses = 10
        ),
        CouponTemplate(
            title = "Cena Sorpresa",
            description = "Cocinaré tu plato favorito para una cena especial",
            category = CouponCategory.SERVICE,
            difficulty = CouponDifficulty.HARD,
            xpReward = 150,
            maxUses = 2
        ),
        CouponTemplate(
            title = "Día de Spa en Casa",
            description = "Mascarillas, masajes y relajación total por 2 horas",
            category = CouponCategory.SERVICE,
            difficulty = CouponDifficulty.HARD,
            xpReward = 200,
            maxUses = 1
        )
    )
    
    fun getSweetCoupons(): List<CouponTemplate> = listOf(
        CouponTemplate(
            title = "Chocolate Caliente",
            description = "Prepararé chocolate caliente con malvaviscos",
            category = CouponCategory.SWEET,
            difficulty = CouponDifficulty.EASY,
            xpReward = 40,
            maxUses = 10
        ),
        CouponTemplate(
            title = "Galletas Caseras",
            description = "Hornearé tus galletas favoritas",
            category = CouponCategory.SWEET,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 80,
            maxUses = 5
        ),
        CouponTemplate(
            title = "Postre Sorpresa",
            description = "Un postre especial hecho con amor",
            category = CouponCategory.SWEET,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 90,
            maxUses = 3
        ),
        CouponTemplate(
            title = "Dulce Beso",
            description = "Un beso dulce cuando menos lo esperes",
            category = CouponCategory.SWEET,
            difficulty = CouponDifficulty.EASY,
            xpReward = 30,
            maxUses = 20
        ),
        CouponTemplate(
            title = "Abrazo de Oso",
            description = "Un abrazo apretado de 1 minuto completo",
            category = CouponCategory.SWEET,
            difficulty = CouponDifficulty.EASY,
            xpReward = 35,
            maxUses = 15
        )
    )
    
    fun getAdventureCoupons(): List<CouponTemplate> = listOf(
        CouponTemplate(
            title = "Cita Sorpresa",
            description = "Una cita donde yo planeo todo, tú solo disfruta",
            category = CouponCategory.ADVENTURE,
            difficulty = CouponDifficulty.HARD,
            xpReward = 180,
            maxUses = 1
        ),
        CouponTemplate(
            title = "Lugar Nuevo",
            description = "Visitaremos un lugar donde nunca hemos estado",
            category = CouponCategory.ADVENTURE,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 120,
            maxUses = 3
        ),
        CouponTemplate(
            title = "Actividad Extrema",
            description = "Haremos esa actividad que siempre has querido probar",
            category = CouponCategory.ADVENTURE,
            difficulty = CouponDifficulty.EXTREME,
            xpReward = 250,
            maxUses = 1
        ),
        CouponTemplate(
            title = "Picnic Romántico",
            description = "Prepararé una canasta y buscaremos el lugar perfecto",
            category = CouponCategory.ADVENTURE,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 100,
            maxUses = 4
        ),
        CouponTemplate(
            title = "Ver el Amanecer",
            description = "Buscaremos el mejor lugar para ver el amanecer juntos",
            category = CouponCategory.ADVENTURE,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 110,
            maxUses = 5
        )
    )
    
    fun getSpicyCoupons(): List<CouponTemplate> = listOf(
        CouponTemplate(
            title = "Beso Apasionado",
            description = "Un beso que te dejará sin aliento (mínimo 1 minuto)",
            category = CouponCategory.SPICY,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 100,
            maxUses = 10
        ),
        CouponTemplate(
            title = "Masaje Sensual",
            description = "Un masaje más íntimo y provocativo de 30 minutos",
            category = CouponCategory.SPICY,
            difficulty = CouponDifficulty.HARD,
            xpReward = 180,
            maxUses = 2
        ),
        CouponTemplate(
            title = "Baile Sensual",
            description = "Bailaré para ti de forma sexy con tu canción favorita",
            category = CouponCategory.SPICY,
            difficulty = CouponDifficulty.HARD,
            xpReward = 200,
            maxUses = 1
        ),
        CouponTemplate(
            title = "Susurros al Oído",
            description = "Te diré cosas picantes al oído por 5 minutos",
            category = CouponCategory.SPICY,
            difficulty = CouponDifficulty.MEDIUM,
            xpReward = 120,
            maxUses = 5
        ),
        CouponTemplate(
            title = "Venda en los Ojos",
            description = "Me vendaré los ojos y tú tendrás el control por 15 minutos",
            category = CouponCategory.SPICY,
            difficulty = CouponDifficulty.EXTREME,
            xpReward = 300,
            maxUses = 1
        ),
        CouponTemplate(
            title = "Recorrido con Hielo",
            description = "Usaré un cubo de hielo para recorrer tu cuerpo",
            category = CouponCategory.SPICY,
            difficulty = CouponDifficulty.HARD,
            xpReward = 220,
            maxUses = 2
        ),
        CouponTemplate(
            title = "Noche de Pasión",
            description = "Una noche completa dedicada al placer mutuo",
            category = CouponCategory.INTIMATE,
            difficulty = CouponDifficulty.EXTREME,
            xpReward = 500,
            maxUses = 1
        ),
        CouponTemplate(
            title = "Fantasía Cumplida",
            description = "Cumpliré una de tus fantasías (dentro de lo razonable)",
            category = CouponCategory.INTIMATE,
            difficulty = CouponDifficulty.EXTREME,
            xpReward = 600,
            maxUses = 1
        ),
        CouponTemplate(
            title = "Strip tease",
            description = "Haré un strip tease sexy solo para ti",
            category = CouponCategory.SPICY,
            difficulty = CouponDifficulty.HARD,
            xpReward = 250,
            maxUses = 1
        ),
        CouponTemplate(
            title = "Aceite Caliente",
            description = "Usaré aceite caliente para un masaje íntimo",
            category = CouponCategory.INTIMATE,
            difficulty = CouponDifficulty.EXTREME,
            xpReward = 400,
            maxUses = 1
        )
    )
    
    fun getAllCoupons(): List<CouponTemplate> {
        return getRomanticCoupons() + getSweetCoupons() + getAdventureCoupons() + getSpicyCoupons()
    }
    
    fun getCouponByDifficulty(difficulty: CouponDifficulty): List<CouponTemplate> {
        return getAllCoupons().filter { it.difficulty == difficulty }
    }
}

data class CouponTemplate(
    val title: String,
    val description: String,
    val category: CouponCategory,
    val difficulty: CouponDifficulty,
    val xpReward: Int,
    val maxUses: Int = 1
)

/**
 * UI de Colección de Cupones
 */
@Composable
fun LoveCouponCollection(
    coupons: List<LoveCoupon>,
    onGiveCoupon: (LoveCoupon) -> Unit,
    onRedeemCoupon: (LoveCoupon) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<CouponCategory?>(null) }
    
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "🎫 Mis Cupones de Amor",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B6B)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Filtros por categoría
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { selectedCategory = null },
                label = { Text("Todos") }
            )
            
            CouponCategory.values().forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category.emoji) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Grid de cupones
        val filteredCoupons = if (selectedCategory == null) {
            coupons
        } else {
            coupons.filter { it.category == selectedCategory }
        }
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredCoupons) { coupon ->
                LoveCouponCard(
                    coupon = coupon,
                    onGive = { onGiveCoupon(coupon) },
                    onRedeem = { onRedeemCoupon(coupon) }
                )
            }
        }
        
        if (coupons.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🎮",
                        fontSize = 64.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡Gana cupones en los minijuegos!",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Los cupones que ganes podrás dárselos a tu pareja ❤️",
                        fontSize = 14.sp,
                        color = Color(0xFFFF6B6B)
                    )
                }
            }
        }
    }
}

@Composable
fun LoveCouponCard(
    coupon: LoveCoupon,
    onGive: () -> Unit,
    onRedeem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showBack by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                brush = coupon.category.gradient,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = coupon.category.emoji,
                        fontSize = 24.sp
                    )
                    
                    Text(
                        text = coupon.difficulty.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (coupon.difficulty) {
                            CouponDifficulty.EASY -> Color(0xFF32CD32)
                            CouponDifficulty.MEDIUM -> Color(0xFFFFA500)
                            CouponDifficulty.HARD -> Color(0xFFFF4500)
                            CouponDifficulty.EXTREME -> Color(0xFF8B0000)
                        }
                    )
                }
                
                // Title
                Text(
                    text = coupon.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    maxLines = 2
                )
                
                // Description
                Text(
                    text = coupon.description,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 3
                )
                
                // Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Usos: ${coupon.timesUsed}/${coupon.maxUses}",
                        fontSize = 8.sp,
                        color = Color.Gray
                    )
                    
                    if (!coupon.isRedeemed && coupon.receiverId != null) {
                        Button(
                            onClick = onRedeem,
                            modifier = Modifier.height(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF32CD32)
                            )
                        ) {
                            Text("Redimir", fontSize = 10.sp)
                        }
                    } else if (coupon.receiverId == null) {
                        Button(
                            onClick = onGive,
                            modifier = Modifier.height(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF6B6B)
                            )
                        ) {
                            Text("Regalar", fontSize = 10.sp)
                        }
                    }
                }
            }
            
            // Stamp for redeemed
            if (coupon.isRedeemed) {
                Text(
                    text = "REDIMIDO",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .rotate(-45f)
                )
            }
        }
    }
}

/**
 * Pantalla para regalar cupón
 */
@Composable
fun GiveCouponDialog(
    coupon: LoveCoupon,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var message by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🎁 Regalar Cupón") },
        text = {
            Column {
                Text(
                    text = "Vas a regalar este cupón:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF0F5)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = coupon.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = coupon.description,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Mensaje para tu pareja (opcional):",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("Ej: Te amo, esto es para ti ❤️") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "💡 Al regalar este cupón, TÚ serás quien lo cumpla para tu pareja",
                    fontSize = 11.sp,
                    color = Color(0xFFFF6B6B),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(message) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B)
                )
            ) {
                Text("🎁 Regalar Cupón")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
