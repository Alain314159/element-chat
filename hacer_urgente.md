# 🚨 HACER URGENTE - MEJORAS, OPTIMIZACIONES Y FEATURES ADICIONALES PARA CERLITA



---

## 1. 🎨 MEJORAS DE UI/UX FALTANTES

### 1.1 Animaciones y Transiciones

| Mejora | Descripción | Impacto | Dificultad | Estado |
|--------|-------------|---------|------------|--------|
| **Skeleton loaders** | Mostrar esqueleto mientras carga contenido | Medio | Baja | ❌ NO IMPLEMENTADO |

### 1.2 Personalización Visual

| Mejora | Descripción | Impacto | Dificultad | Estado |
|--------|-------------|---------|------------|--------|
| **Burbujas de chat personalizables** | Cada usuario elige color/forma de sus bubbles | Alto | Baja | ❌ NO IMPLEMENTADO |
| **Fuentes personalizables** | 5-10 fuentes románticas para elegir | Medio | Baja | ❌ NO IMPLEMENTADO |

---

## 2. ⚡ OPTIMIZACIONES DE RENDIMIENTO FALTANTES

### 2.1 Velocidad y Memoria

| Optimización | Descripción | Mejora | Dificultad | Estado |
|--------------|-------------|--------|------------|--------|
| **Compose compiler metrics** | Optimizar recomposiciones | 20% más fluido | Media | ❌ NO IMPLEMENTADO |
| **Baseline profiles** | Mejorar tiempo de inicio | 40% más rápido | Media | ❌ NO IMPLEMENTADO |

---

## 3. 💕 FEATURES ROMÁNTICOS FALTANTES

### 3.1 Conexión Emocional

| Feature | Descripción | Impacto | Dificultad | Estado |
|---------|-------------|---------|------------|--------|
| **Mapa de distancia** | Mostrar distancia entre ustedes (opcional, con privacidad) | Medio | Media | ❌ NO IMPLEMENTADO |

### 3.2 Recuerdos y Historial

| Feature | Descripción | Impacto | Dificultad | Estado |
|---------|-------------|---------|------------|--------|
| **Nube de palabras de la relación** | Palabras más usadas entre ustedes | Medio | Media | ❌ NO IMPLEMENTADO |
| **Estadísticas de amor avanzadas** | Mensajes enviados, días juntos, etc. (más allá del contador básico) | Alto | Baja | ❌ NO IMPLEMENTADO |
| **Registro de primeras veces** | Registrar y celebrar primeras veces (primer beso, viaje, etc.) de forma explícita | Alto | Baja | ❌ NO IMPLEMENTADO |

### 3.3 Sorpresas y Detalles

| Feature | Descripción | Impacto | Dificultad | Estado |
|---------|-------------|---------|------------|--------|
| **Regalos virtuales** | Enviar regalos animados (flores, chocolates, etc.) - sistema separado de cupones | Alto | Media | ❌ NO IMPLEMENTADO |
| **Quiz de la relación** | Preguntas sobre cuánto se conocen | Alto | Media | ❌ NO IMPLEMENTADO |

---

## 4. 🎮 GAMIFICACIÓN FALTANTE

| Elemento | Descripción | Impacto | Dificultad | Estado |
|----------|-------------|---------|------------|--------|
| **Jardín de la relación** | Plantar flores virtuales que crecen juntas | Alto | Media | ❌ NO IMPLEMENTADO |

---

## 5. 🔧 MEJORAS DE ARQUITECTURA TÉCNICA FALTANTES

| Mejora | Descripción | Impacto | Dificultad | Estado |
|--------|-------------|---------|------------|--------|
| **GraphQL** | Para queries más eficientes a Matrix | Medio | Alta | ❌ NO IMPLEMENTADO |
| **CDN para medios** | Servir imágenes/videos desde CDN | Alto | Media | ❌ NO IMPLEMENTADO |
| **A/B testing framework** | Probar diferentes UI/features | Medio | Media | ❌ NO IMPLEMENTADO |

---

## 6. 🆕 FEATURES COMPLETAMENTE NUEVOS FALTANTES

```
┌─────────────────────────────────────────────────────────────────────────┐
│  FEATURES QUE NO ESTÁN EN EL PLAN ORIGINAL                            │
├─────────────────────────────────────────────────────────────────────────┤
│  🎮 MINI JUEGOS PARA PAREJA                                             │
│      Juegos de 2 jugadores dentro de la app                            │
│      Impacto: 🔥🔥🔥  Dificultad: Alta  ❌ NO IMPLEMENTADO            │
│                                                                         │
│  🎨 LIENZO COMPARTIDO                                                   │
│       Dibujar juntos en tiempo real en un canvas                       │
│       Impacto: 🔥🔥🔥  Dificultad: Alta  ❌ NO IMPLEMENTADO            │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 7. 📦 DEPENDENCIAS ADICIONALES SUGERIDAS (FALTANTES)

```kotlin
// build.gradle.kts - Dependencias que FALTAN instalar

// Accompanist (utilidades Compose) - FALTA
implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")
implementation("com.google.accompanist:accompanist-permissions:0.36.0")

// Chucker (debug de red) - FALTA
debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")

// Para Baseline Profiles - FALTA
implementation("androidx.profileinstaller:profileinstaller:1.3.1")

// Para A/B Testing - FALTA
implementation("com.google.firebase:firebase-abt:21.1.1")

