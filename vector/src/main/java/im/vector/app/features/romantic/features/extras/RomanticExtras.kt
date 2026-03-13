/*
 * Copyright 2024 Cerdita App
 * 
 * Funcionalidades Románticas Adicionales
 * Contador de días, estados, frases, y más
 */

package im.vector.app.features.romantic.features.extras

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

/**
 * Widget de Estado de la Relación
 */
@Composable
fun RelationshipStatusWidget(
    startDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val daysTogether = Period.between(startDate, LocalDate.now()).days
    val monthsTogether = Period.between(startDate, LocalDate.now()).toTotalMonths()
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F5)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFFB6C1), Color(0xFFFF6B6B))
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💕 Tiempo Juntos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatusItem(
                        value = daysTogether.toString(),
                        label = "Días",
                        emoji = "📅"
                    )
                    
                    StatusItem(
                        value = monthsTogether.toString(),
                        label = "Meses",
                        emoji = "🌙"
                    )
                    
                    StatusItem(
                        value = (monthsTogether / 12).toString(),
                        label = "Años",
                        emoji = "🎂"
                    )
                }
            }
        }
    }
}

@Composable
fun StatusItem(
    value: String,
    label: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

/**
 * Frases Románticas del Día
 */
object DailyLoveQuotes {
    
    private val quotes = listOf(
        "El amor no se mira, se siente, y aún más cuando ella está lejos. ❤️",
        "Eres la casualidad más bonita de mi vida. 🌟",
        "Contigo quiero todos los días de mi vida. 💕",
        "Te amo no solo por lo que eres, sino por lo que soy cuando estoy contigo. 💖",
        "Mi lugar favorito en el mundo es a tu lado. 🏠",
        "Eres mi sueño hecho realidad. ✨",
        "Cada día te amo más, y mañana te amaré aún más. 💗",
        "Juntos es mi lugar favorito. 💑",
        "Eres la razón por la que sonrío todos los días. 😊",
        "El amor es estar contigo en cada momento. ⏰",
        "Eres mi hoy y todos mis mañanas. 🌅",
        "Te elegiría a ti una y mil veces más. 💫",
        "Mi corazón late más fuerte cuando estás cerca. 💓",
        "Eres lo mejor que me ha pasado. 🎁",
        "Contigo, todo es más bonito. 🌸",
        "Eres mi persona favorita. 💕",
        "El amor verdadero es estar juntos en las buenas y en las malas. 💪",
        "Cada momento contigo es un tesoro. 💎",
        "Eres mi sol en los días nublados. ☀️",
        "Te amo más que ayer, pero menos que mañana. 📈"
    )
    
    fun getQuoteOfTheDay(): String {
        val dayOfYear = LocalDate.now().dayOfYear
        return quotes[dayOfYear % quotes.size]
    }
    
    fun getRandomQuote(): String {
        return quotes.random()
    }
}

/**
 * Widget de Frase del Día
 */
@Composable
fun DailyQuoteWidget(
    modifier: Modifier = Modifier
) {
    val quote = remember { DailyLoveQuotes.getQuoteOfTheDay() }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F5)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💌",
                        fontSize = 24.sp
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "Frase del Día",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B6B)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "\"$quote\"",
                    fontSize = 14.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Color(0xFF333333),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

/**
 * Lista de Razones Por Las Que Te Amo
 */
@Composable
fun LoveReasonsList(
    modifier: Modifier = Modifier
) {
    val reasons = remember {
        listOf(
            "Me haces sonreír incluso en los días difíciles",
            "Me escuchas y me comprendes como nadie",
            "Me apoyas en todos mis sueños y proyectos",
            "Tienes la mejor sonrisa del mundo",
            "Me haces sentir la persona más especial",
            "Siempre sabes cómo hacerme reír",
            "Me das los mejores abrazos",
            "Confías en mí incondicionalmente",
            "Me inspiras a ser mejor persona",
            "Amas a mi familia como si fuera la tuya",
            "Tienes los detalles más bonitos conmigo",
            "Me haces sentir seguro/a de compartir todo contigo",
            "Nunca me juzgas, siempre me comprendes",
            "Celebras mis logros como si fueran tuyos",
            "Me acompañas en los momentos difíciles"
        )
    }
    
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reasons.size) { index ->
            ReasonCard(
                reason = reasons[index],
                number = index + 1
            )
        }
    }
}

@Composable
fun ReasonCard(
    reason: String,
    number: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFF6B6B)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = reason,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            
            Text(
                text = "💕",
                fontSize = 16.sp
            )
        }
    }
}

/**
 * Predicciones de Relación (diversión)
 */
object RelationshipPredictions {
    
    fun getPrediction(): String {
        val predictions = listOf(
            "Hoy será un día lleno de sorpresas románticas 💝",
            "Prepárate para recibir mucho amor hoy 💖",
            "Es un buen día para planear una cita especial 🌹",
            "Las estrellas dicen que hoy se reirán mucho juntos 😂",
            "Hoy es perfecto para un abrazo largo 🤗",
            "Alguien está pensando mucho en ti hoy 💭",
            "La luna favorece las conversaciones profundas hoy 🌙",
            "Hoy es día de demostrar amor con hechos 💪",
            "Las energías están alineadas para el romance ✨",
            "Hoy recibirás un mensaje que te hará sonreír 😊"
        )
        
        return predictions.random()
    }
}

/**
 * Widget de Predicción del Día
 */
@Composable
fun DailyPredictionWidget(
    modifier: Modifier = Modifier
) {
    val prediction = remember { RelationshipPredictions.getPrediction() }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6E6FA)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔮",
                fontSize = 32.sp
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "Predicción del Día",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                Text(
                    text = prediction,
                    fontSize = 14.sp,
                    color = Color(0xFF333333)
                )
            }
        }
    }
}

/**
 * Contador de Próximos Eventos
 */
@Composable
fun NextEventCountdown(
    eventName: String,
    eventDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val daysUntil = Period.between(LocalDate.now(), eventDate).days
    
    if (daysUntil >= 0) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
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
                Column {
                    Text(
                        text = eventName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    
                    Text(
                        text = eventDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFF6B6B)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = daysUntil.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Text(
                            text = "días",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}
