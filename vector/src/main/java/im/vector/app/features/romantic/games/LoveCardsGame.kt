/*
 * Copyright 2024 Cerdita App
 * 
 * Tarjetas de Amor - Minijuego para parejas
 * Muestra tarjetas con preguntas románticas, retos y mensajes
 */

package im.vector.app.features.romantic.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import kotlin.random.Random

/**
 * Minijuego de Tarjetas de Amor
 * 
 * TIPOS DE CARTAS:
 * 1. Preguntas Románticas: "¿Qué es lo que más te gusta de mí?"
 * 2. Retos: "Manda una foto haciendo cara linda"
 * 3. Mensajes: "Eres lo mejor que me ha pasado"
 * 4. Recuerdos: "¿Recuerdas nuestra primera cita?"
 * 5. Deseos: "Te deseo un abrazo de 1 minuto"
 */

data class LoveCard(
    val type: CardType,
    val title: String,
    val content: String,
    val color: Color
)

enum class CardType {
    QUESTION,   // Pregunta romántica
    CHALLENGE,  // Reto
    MESSAGE,    // Mensaje de amor
    MEMORY,     // Recuerdo
    WISH        // Deseo
}

object LoveCardsDeck {
    
    private val questions = listOf(
        "¿Qué es lo que más te gusta de mí?",
        "¿Cuándo te diste cuenta de que me amabas?",
        "¿Cuál es tu recuerdo favorito conmigo?",
        "¿Qué te hago que te enamora más?",
        "¿Cómo describirías nuestro amor en 3 palabras?",
        "¿Qué es lo primero que pensaste cuando me conociste?",
        "¿Cuál es mi cualidad que más admiras?",
        "¿Qué canción te recuerda a nosotros?",
        "¿Qué es lo que más extrañas cuando no estamos juntos?",
        "¿Cómo te imaginas nuestro futuro?"
    )
    
    private val challenges = listOf(
        "Manda una foto haciendo cara linda 📸",
        "Dime 3 cosas que te gustan de mí en voz alta 🎤",
        "Escribe un poema corto sobre nosotros ✍️",
        "Manda un audio diciendo 'te amo' de forma creativa 💕",
        "Haz un dibujo de nosotros y mándalo 🎨",
        "Baila nuestra canción y graba un video 💃",
        "Dime un apodo nuevo para mí 🥰",
        "Manda una foto de cuando nos conocimos 📷",
        "Escribe 5 razones por las que me amas 💌",
        "Canta una canción romántica para mí 🎵"
    )
    
    private val messages = listOf(
        "Eres lo mejor que me ha pasado en la vida 💕",
        "Te amo más que ayer y menos que mañana 💖",
        "Eres mi persona favorita en el mundo 🌎",
        "Contigo todo es más bonito 🌸",
        "Eres mi sueño hecho realidad ✨",
        "Me haces la persona más feliz 🥰",
        "Eres mi lugar seguro 🏠",
        "Contigo quiero envejecer 👵👴",
        "Eres mi casualidad favorita 🍀",
        "Te elegiría una y mil veces más 💫"
    )
    
    private val memories = listOf(
        "¿Recuerdas nuestra primera cita? 💕",
        "¿Recuerdas la primera vez que nos dijimos 'te amo'? 💌",
        "¿Recuerdas nuestro primer beso? 💋",
        "¿Recuerdas nuestra primera foto juntos? 📸",
        "¿Recuerdas el día que nos conocimos? ✨",
        "¿Recuerdas nuestro viaje más especial? 🚗",
        "¿Recuerdas nuestra canción especial? 🎵",
        "¿Recuerdas la primera vez que nos abrazamos? 🤗"
    )
    
    private val wishes = listOf(
        "Te deseo un abrazo de 1 minuto 🤗",
        "Te deseo 5 besos ahora 💋",
        "Te deseo una cita sorpresa pronto 🎁",
        "Te deseo que me cuentes un secreto 💭",
        "Te deseo 10 minutos de masajes 💆",
        "Te deseo ver tu película favorita juntos 🎬",
        "Te deseo cocinar juntos esta semana 👨‍🍳",
        "Te deseo un día entero solo para nosotros 📅"
    )
    
    fun drawRandomCard(): LoveCard {
        val type = CardType.values().random()
        
        return when (type) {
            CardType.QUESTION -> {
                LoveCard(
                    type = type,
                    title = "❓ Pregunta Romántica",
                    content = questions.random(),
                    color = Color(0xFFFF6B9D)
                )
            }
            CardType.CHALLENGE -> {
                LoveCard(
                    type = type,
                    title = "🎯 Reto",
                    content = challenges.random(),
                    color = Color(0xFFFFB347)
                )
            }
            CardType.MESSAGE -> {
                LoveCard(
                    type = type,
                    title = "💕 Mensaje de Amor",
                    content = messages.random(),
                    color = Color(0xFFFF6B6B)
                )
            }
            CardType.MEMORY -> {
                LoveCard(
                    type = type,
                    title = "💭 Recuerdo",
                    content = memories.random(),
                    color = Color(0xFF95E1D3)
                )
            }
            CardType.WISH -> {
                LoveCard(
                    type = type,
                    title = "🎁 Deseo",
                    content = wishes.random(),
                    color = Color(0xFFDDA0DD)
                )
            }
        }
    }
}

@Composable
fun LoveCardsGame(
    modifier: Modifier = Modifier,
    onCardDrawn: (LoveCard) -> Unit = {}
) {
    var currentCard by remember { mutableStateOf<LoveCard?>(null) }
    var isDrawing by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "🎴 Tarjetas de Amor 💕",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B6B)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Descubre preguntas, retos y mensajes especiales",
            fontSize = 14.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Carta actual
        if (currentCard != null) {
            LoveCardDisplay(
                card = currentCard!!,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        } else {
            // Mazo de cartas
            CardDeckPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Botón de robar carta
        Button(
            onClick = {
                isDrawing = true
                currentCard = LoveCardsDeck.drawRandomCard()
                onCardDrawn(currentCard!!)
                isDrawing = false
            },
            enabled = !isDrawing,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B6B)
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (currentCard == null) "Robar Carta" else "Robar Otra Carta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (currentCard != null) {
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botón de compartir
            OutlinedButton(
                onClick = {
                    // Compartir carta
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Compartir con mi pareja")
            }
        }
    }
}

@Composable
fun LoveCardDisplay(
    card: LoveCard,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        card.color.copy(alpha = 0.3f),
                        card.color.copy(alpha = 0.8f),
                        card.color
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Tipo de carta
            Surface(
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = card.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
            
            // Contenido
            Text(
                text = card.content,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            // Icono decorativo
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(48.dp)
            )
        }
    }
}

@Composable
fun CardDeckPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFFF0F5))
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null,
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(80.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Mazo de Amor",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Presiona el botón para robar una carta",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