// Para GraphQL - FALTA
implementation("com.apollographql.apollo3:apollo-runtime:3.8.2")

// Google Maps (si se requiere específicamente para mapa de distancia) - FALTA
implementation("com.google.maps.android:maps-compose:4.3.0")
implementation("com.google.android.gms:play-services-maps:18.2.0")
```

### Dependencias YA INSTALADAS (no es necesario añadirlas):
- ✅ Lottie Compose 6.4.0
- ✅ MapLibre (alternativa a Google Maps)
- ✅ DataStore Preferences 1.0.0
- ✅ Glide 4.16.0
- ✅ Room 2.6.1
- ✅ Hilt 2.51.1
- ✅ Rust Crypto SDK 26.1.28

---

## 8. 📋 RESUMEN DE PRIORIDADES

### 🔥 ALTA PRIORIDAD (Impacto Alto, Dificultad Baja/Media)
1. **Burbujas de chat personalizables** - Impacto Alto, Dificultad Baja
2. **Estadísticas de amor avanzadas** - Impacto Alto, Dificultad Baja
3. **Registro de primeras veces** - Impacto Alto, Dificultad Baja
4. **Skeleton loaders** - Impacto Medio, Dificultad Baja

### ⚡ MEDIA PRIORIDAD (Impacto Medio, Dificultad Media)
1. **Mapa de distancia** - Requiere integrar Google Maps
2. **Nube de palabras de la relación**
3. **Quiz de la relación**
4. **Jardín de la relación**

### 🏗️ BAJA PRIORIDAD (Alta Dificultad o Impacto Medio)
1. **Compose compiler metrics** - Requiere configuración de build
2. **Baseline profiles** - Requiere análisis de rendimiento
3. **GraphQL** - Cambio arquitectónico mayor
4. **CDN para medios** - Requiere infraestructura
5. **A/B testing framework** - Requiere Firebase
6. **Mini juegos para pareja** - Alta complejidad
7. **Lienzo compartido** - Alta complejidad técnica

---

## 10. 🔧 MEJORAS PARA FEATURES YA IMPLEMENTADAS

Estas features **YA ESTÁN IMPLEMENTADAS** pero se pueden **MEJORAR** significativamente:

### 10.1 Botón de Abrazo Mágico (`HugButton.kt`)

**Estado actual:** 4 tipos de abrazos (NORMAL, LONG, TIGHT, SPIN) con animaciones Lottie

**Mejoras sugeridas:**

| Mejora | Descripción | Impacto | Dificultad |
|--------|-------------|---------|------------|
|
| **Reacción a abrazos** | Permitir reaccionar al abrazo recibido (❤️, 🥰, 😊) | Alto | Baja |

### 10.2 Mascotas (`MascotSystem.kt`)

**Estado actual:** Cerdita y Koalita con 10 estados de ánimo, niveles y XP

**Mejoras sugeridas:**

| Mejora | Descripción | Impacto | Dificultad |
|--------|-------------|---------|------------|
| **Personalización de mascotas** | Ropa, accesorios, colores personalizables | 🔥 Alto | Media |
| **Interacción entre mascotas** | Las mascotas interactúan entre sí cuando ambos usuarios tienen | Alto | Alta |
| **Mascotas dormidas** | La mascota se duerme en horario nocturno real | Medio | Baja |
| **Comida para mascotas** | Alimentar mascota con monedas ganadas en juegos | Alto | Media |
|
| **Mascota bebé** | Evolución de mascota cuando la relación cumple hitos | 🔥 Alto | Media |

**Cómo implementar:**
```kotlin
// En MascotSystem.kt, añadir:
data class MascotOutfit(
    val id: String,
    val name: String,
    val type: OutfitType, // HAT, SHIRT, ACCESSORY
    val rarity: Rarity,   // COMMON, RARE, LEGENDARY
    val unlockCondition: UnlockCondition
)

// Sistema de inventario
data class MascotInventory(
    val ownedOutfits: List<MascotOutfit>,
    val equippedOutfit: MascotOutfit?
)
```

---

### 10.3 Árbol de la Relación (`RelationshipTree.kt`)

**Estado actual:** 6 niveles de árbol con hitos básicos

**Mejoras sugeridas:**

| Mejora | Descripción | Impacto | Dificultad |
|--------|-------------|---------|------------|
| **Hitos personalizables** | Permitir crear hitos personalizados con foto | 🔥 Alto | Baja |
| **Árbol 3D interactivo** | Renderizado 3D del árbol que se puede rotar | Alto | Alta |
| **Estaciones del año** | El árbol cambia según estación del año real | Medio | Media |
| **Frutos del árbol** | Cada fruto es un recuerdo (foto, mensaje, audio) | Alto | Media |
| **Árbol de la pareja gemelo** | Ambos usuarios ven el mismo árbol sincronizado | Alto | Media |
| **Decoraciones festivas** | El árbol se decora en Navidad, aniversarios, etc. | Medio | Baja |

**Cómo implementar:**
```kotlin
// En RelationshipTree.kt, añadir:
data class CustomMilestone(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val photoUri: String?,
    val audioNoteUri: String?,
    val location: Location?,
    val tags: List<String>
)

