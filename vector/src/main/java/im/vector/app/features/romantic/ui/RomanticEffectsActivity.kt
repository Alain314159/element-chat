/*
 * Copyright 2024 Cerdita App
 *
 * Pantalla de Configuración de Efectos Románticos - Jetpack Compose
 */

package im.vector.app.features.romantic.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Pantalla principal de configuración de efectos románticos
 * 
 * Características:
 * - Lista de efectos disponibles
 * - Toggle para activar/desactivar cada efecto
 * - Slider para controlar intensidad
 * - Botones de prueba para vista previa
 * - Guardado automático de preferencias
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RomanticEffectsScreen(
    onBackClick: () -> Unit,
    viewModel: RomanticEffectsViewModel = viewModel { RomanticEffectsViewModel(androidx.compose.ui.platform.LocalContext.current) }
) {
    val state by viewModel.state.collectAsState()
    val effectLaunched = remember { mutableStateOf<RomanticEffectType?>(null) }

    // Escuchar eventos del ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RomanticEffectsEvent.TestEffect -> {
                    effectLaunched.value = event.effectType
                }
                is RomanticEffectsEvent.SettingsSaved -> {
                    // Mostrar snackbar de confirmación
                }
                is RomanticEffectsEvent.Error -> {
                    // Mostrar error
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "✨ Efectos Románticos",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Switch global para activar/desactivar efectos
                item {
                    GlobalEffectsToggle(
                        enabled = state.effectsEnabled,
                        onToggle = { viewModel.toggleGlobalEffects(it) }
                    )
                }

                // Slider de intensidad
                item {
                    EffectIntensitySlider(
                        intensity = state.effectIntensity,
                        enabled = state.effectsEnabled,
                        onIntensityChange = { viewModel.updateEffectIntensity(it) }
                    )
                }

                // Lista de efectos disponibles
                item {
                    Text(
                        text = "Efectos Disponibles",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Lluvia de corazones
                item {
                    EffectCard(
                        effectType = RomanticEffectType.HEARTS,
                        enabled = state.heartRainEnabled && state.effectsEnabled,
                        onToggle = { viewModel.toggleEffect(RomanticEffectType.HEARTS, it) },
                        onTest = { viewModel.testEffect(RomanticEffectType.HEARTS) },
                        canTest = state.effectsEnabled
                    )
                }

                // Besos voladores
                item {
                    EffectCard(
                        effectType = RomanticEffectType.KISS,
                        enabled = state.kissEffectEnabled && state.effectsEnabled,
                        onToggle = { viewModel.toggleEffect(RomanticEffectType.KISS, it) },
                        onTest = { viewModel.testEffect(RomanticEffectType.KISS) },
                        canTest = state.effectsEnabled
                    )
                }

                // Flores bloom
                item {
                    EffectCard(
                        effectType = RomanticEffectType.FLOWERS,
                        enabled = state.flowerEffectEnabled && state.effectsEnabled,
                        onToggle = { viewModel.toggleEffect(RomanticEffectType.FLOWERS, it) },
                        onTest = { viewModel.testEffect(RomanticEffectType.FLOWERS) },
                        canTest = state.effectsEnabled
                    )
                }

                // Confeti romántico
                item {
                    EffectCard(
                        effectType = RomanticEffectType.CONFETTI,
                        enabled = state.confettiEffectEnabled && state.effectsEnabled,
                        onToggle = { viewModel.toggleEffect(RomanticEffectType.CONFETTI, it) },
                        onTest = { viewModel.testEffect(RomanticEffectType.CONFETTI) },
                        canTest = state.effectsEnabled
                    )
                }

                // Espaciador final
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // Botón de restablecer
                item {
                    OutlinedButton(
                        onClick = { viewModel.resetToDefaults() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFFF6B6B)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF6B6B), Color(0xFFFFB6C1))
                            )
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Restablecer valores por defecto")
                    }
                }
            }

            // Overlay de efecto de prueba
            effectLaunched.value?.let { effectType ->
                TestEffectOverlay(
                    effectType = effectType,
                    onFinished = { effectLaunched.value = null }
                )
            }
        }
    }
}

/**
 * Tarjeta global para activar/desactivar todos los efectos
 */
@Composable
fun GlobalEffectsToggle(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFFF0F5),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Activar efectos románticos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Muestra corazones, besos y efectos especiales al detectar palabras románticas",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFFFB6C1),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFCCCCCC)
                )
            )
        }
    }
}

/**
 * Slider para controlar la intensidad de los efectos
 */
@Composable
fun EffectIntensitySlider(
    intensity: Int,
    enabled: Boolean,
    onIntensityChange: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFFF0F5),
        shape = RoundedCornerShape(16.dp)
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Intensidad",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                }
                Text(
                    text = "$intensity%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B6B)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Slider(
                value = intensity.toFloat(),
                onValueChange = { onIntensityChange(it.toInt()) },
                enabled = enabled,
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFF6B6B),
                    activeTrackColor = Color(0xFFFFB6C1),
                    inactiveTrackColor = Color(0xFFCCCCCC)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Suave",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Intenso",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * Tarjeta individual para cada efecto
 */
@Composable
fun EffectCard(
    effectType: RomanticEffectType,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onTest: () -> Unit,
    canTest: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFFF0F5),
        shape = RoundedCornerShape(16.dp)
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
                Row(modifier = Modifier.weight(1f)) {
                    Text(
                        text = effectType.icon,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = effectType.displayName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = effectType.description,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = onToggle,
                    enabled = canTest,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFFB6C1)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onTest,
                enabled = canTest && enabled,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (enabled) Color(0xFFFF6B6B) else Color.Gray,
                    disabledContainerColor = Color(0xFFCCCCCC)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Probar efecto")
            }
        }
    }
}

/**
 * Overlay para mostrar efecto de prueba
 */
@Composable
fun TestEffectOverlay(
    effectType: RomanticEffectType,
    onFinished: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        visible = false
        kotlinx.coroutines.delay(500)
        onFinished()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = effectType.icon,
                    fontSize = 80.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Vista previa: ${effectType.displayName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
