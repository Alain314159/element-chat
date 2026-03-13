/*
 * Copyright 2024 Cerdita App
 * 
 * Minijuegos para Parejas
 * Memory, Quiz y Juegos de Conexión
 */

package im.vector.app.features.romantic.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

/**
 * Juego de Memory para Parejas
 * Encuentra las parejas de cartas
 */
@Composable
fun CoupleMemoryGame(
    onGameComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var cards by remember { mutableStateOf(generateMemoryCards()) }
    var flippedCards by remember { mutableStateOf<List<Int>>(emptyList()) }
    var matchedPairs by remember { mutableStateOf(0) }
    var moves by remember { mutableStateOf(0) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎴 Memory de Pareja",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B6B)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Movimientos: $moves | Parejas: $matchedPairs/8",
            fontSize = 14.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cards.size) { index ->
                MemoryCard(
                    card = cards[index],
                    isFlipped = flippedCards.contains(index),
                    onClick = {
                        if (flippedCards.size < 2 && !flippedCards.contains(index)) {
                            flippedCards = flippedCards + index
                            moves++
                            
                            if (flippedCards.size == 2) {
                                val firstCard = cards[flippedCards[0]]
                                val secondCard = cards[flippedCards[1]]
                                
                                if (firstCard.value == secondCard.value) {
                                    matchedPairs++
                                    flippedCards = emptyList()
                                    
                                    if (matchedPairs == 8) {
                                        onGameComplete(moves)
                                    }
                                } else {
                                    // Voltear después de un delay
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

data class MemoryCard(
    val id: Int,
    val value: String,
    val isMatched: Boolean = false
)

@Composable
fun MemoryCard(
    card: MemoryCard,
    isFlipped: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(
                if (isFlipped) Color(0xFFFFB6C1) else Color(0xFFFF6B6B)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isFlipped) {
            Text(
                text = card.value,
                fontSize = 32.sp
            )
        } else {
            Text(
                text = "❓",
                fontSize = 24.sp
            )
        }
    }
}

fun generateMemoryCards(): List<MemoryCard> {
    val emojis = listOf("💕", "💋", "🤗", "💌", "🌹", "🍫", "🧸", "💍")
    val pairs = emojis.flatMap { listOf(it, it) }.shuffled()
    
    return pairs.mapIndexed { index, value ->
        MemoryCard(id = index, value = value)
    }
}

/**
 * Quiz de Pareja
 * Preguntas para conocerse mejor
 */
@Composable
fun CoupleQuiz(
    onComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember { getCoupleQuizQuestions() }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    
    val currentQuestion = questions[currentQuestionIndex]
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "❓ Quiz de Pareja",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B6B)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Pregunta ${currentQuestionIndex + 1}/${questions.size}",
            fontSize = 14.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = currentQuestion.question,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        currentQuestion.options.forEach { option ->
            Button(
                onClick = {
                    if (!answered) {
                        if (option == currentQuestion.correctAnswer) {
                            score++
                        }
                        answered = true
                    }
                    
                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                        answered = false
                    } else {
                        onComplete(score)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB6C1)
                )
            ) {
                Text(option, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

fun getCoupleQuizQuestions(): List<QuizQuestion> {
    return listOf(
        QuizQuestion(
            question = "¿Cuál es nuestra canción especial?",
            options = listOf("Opción 1", "Opción 2", "Opción 3", "Opción 4"),
            correctAnswer = "Opción 1"
        ),
        QuizQuestion(
            question = "¿Dónde fue nuestra primera cita?",
            options = listOf("Opción 1", "Opción 2", "Opción 3", "Opción 4"),
            correctAnswer = "Opción 1"
        ),
        QuizQuestion(
            question = "¿Cuál es mi comida favorita?",
            options = listOf("Opción 1", "Opción 2", "Opción 3", "Opción 4"),
            correctAnswer = "Opción 1"
        )
    )
}

/**
 * Juego de Conexión - Completar frases
 */
@Composable
fun ConnectionGame(
    onComplete: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var phrase by remember { mutableStateOf("") }
    var userAnswer by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "💕 Completa la Frase",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B6B)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Contigo todo es más...",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Tu respuesta") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                isCorrect = userAnswer.contains("bonito", ignoreCase = true) ||
                           userAnswer.contains("mejor", ignoreCase = true) ||
                           userAnswer.contains("feliz", ignoreCase = true)
                
                if (isCorrect == true) {
                    onComplete(true)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B6B)
            )
        ) {
            Text("Comprobar", color = Color.White)
        }
        
        if (isCorrect != null) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (isCorrect == true) "¡Correcto! 💕" else "Inténtalo de nuevo ❤️",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCorrect == true) Color(0xFF32CD32) else Color(0xFFFF6B6B)
            )
        }
    }
}