// Sincronización con Matrix
suspend fun syncMilestoneWithPartner(milestone: CustomMilestone) {
    matrixRepository.sendEncryptedMilestone(milestone)
}
```

---


---

### 10.5 Cupones de Amor (`LoveCoupons.kt`)

**Estado actual:** 6 categorías, 30+ cupones predefinidos

**Mejoras sugeridas:**

| Mejora | Descripción | Impacto | Dificultad |
|--------|-------------|---------|------------|
| **Cupones personalizables** | Crear cupones personalizados con condiciones | 🔥 Alto | Baja |
| **Cupones con foto/video** | Adjuntar multimedia al cupón | Alto | Baja |
| **Cupones expirables** | Fecha de vencimiento opcional | Medio | Baja |
| **Sistema de subasta** | Subastar cupones especiales con monedas | Medio | Media |
| **Cupones colaborativos** | Cupones que requieren que ambos completen | Alto | Media |
| **Historial de redención** | Ver cupones redimidos con fecha y foto del momento | Medio | Baja |

**Cómo implementar:**
```kotlin
// En LoveCoupons.kt, añadir:
data class CustomCoupon(
    val id: String,
    val title: String,
    val description: String,
    val conditions: List<String>,
    val expirationDate: LocalDate?,
    val mediaAttachments: List<String>, // URIs
    val redemptionCount: Int, // Cuántas veces se puede redimir
    val isTransferable: Boolean
)
```

---

### 10.6 Notas de Amor (`LoveNotes.kt`)

**Estado actual:** 9 tipos de notas, programables, con contraseña

**Mejoras sugeridas:**

| Mejora | Descripción | Impacto | Dificultad |
|--------|-------------|---------|------------|
| **Notas con ubicación** | Se desbloquean al llegar a lugar especial | 🔥 Alto | Media |
| **Notas colaborativas** | Ambos pueden escribir en la misma nota | Alto | Media |
| **Sobre sorpresa** | Nota que se autodestruye después de leer | Medio | Baja |
| **Notas con realidad aumentada** | Escanear objeto para ver nota | Alto | Alta |
| **Playlist adjunta** | Adjuntar canción de Spotify/YouTube | Alto | Baja |
| **Calendario de notas** | Ver notas del mes en vista de calendario | Medio | Baja |

**Cómo implementar:**
```kotlin
// En LoveNotes.kt, añadir:
data class LocationTrigger(
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Float,
    val triggerOnce: Boolean
)

data class CollaborativeNote(
    val id: String,
    val contributions: List<NoteContribution>,
    val isLocked: Boolean,
    val unlockCondition: NoteUnlockCondition
)

data class NoteContribution(
    val authorId: String,
    val content: String,
    val timestamp: Long,
    val mediaAttachments: List<String>
)
```

---

### 10.7 Detector de Palabras Románticas (`RomanticWordDetector.kt`)

**Estado actual:** 9 categorías de palabras con efectos visuales

**Mejoras sugeridas:**

| Mejora | Descripción | Impacto | Dificultad |
|--------|-------------|---------|------------|
| **Palabras personalizables** | Usuario añade sus propias palabras románticas | Alto | Baja |
| **Efectos en audio** | Detectar "te amo" en mensajes de voz (speech-to-text) | 🔥 Alto | Alta |
| **Frecuencia de palabras** | Estadísticas de palabras románticas más usadas | Medio | Baja |
| **Efectos combinados** | Múltiples efectos si hay varias palabras | Medio | Media |
| **Idiomas múltiples** | Soporte para inglés, portugués, etc. | Medio | Media |
| **Momento del día** | Efectos diferentes según hora (amanecer/noche) | Medio | Baja |

**Cómo implementar:**
```kotlin
// En RomanticWordDetector.kt, añadir:
data class CustomRomanticWord(
    val word: String,
    val effectType: RomanticEffectType,
    val isCaseSensitive: Boolean,
    val language: String
)

// Speech-to-text para audios
suspend fun detectRomanticWordsInAudio(audioUri: String): List<DetectedWord> {
    val transcript = speechRecognizer.transcribe(audioUri)
    return detectWords(transcript)
}
```

---

### 10.8 Cápsulas del Tiempo (`TimeCapsules.kt`)

**Estado actual:** Mensajes programados para abrir en fecha futura

**Mejoras sugeridas:**

| Mejora | Descripción | Impacto | Dificultad |
|--------|-------------|---------|------------|
| **Cápsula colaborativa** | Ambos escriben, se abre cuando ambos completan | 🔥 Alto | Media |
| **Cápsula con ubicación** | Se desbloquea en lugar específico | Alto | Media |
| **Cápsula de voz/video** | Grabar mensaje de audio/video para el futuro | Alto | Baja |
| **Recordatorio de apertura** | Notificación días antes de abrir | Medio | Baja |
| **Cápsula sorpresa** | No revela contenido hasta que se abre | Medio | Baja |
| **Legado de cápsulas** | Cápsula que se abre en aniversario específico | Alto | Baja |

**Cómo implementar:**
```kotlin
// En TimeCapsules.kt, añadir:
data class CollaborativeCapsule(
    val id: String,
    val creatorContribution: CapsuleContribution,
    val partnerContribution: CapsuleContribution?,
    val isOpenedByCreator: Boolean,
    val isOpenedByPartner: Boolean,
    val unlockDate: LocalDateTime
)

data class CapsuleContribution(
    val text: String?,
    val audioUri: String?,
    val videoUri: String?,
    val photoUris: List<String>,
    val contributionDate: LocalDateTime
)
```

---

### 10.9 Efectos Visuales Románticos (`RomanticEffects.kt`)

**Estado actual:** Lluvia de corazones, besos voladores, flores

**Mejoras sugeridas:**

| Mejora | Descripción | Impacto | Dificultad |
|--------|-------------|---------|------------|
| **Efectos interactivos** | Tocar corazones para explotarlos | Alto | Media |
| **Efectos en pantalla completa** | Ocupar toda la pantalla del chat | Alto | Baja |
| **Efectos 3D** | Corazones con profundidad 3D | Medio | Media |
| **Efectos de sonido** | Sonido romántico sincronizado con efecto | Alto | Baja |
| **Efectos personalizados** | Usuario crea su propio efecto | Medio | Alta |
| **Efectos por logro** | Efecto especial al alcanzar logro | Medio | Baja |

**Cómo implementar:**
```kotlin
// En RomanticEffects.kt, añadir:
data class InteractiveEffect(
    val effectType: RomanticEffectType,
    val isInteractive: Boolean,
    val onTap: () -> Unit,
    val soundEffect: Int?, // Resource ID
    val durationMillis: Long
)

// Efecto en pantalla completa
@Composable
fun FullScreenRomanticEffect(
    effectType: RomanticEffectType,
    onEffectComplete: () -> Unit
) {
    // Canvas en pantalla completa con efectos
}
```


---

## 11. 📋 VERIFICACIÓN DE COMPONENTES ADICIONALES

### 11.1 Componentes que NO existen en el proyecto

Los siguientes componentes **NO ESTÁN IMPLEMENTADOS** y se deben crear:

#### ❌ WelcomeScreen (NO IMPLEMENTADO)

**Estado:** No existe archivo `WelcomeScreen.kt` en el proyecto

**Ubicación sugerida:** `vector/src/main/java/im/vector/app/features/romantic/ui/screens/welcome/WelcomeScreen.kt`

**Especificación:**
```kotlin
// Componente requerido:
- Animación Lottie de Cerdita + Koalita saludando (3s loop)
- Logo de la app
- Título: "Cerdita 💕"
- Subtítulo: "Chat romántico para ustedes dos"
- Botón: "Comenzar"
- Background: Gradiente rosa-amarillo
```

**Cómo implementar:**
```kotlin
package im.vector.app.features.romantic.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
fun WelcomeScreen(
    onBeginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_welcome)
    )
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFB6C1), // Rosa
                        Color(0xFFFFFACD)  // Amarillo
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Implementar UI
    }
}
```

**Animación requerida:** `anim_welcome.raw` (no existe)

---

#### ❌ Animaciones Lottie Faltantes

**Estado actual:** Solo existen 8 animaciones:
- ✅ `anim_heart_large.raw`
- ✅ `anim_heart_small.raw`
- ✅ `anim_hug_normal.raw`
- ✅ `anim_hug_long.raw`
- ✅ `anim_hug_tight.raw`
- ✅ `anim_hug_spin.raw`
- ✅ `anim_kiss_normal.raw`
- ✅ `anim_kiss_passionate.raw`

**Animaciones FALTANTES (12+):**

| Animación | Descripción | Uso | Prioridad |
|-----------|-------------|-----|-----------|
| `anim_welcome.raw` | Cerdita + Koalita saludando | WelcomeScreen | 🔥 Alta |
| `anim_loading.raw` | Loading spinner con corazón | Pantallas de carga | 🔥 Alta |
| `anim_success.raw` | Check exitoso | Confirmaciones | 🔥 Alta |
| `anim_error.raw` | Error shake | Errores | 🔥 Alta |
| `anim_confetti.raw` | Confeti cayendo | Cumpleaños, logros | 🔥 Alta |
| `anim_flowers_bloom.raw` | Flores floreciendo | Efecto "gracias" | Media |
| `anim_sunrise.raw` | Amanecer | Efecto "buenos días" | Media |
| `anim_moon.raw` | Luna brillante | Efecto "buenas noches" | Media |
| `anim_stars_sparkle.raw` | Estrellas brillando | Efecto cumplidos | Media |
| `anim_pig_jump.raw` | Cerdita saltando | Mascota feliz | Baja |
| `anim_pig_sleep.raw` | Cerdita durmiendo | Mascota dormida | Baja |
| `anim_koala_swing.raw` | Koalita balanceándose | Mascota relajada | Baja |
| `anim_koala_eat.raw` | Koalita comiendo | Mascota alimentándose | Baja |
| `anim_notification.raw` | Campana sonando | Notificaciones | Media |

**Cómo obtener:**
1. **LottieFiles.com** - Buscar animaciones gratis y convertir a `.raw`
2. **After Effects + Bodymovin** - Crear animaciones personalizadas
3. **Contratar animador** - Para animaciones específicas de Cerdita/Koalita

---

#### ❌ Sistema de Temas y Colores (NO IMPLEMENTADO)

**Estado:** No existen archivos `Color.kt` ni `Theme.kt` en el módulo romántico

**Ubicación sugerida:** `vector/src/main/java/im/vector/app/features/romantic/ui/theme/`

**Archivos a crear:**

1. **`Color.kt`** - Colores del tema Cerdita:
```kotlin
package im.vector.app.features.romantic.ui.theme

import androidx.compose.ui.graphics.Color

// Colores principales
val PigPink = Color(0xFFFFB6C1)        // Rosa cerdita
val KoalaGray = Color(0xFF778899)      // Gris koalita
val LoveRed = Color(0xFFFF6B6B)        // Rojo amor
val SoftYellow = Color(0xFFFFFACD)     // Amarillo suave
val MintGreen = Color(0xFF98FF98)      // Verde menta
val Lavender = Color(0xFFE6E6FA)       // Lavanda

// Fondos
val BackgroundLight = Color(0xFFFFF5F7)
val BackgroundDark = Color(0xFF1A1A2E)

// Texto
val TextPrimaryLight = Color(0xFF1A1A2E)
val TextPrimaryDark = Color(0xFFF5F5F5)

// Énfasis
val AccentPink = Color(0xFFFF85A2)
val AccentYellow = Color(0xFFFFE66D)
val AccentCoral = Color(0xFFFF7F7F)
```

2. **`Theme.kt`** - Tema Material 3:
```kotlin
package im.vector.app.features.romantic.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PigPink,
    onPrimary = Color.White,
    secondary = KoalaGray,
    tertiary = LoveRed,
    background = BackgroundLight,
    // ... más colores
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentPink,
    // ... más colores
)

@Composable
fun CerditaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

**Mejora para colores existentes:**
Los colores actuales en `library/ui-styles/src/main/res/values/colors.xml` son genéricos. Se recomienda:
- Mantener colores base de Element/Matrix
- Añadir colores románticos específicos en módulo romántico
- Usar `Color.kt` para componentes Compose románticos

---

#### ❌ Iconos Románticos Específicos (PARCIALMENTE IMPLEMENTADOS)

**Estado:** El proyecto tiene 356+ iconos XML, pero **NO** tiene los iconos románticos específicos:

| Icono | Estado | Ubicación sugerida |
|-------|--------|-------------------|
| `ic_hug.xml` | ❌ No existe | `vector/src/main/res/drawable/` |
| `ic_sticker.xml` | ❌ No existe | `vector/src/main/res/drawable/` |
| `ic_heart.xml` | ❌ No existe (solo en verification) | `vector/src/main/res/drawable/` |
| `ic_cerdita.xml` | ❌ No existe | `vector/src/main/res/drawable/` |
| `ic_koalita.xml` | ❌ No existe | `vector/src/main/res/drawable/` |
| `ic_romantic_hub.xml` | ❌ No existe | `vector/src/main/res/drawable/` |

**Iconos que SÍ existen y se pueden usar:**
- ✅ `ic_verification_heart.xml` - Corazón (se puede reutilizar)
- ✅ `ic_verification_pig.xml` - Cerdita (se puede reutilizar)
- ✅ `ic_attachment_sticker.xml` - Sticker (similar)
- ✅ `ic_notification.png` - Notificación genérico

**Cómo crear iconos faltantes:**
1. Usar **Android Studio Icon Generator**
2. Importar de **Material Icons** (https://fonts.google.com/icons)
3. Crear vectoriales personalizados en **Figma/Illustrator**

---

#### ❌ Onboarding Romántico (NO IMPLEMENTADO)

**Estado:** Existe onboarding genérico de Matrix en `features/onboarding/` pero **NO** hay onboarding romántico específico

**Ubicación sugerida:** `vector/src/main/java/im/vector/app/features/romantic/onboarding/`

**Pantallas a crear:**
1. `RomanticOnboardingActivity.kt` - Actividad contenedora
2. `RomanticOnboardingScreen1.kt` - "Conecta con tu pareja"
3. `RomanticOnboardingScreen2.kt` - "Comparte momentos especiales"
4. `RomanticOnboardingScreen3.kt` - "Jueguen juntos"
5. `RomanticOnboardingViewModel.kt` - ViewModel

**Especificación:**
```kotlin
// 3 pantallas de onboarding romántico:
// 1. Icono: Cerdita + Koalita abrazándose
//    Título: "Conecta con tu pareja"
//    Descripción: "Chat privado solo para ustedes dos"

// 2. Icono: Corazón con efectos
//    Título: "Comparte momentos"
//    Descripción: "Envía abrazos, besos y efectos románticos"

// 3. Icono: Árbol creciendo
//    Título: "Hagan crecer su amor"
//    Descripción: "Desbloquea logros y crea recuerdos juntos"
```

---

### 11.2 Componentes que SÍ existen y cómo mejorarlos



#### ✅ RomanticWordDetector (YA IMPLEMENTADO)

**Estado actual:** Implementado en `vector/src/main/java/im/vector/app/features/romantic/RomanticWordDetector.kt`

**Lo que tiene:**
- ✅ 9 categorías de palabras románticas
- ✅ Efectos asociados a cada categoría
- ✅ Funciones `detectCategory()`, `hasRomanticEffect()`, `getEffectType()`

**Mejoras sugeridas:**

| Mejora | Código de ejemplo | Prioridad |
|--------|-------------------|-----------|
| **Palabras personalizables** | Añadir `addCustomWord(category, word)` con DataStore | 🔥 Alta |
| **Soporte multilenguaje** | Añadir categorías en inglés/portugués | Media |
| **Expresiones regulares** | Usar Regex para coincidencias más flexibles | Media |
| **Frecuencia de palabras** | Contador de palabras más usadas | Baja |
| **Detección en audio** | Integrar speech-to-text para mensajes de voz | Alta |

**Ejemplo de implementación (Palabras personalizables):**
```kotlin
// En RomanticWordDetector.kt, añadir:
private const val CUSTOM_WORDS_KEY = "custom_romantic_words"

fun addCustomWord(context: Context, category: String, word: String) {
    val prefs = context.getSharedPreferences("romantic_settings", Context.MODE_PRIVATE)
    val customWords = prefs.getString(CUSTOM_WORDS_KEY, "")?.split("|")?.toMutableList() ?: mutableListOf()
    customWords.add("$category:$word")
    prefs.edit().putString(CUSTOM_WORDS_KEY, customWords.joinToString("|")).apply()
}

fun loadCustomWords(context: Context) {
    // Cargar y añadir a categories
}
```

---

#### ✅ RomanticEffects (YA IMPLEMENTADO)

**Estado actual:** Implementado en `vector/src/main/java/im/vector/app/features/romantic/ui/RomanticEffects.kt`

**Directorio de efectos vacío:** `vector/src/main/java/im/vector/app/features/romantic/ui/effects/` (sin archivos)

**Lo que probablemente tiene:** (basado en estructura)
- Efectos de corazones, besos, flores, etc.

**Mejoras sugeridas:**

| Mejora | Código de ejemplo | Prioridad |
|--------|-------------------|-----------|
| **Efectos interactivos** | `onClick: () -> Unit` en cada partícula | 🔥 Alta |
| **Efectos de sonido** | `SoundPool` para sonidos sincronizados | 🔥 Alta |
| **Pantalla completa** | `FullScreenRomanticEffect` composable | Media |
| **Efectos 3D** | Usar `androidx.graphics` para profundidad | Media |
| **Efectos personalizados** | Builder pattern para crear efectos | Baja |

---

#### ✅ RelationshipTree (YA IMPLEMENTADO)

**Estado actual:** Implementado en `vector/src/main/java/im/vector/app/features/romantic/ui/RelationshipTree.kt`

**Lo que tiene:**
- ✅ 6 niveles de árbol
- ✅ Sistema de hitos (milestones)
- ✅ Visualización en Canvas

**Mejoras sugeridas:**

| Mejora | Código de ejemplo | Prioridad |
|--------|-------------------|-----------|
| **Hitos con fotos** | Añadir `photoUri: String?` a `RelationshipMilestone` | 🔥 Alta |
| **Árbol sincronizado** | Sync con Matrix para que ambos vean el mismo | 🔥 Alta |
| **Estaciones del año** | Cambiar colores según estación real | Media |
| **Frutos interactivos** | Click en frutos para ver recuerdos | Media |
| **Decoraciones festivas** | Añadir adornos en fechas especiales | Baja |

**Ejemplo de implementación (Hitos con fotos):**
```kotlin
// En RelationshipTree.kt, modificar:
data class RelationshipMilestone(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val type: MilestoneType,
    val icon: String,
    val photoUri: String? = null,      // NUEVO
    val audioNoteUri: String? = null,  // NUEVO
    val tags: List<String> = emptyList() // NUEVO
)
```

---

#### ✅ MascotSystem (YA IMPLEMENTADO)

**Estado actual:** Implementado en `vector/src/main/java/im/vector/app/features/romantic/ui/mascots/MascotSystem.kt`

**Lo que tiene:**
- ✅ 2 tipos de mascotas (Cerdita, Koalita)
- ✅ 10 estados de ánimo
- ✅ Sistema de niveles y XP

**Mejoras sugeridas:**

| Mejora | Código de ejemplo | Prioridad |
|--------|-------------------|-----------|
| **Ropa y accesorios** | `data class MascotOutfit` con inventario | 🔥 Alta |
| **Interacción entre mascotas** | Sync con Matrix para mascotas de pareja | 🔥 Alta |
| **Comida para mascotas** | Sistema de alimentación con monedas | Media |

| **Mascota bebé** | Evolución por hitos de relación | Alta |
| **Mascotas dormidas** | Estado SLEEPY en horario nocturno | Baja |

**Ejemplo de implementación (Ropa y accesorios):**
```kotlin
// En MascotSystem.kt, añadir:
enum class OutfitType { HAT, SHIRT, ACCESSORY, SHOES }
enum class Rarity { COMMON, RARE, EPIC, LEGENDARY }

data class MascotOutfit(
    val id: String,
    val name: String,
    val type: OutfitType,
    val rarity: Rarity,
    val unlockCondition: UnlockCondition,
    val drawableResId: Int
)

data class MascotInventory(
    val ownedOutfits: List<MascotOutfit> = emptyList(),
    val equippedOutfit: MascotOutfit? = null
)
```

---

#### ✅ LoveCoupons (YA IMPLEMENTADO)

**Estado actual:** Implementado en `vector/src/main/java/im/vector/app/features/romantic/features/coupons/LoveCoupons.kt`

**Lo que tiene:**
- ✅ 6 categorías (ROMANTIC, SWEET, SERVICE, ADVENTURE, SPICY, INTIMATE)
- ✅ 30+ cupones predefinidos
- ✅ Sistema de recompensas XP

**Mejoras sugeridas:**

| Mejora | Código de ejemplo | Prioridad |
|--------|-------------------|-----------|
| **Cupones personalizables** | `data class CustomCoupon` con creador | 🔥 Alta |
| **Cupones con multimedia** | Añadir `photoUris: List<String>` | 🔥 Alta |
| **Fecha de vencimiento** | Añadir `expirationDate: LocalDate?` | Media |
| **Historial de redención** | Guardar cuándo se redimió cada cupón | Media |
| **Cupones colaborativos** | Ambos deben completar para redimir | Alta |

**Ejemplo de implementación (Cupones personalizables):**
```kotlin
// En LoveCoupons.kt, añadir:
data class CustomCoupon(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val conditions: List<String> = emptyList(),
    val expirationDate: LocalDate? = null,
    val mediaAttachments: List<String> = emptyList(), // URIs de fotos/videos
    val redemptionCount: Int = 1, // Cuántas veces se puede redimir
    val isTransferable: Boolean = true,
    val creatorId: String,
    val createdDate: LocalDateTime = LocalDateTime.now()
)
```

---

#### ✅ LoveNotes (YA IMPLEMENTADO)

**Estado actual:** Implementado en `vector/src/main/java/im/vector/app/features/romantic/features/LoveNotes.kt`

**Lo que tiene:**
- ✅ 9 tipos de notas
- ✅ Programación de entrega
- ✅ Notas con contraseña

**Mejoras sugeridas:**

| Mejora | Código de ejemplo | Prioridad |
|--------|-------------------|-----------|
| **Desbloqueo por ubicación** | `data class LocationTrigger` con geofencing | 🔥 Alta |
| **Notas colaborativas** | Múltiples `NoteContribution` | 🔥 Alta |
| **Playlist adjunta** | Añadir `spotifyUri: String?` o `youtubeId: String?` | Media |
| **Calendario de notas** | Vista de calendario con notas del mes | Media |
| **Notas que se autodestruyen** | `destroyAfterRead: Boolean` | Baja |

**Ejemplo de implementación (Desbloqueo por ubicación):**
```kotlin
// En LoveNotes.kt, añadir:
data class LocationTrigger(
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Float = 100f,
    val triggerOnce: Boolean = true,
    val locationName: String = ""
)

data class LoveNote(
    // ... campos existentes
    val locationTrigger: LocationTrigger? = null, // NUEVO
    val spotifyTrackId: String? = null,           // NUEVO
    val youtubeVideoId: String? = null            // NUEVO
)

// Usar FusedLocationProviderClient para detectar cuando usuario entra en radio
```

---

#### ✅ TimeCapsules (YA IMPLEMENTADO)

**Estado actual:** Implementado en `vector/src/main/java/im/vector/app/features/romantic/features/TimeCapsules.kt`

**Lo que tiene:**
- ✅ Mensajes programados para fecha futura
- ✅ UI de cápsulas bloqueadas/desbloqueadas

**Mejoras sugeridas:**

| Mejora | Código de ejemplo | Prioridad |
|--------|-------------------|-----------|
| **Cápsula colaborativa** | Ambos escriben, se abre cuando ambos completan | 🔥 Alta |
| **Cápsula con ubicación** | Se desbloquea en lugar específico | 🔥 Alta |
| **Cápsula de voz/video** | Grabar audio/video para el futuro | Alta |
| **Recordatorio de apertura** | Notificación días antes | Media |
| **Legado de cápsulas** | Cápsula para aniversario específico | Baja |

**Ejemplo de implementación (Cápsula colaborativa):**
```kotlin
// En TimeCapsules.kt, añadir:
data class CapsuleContribution(
    val authorId: String,
    val text: String?,
    val audioUri: String?,
    val videoUri: String?,
    val photoUris: List<String> = emptyList(),
    val contributionDate: LocalDateTime = LocalDateTime.now()
)

data class CollaborativeCapsule(
    val id: String = UUID.randomUUID().toString(),
    val creatorContribution: CapsuleContribution,
    val partnerContribution: CapsuleContribution? = null,
    val isOpenedByCreator: Boolean = false,
    val isOpenedByPartner: Boolean = false,
    val unlockDate: LocalDateTime,
    val title: String,
    val description: String
)
```

---

#### ✅ AffectionCounter (YA IMPLEMENTADO)

**Estado actual:** Implementado en `vector/src/main/java/im/vector/app/features/romantic/games/AffectionCounter.kt`

**Lo que tiene:**
- ✅ Contador de besos y abrazos
- ✅ Sistema de logros
- ✅ Rachas y puntos XP

**Mejoras sugeridas:**

| Mejora | Código de ejemplo | Prioridad |
|--------|-------------------|-----------|
| **Tabla de clasificación** | Ver quién da más besos/abrazos | 🔥 Alta |
| **Metas semanales/mensuales** | `data class AffectionGoal` con período | 🔥 Alta |
| **Power-ups** | Beso doble, abrazo triple por tiempo | 🔥 Alta |
| **Eventos especiales** | "Día del beso" - 2x XP los viernes | Media |
| **Registro emocional** | ¿Cómo te sentiste después? | Media |

**Ejemplo de implementación (Tabla de clasificación):**
```kotlin
// En AffectionCounter.kt, añadir:
data class LeaderboardEntry(
    val userId: String,
    val userName: String,
    val kissesSent: Int,
    val hugsSent: Int,
    val totalPoints: Int
)

fun getLeaderboard(context: Context): List<LeaderboardEntry> {
    // Obtener stats de ambos usuarios y ordenar
}

// UI:
@Composable
fun LeaderboardCard(leaderboard: List<LeaderboardEntry>) {
    // Mostrar tabla con posiciones
}
```

---

### 13.1 Estado Actual del Proyecto

```
✅ LO QUE YA ESTÁ COMPLETO (60%)

┌──────────────────────┬─────────────────────────────────────────────────────┬─────────┐
│ Componente           │ Archivos                                            │ Estado  │
├──────────────────────┼─────────────────────────────────────────────────────┼─────────┤
│ Core Romántico       │ RomanticWordDetector.kt, RelationshipDaysManager.kt │ ✅ 100% │
│ Base de Datos Room   │ 24 entidades, DAOs, Repository                      │ ✅ 100% │
│ UI Components        │ 15 componentes Compose                              │ ✅ 80%  │
│ Juegos               │ 5 juegos implementados                              │ ✅ 70%  │
│ Animaciones Lottie   │ 7 animaciones .raw                                  │ ✅ 100% │
│ Dependency Injection │ Módulos Hilt                                        │ ✅ 90%  │
└──────────────────────┴─────────────────────────────────────────────────────┴─────────┘
```

### 13.2 Lo que Está a Medias

- [ ] **Pantallas del Hub** - 7 TODOs pendientes de implementar
- [ ] **ViewModels** - Faltan ViewModels para cada pantalla
- [ ] **Strings** - Solo existen IDs, faltan textos en EN/ES
- [ ] **Iconos** - Faltan drawable icons para features

### 13.3 Lo que Falta (Crítico)

- [ ] **Integración con el Chat** - Botón, detector, efectos en Timeline
- [ ] **Activities** - Registrar en AndroidManifest
- [ ] **Sincronización Matrix** - Eventos personalizados para pareja
- [ ] **Testing** - 0 tests escritos
- [ ] **Documentación técnica** - README específico del módulo

---

### 13.4 Fases de Implementación

#### FASE 1: INTEGRACIÓN BÁSICA (1 semana) 🔴 PRIORIDAD MÁXIMA

**Objetivo:** Que el botón romántico aparezca en el chat y los efectos funcionen

**Tareas:**
1. Añadir FloatingActionButton en `RoomDetailActivity.kt`
2. Integrar `RomanticWordDetector` en `TimelineViewModel.kt`
3. Mostrar efectos visuales en `TimelineFragment.kt`
4. Registrar `RomanticSettingsActivity` en `AndroidManifest.xml`
5. Crear strings básicos en `values/strings.xml` y `values-es/strings.xml`

**Archivos a modificar:**
- `vector/src/main/java/im/vector/app/features/home/room/detail/RoomDetailActivity.kt`
- `vector/src/main/java/im/vector/app/features/home/room/detail/TimelineViewModel.kt`
- `vector/src/main/java/im/vector/app/features/home/room/detail/TimelineFragment.kt`
- `vector/src/main/AndroidManifest.xml`

---

#### FASE 2: PANTALLAS COMPLETAS (2 semanas) 🟡 PRIORIDAD ALTA

**Objetivo:** Implementar las 7 pantallas con TODO

**Tareas:**
1. Pantalla de Días Juntos (completar TODO línea 54)
2. Pantalla de Notas de Amor (completar TODO línea 63)
3. Pantalla de Regalos (completar TODO línea 72)
4. Pantalla de Desafíos (completar TODO línea 81)
5. Pantalla de Cápsulas del Tiempo (completar TODO línea 90)
6. Pantalla de Settings (completar TODO línea 99)
7. Pantalla de Detalle de Álbum (completar TODO línea 115)

**Archivos a crear por pantalla:**
- `XxxScreen.kt` (ya existe el placeholder)
- `XxxViewModel.kt` (nuevo)
- `XxxActivity.kt` o `XxxFragment.kt` (nuevo)

---

#### FASE 3: RECURSOS (1 semana) 🟡 PRIORIDAD ALTA

**Objetivo:** Tener todos los recursos necesarios

**Tareas:**
1. Crear `values/strings_romantic.xml` con 50+ strings en inglés
2. Crear `values-es/strings_romantic.xml` con traducciones
3. Crear 10+ iconos drawable
4. Crear 6 animaciones Lottie adicionales (amanecer, luna, confeti, etc.)

---

#### FASE 4: SINCRONIZACIÓN MATRIX (2-3 semanas) 🟢 PRIORIDAD MEDIA

**Objetivo:** Que besos/abrazos/notas se sincronicen entre la pareja

**Tareas:**
1. Crear servicio de sincronización
2. Definir eventos personalizados de Matrix
3. Implementar envío/recepción de eventos románticos
4. Sincronizar contadores y logros

---

#### FASE 5: TESTING (1 semana) 🟢 PRIORIDAD MEDIA

**Tareas:**
1. Tests unitarios para `RomanticWordDetector`
2. Tests unitarios para `RelationshipDaysManager`
3. Tests de UI para componentes Compose
4. Tests de integración para Room

---

#### FASE 6: DOCUMENTACIÓN (3 días) 🔵 PRIORIDAD BAJA

**Tareas:**
1. README del módulo romántico
2. Actualizar README principal
3. Guía de implementación

---

### 13.5 Cronograma Total

```
┌─────────────────────────────────────────────────────────────────────────┐
│  CRONOGRAMA ESTIMADO - 7-8 SEMANAS                                      │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  Semana 1-2:  FASE 1 - Integración Básica                              │
│  Semana 3-4:  FASE 2 - Pantallas Completas                             │
│  Semana 5:    FASE 3 - Recursos                                        │
│  Semana 6-7:  FASE 4 - Sincronización Matrix                           │
│  Semana 8:    FASE 5 - Testing + FASE 6 - Documentación                │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

**Última actualización:** 17 de marzo de 2026
**Proyecto:** CERLITA (`/data/data/com.termux/files/home/cerlita`)
