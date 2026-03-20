# 📚 Documentación de Funcionalidades Románticas - Cerlita

Este documento describe todas las funcionalidades románticas implementadas en Cerlita, un cliente Matrix para parejas basado en Element Android.

---

## 📋 Índice

1. [Arquitectura General](#arquitectura-general)
2. [Funcionalidades Principales](#funcionalidades-principales)
3. [Base de Datos](#base-de-datos)
4. [UI Components](#ui-components)
5. [Juegos para Parejas](#juegos-para-parejas)
6. [Features Románticos](#features-románticos)
7. [Navegación](#navegación)
8. [Recursos](#recursos)

---

## 🏗️ Arquitectura General

El módulo romántico está organizado en la siguiente estructura:

```
vector/src/main/java/im/vector/app/features/romantic/
├── data/                    # Capa de datos
│   ├── dao/                 # Data Access Objects (Room)
│   ├── database/            # Configuración de base de datos
│   └── repository/          # Repositorios de datos
├── di/                      # Inyección de dependencias
├── features/                # Funcionalidades románticas
│   ├── coupons/             # Cupones de amor
│   ├── extras/              # Funcionalidades adicionales
│   └── ...                  # Otras features
├── games/                   # Juegos para parejas
│   └── coupons/             # Recompensas de juegos
├── milestones/              # Registro de hitos importantes
├── navigation/              # Navegación entre pantallas
├── notifications/           # Servicio de notificaciones
├── settings/                # Configuración romántica
├── stats/                   # Estadísticas de amor
├── ui/                      # Componentes de interfaz
│   ├── components/          # Componentes reutilizables
│   ├── mascots/             # Sistema de mascotas
│   ├── screens/             # Pantallas principales
│   └── themes/              # Temas y estilos
├── RomanticWordDetector.kt  # Detector de palabras románticas
└── RelationshipDaysManager.kt  # Gestor de días de relación
```

---

## 💕 Funcionalidades Principales

### 1. Detector de Palabras Románticas

**Archivo:** `RomanticWordDetector.kt`

Detecta automáticamente palabras románticas en los mensajes y activa efectos visuales.

**Categorías disponibles:**

| Categoría | Palabras Ejemplo | Efecto |
|-----------|-----------------|--------|
| 💕 Amor | "te amo", "te quiero", "mi vida" | Lluvia de corazones |
| 💋 Besos | "besos", "muacks", "kisses" | Besos voladores |
| 🌅 Buenos Días | "buenos días", "feliz día" | Amanecer |
| 🌙 Buenas Noches | "buenas noches", "dulces sueños" | Luna |
| 🎉 Cumpleaños | "feliz cumpleaños" | Confeti |
| ☁️ Extrañar | "te extraño", "me haces falta" | Nubes |
| 🌸 Gracias | "gracias", "mil gracias" | Flores |
| 🤗 Abrazo | "abrazo", "abrazame", "apapacho" | Abrazo animado |
| ✨ Cumplidos | "eres hermoso", "me encantas" | Brillos |

**Uso:**
```kotlin
// Detectar categoría de palabra romántica
val category = RomanticWordDetector.detectCategory("Te amo mucho")
if (category != null) {
    // Activar efecto visual correspondiente
    triggerRomanticEffect(category.effectType)
}

// Verificar si un mensaje tiene efecto romántico
if (RomanticWordDetector.hasRomanticEffect(message)) {
    // El mensaje contiene palabras románticas
}
```

---

### 2. Gestor de Días de Relación

**Archivo:** `RelationshipDaysManager.kt`

Persiste y calcula el tiempo transcurrido desde el inicio de la relación.

**Características:**
- Almacena fecha de inicio en SharedPreferences
- Calcula días, meses y años transcurridos
- Detecta automáticamente aniversarios
- Formato legible: "1 año, 2 meses y 3 días juntos 💕"

**Uso:**
```kotlin
val daysManager = RelationshipDaysManager(context)

// Configurar fecha de inicio
daysManager.setStartDate(LocalDate.of(2024, 1, 15))

// Obtener días transcurridos
val daysText = daysManager.getDaysTogetherText()

// Verificar si es aniversario
if (daysManager.isAnniversary()) {
    // Mostrar mensaje especial
}
```

---

### 3. Botón de Abrazos (HugButton)

**Archivos:** 
- `ui/HugButton.kt` - Componente Compose
- `ui/HugButtonView.kt` - Wrapper Android View
- `ui/HugButtonViewModel.kt` - ViewModel

**Tipos de abrazos:**

| Tipo | Mensaje | Animación |
|------|---------|-----------|
| NORMAL | "Te envío un abrazo 🐷🤗🐨" | anim_hug_normal.raw |
| LONG | "Te mando un abrazo bien largo 💕" | anim_hug_long.raw |
| TIGHT | "Te abrazo muy fuerte 🤗💖" | anim_hug_tight.raw |
| SPIN | "¡Abrazo con giro! 🐷🤗🐨💫" | anim_hug_spin.raw |

**Características:**
- Integrado en RoomDetailActivity
- Envía mensajes automáticos al chat
- Registra abrazos en base de datos
- Sistema de reacciones (❤️, 🥰, 😊)
- Animaciones Lottie

---

### 4. Efectos Visuales Románticos

**Archivos:**
- `ui/RomanticEffects.kt` - Efectos visuales
- `ui/RomanticEffectsActivity.kt` - Pantalla de efectos
- `ui/RomanticEffectViewModel.kt` - ViewModel

**Efectos disponibles:**
- HeartRainEffect - Lluvia de corazones
- FlyingKissEffect - Beso volador con trayectoria parabólica
- FlowerBloomEffect - Flores que florecen

**Activación:** Se activan automáticamente cuando RomanticWordDetector detecta palabras románticas.

---

## 🗄️ Base de Datos

### Configuración

**Archivo:** `data/database/RomanticDatabase.kt`

- Motor: Room Database
- Versión actual: 4
- Migraciones: MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4

### Entidades (33 tablas)

#### Entidades Principales

| Entidad | Tabla | Descripción |
|---------|-------|-------------|
| `HugEntity` | hugs | Registro de abrazos enviados/recibidos |
| `HugReactionEntity` | hug_reactions | Reacciones a abrazos |
| `RelationshipDayEntity` | relationship_days | Días de relación |
| `MascotEntity` | mascots | Estado de mascotas |
| `MascotOutfitEntity` | mascot_outfits | Outfits de mascotas |
| `MascotInventoryEntity` | mascot_inventory | Inventario equipado |

#### Entidades de Juegos

| Entidad | Tabla | Descripción |
|---------|-------|-------------|
| `AffectionCounterEntity` | affection_counters | Contador de besos/abrazos |
| `AffectionAchievementEntity` | affection_achievements | Logros de afecto |
| `LoveCardEntity` | love_cards | Cartas del juego |
| `CouponRewardEntity` | coupon_rewards | Recompensas de cupones |

#### Entidades de Features

| Entidad | Tabla | Descripción |
|---------|-------|-------------|
| `LoveNoteEntity` | love_notes | Notas de amor |
| `LocationTriggerEntity` | location_triggers | Triggers de ubicación |
| `CollaborativeNoteEntity` | collaborative_notes | Notas colaborativas |
| `NoteContributionEntity` | note_contributions | Contribuciones a notas |
| `TimeCapsuleEntity` | time_capsules | Cápsulas del tiempo |
| `CustomCouponEntity` | custom_coupons | Cupones personalizados |
| `CouponRedemptionHistoryEntity` | coupon_redemption_history | Historial de redención |
| `CustomMilestoneEntity` | custom_milestones | Hitos personalizados |
| `FirstTimeEntity` | first_times | Registro de primeras veces |

### DAO

**Archivo:** `data/dao/RomanticDao.kt`

Más de 60 métodos para operaciones CRUD:
- Inserción, actualización, eliminación
- Queries con Flow para observación reactiva
- Métodos específicos por entidad

### Repositorio

**Archivo:** `data/repository/RomanticRepository.kt`

Proporciona una API unificada para acceder a datos románticos:
- Operaciones suspendidas
- Flujos de datos reactivos
- Lógica de negocio centralizada

---

## 🎨 UI Components

### Componentes Principales

#### WelcomeScreen
**Archivo:** `ui/screens/welcome/WelcomeScreen.kt`

Pantalla de bienvenida con:
- Animación Lottie (placeholder 🐷💕🐨)
- Logo y título de la app
- Botón "Comenzar"
- Fondo gradiente rosa-amarillo

#### RomanticOnboarding
**Archivos:** `ui/onboarding/`

3 pantallas de onboarding:
1. "Conecta con tu pareja"
2. "Comparte momentos especiales"
3. "Hagan crecer su amor"

#### RomanticHubScreen
**Archivo:** `ui/RomanticHubScreen.kt`

Menú principal de funcionalidades románticas:
- Accesos a todas las features
- Diseño en grid con iconos
- Navegación tipada con RomanticFeature

#### HugButton
**Archivos:** `ui/HugButton.kt`, `ui/HugButtonView.kt`

Botón flotante para enviar abrazos:
- 4 tipos de abrazos
- Animaciones Lottie
- Integrado en chat principal

#### RelationshipDaysCounter
**Archivo:** `ui/RelationshipDaysCounter.kt`

Widget para mostrar días de relación:
- Versión completa y compacta
- Diálogo para configurar fecha
- Mensaje especial en aniversarios

#### RelationshipTree
**Archivo:** `ui/RelationshipTree.kt`

Árbol visual que crece con la relación:
- 6 niveles de crecimiento
- Hitos automáticos
- Hitos personalizables con foto

#### MascotSystem
**Archivo:** `ui/mascots/MascotSystem.kt`

Sistema de mascotas personalizables:
- Cerdita y Koalita
- 10 estados de ánimo
- Sistema de XP y niveles
- Outfits personalizables

### Componentes Reutilizables

#### ChatBubbleCustom
**Archivo:** `ui/components/ChatBubbleCustom.kt`

Burbujas de chat personalizadas con colores románticos.

#### SkeletonLoader
**Archivo:** `ui/components/SkeletonLoader.kt`

Animación de carga tipo skeleton para contenido.

---

## 🎮 Juegos para Parejas

### 1. Contador de Afecto (AffectionCounter)

**Archivos:**
- `games/AffectionCounter.kt`
- `games/AffectionCounterActivity.kt`

**Características:**
- Contador de besos enviados/recibidos
- Contador de abrazos enviados/recibidos
- Sistema de puntos de amor (10 pts por acción)
- Rachas diarias
- 8 logros desbloqueables

**Logros:**
- 🏆 Primer Beso
- 🏆 Primer Abrazo
- 🏆 Científico de Besos (100 besos)
- 🏆 Abrazador Experto (100 abrazos)
- 🏆 Semana Romántica (7 días racha)
- 🏆 Mes de Amor (30 días racha)
- 🏆 Mil Puntos de Amor
- 🏆 Maestro del Amor (todos los logros)

---

### 2. Juego de Cartas de Amor (LoveCardsGame)

**Archivos:**
- `games/LoveCardsGame.kt`
- `games/LoveCardsGameActivity.kt`

**Tipos de cartas (50+ cartas):**
- ❓ **Preguntas Románticas**: "¿Qué es lo que más te gusta de mí?"
- 🎯 **Retos**: "Manda una foto haciendo cara linda"
- 💕 **Mensajes de Amor**: "Eres lo mejor que me ha pasado"
- 💭 **Recuerdos**: "¿Recuerdas nuestra primera cita?"
- 🎁 **Deseos**: "Te deseo un abrazo de 1 minuto"

**Características:**
- Diseño con gradientes por tipo
- Opción de compartir con la pareja
- Barajado aleatorio

---

### 3. Juegos de Pareja (CoupleGames)

**Archivo:** `games/CoupleGamesActivity.kt`

Actividad contenedora para múltiples juegos:
- Acceso a AffectionCounter
- Acceso a LoveCardsGame
- Integración de recompensas

---

## 💌 Features Románticos

### 1. Notas de Amor (LoveNotes)

**Archivos:**
- `features/LoveNotes.kt`
- `features/LoveNotesActivity.kt`

**Tipos de notas:**
- ☀️ Buenos Días
- 🌙 Buenas Noches
- 🎉 Aniversario
- 🎂 Cumpleaños
- 💪 Ánimo
- 🙏 Disculpa
- 🙏 Agradecimiento
- 🎁 Sorpresa
- 💕 Normal

**Características:**
- Programar entrega en fechas especiales
- Notas bloqueadas con contraseña/pista
- Adjuntar fotos, audios, videos
- Contador de notas sin leer
- **Notas colaborativas**: Ambos pueden contribuir
- **Desbloqueo por ubicación**: Geofencing opcional

**Entidades relacionadas:**
- `LoveNoteEntity` - Notas principales
- `LocationTriggerEntity` - Triggers de ubicación
- `CollaborativeNoteEntity` - Notas colaborativas
- `NoteContributionEntity` - Contribuciones

---

### 2. Cupones de Amor (LoveCoupons)

**Archivos:**
- `features/coupons/LoveCoupons.kt`
- `features/coupons/LoveCouponsActivity.kt`

**Categorías (6 categorías, 30+ cupones):**
- 💕 Románticos
- 🎁 Sorpresas
- 🍽️ Citas
- 💆 Masajes
- 🎬 Películas
- 🌟 Especiales

**Características:**
- Cupones personalizables
- Condiciones de uso
- Fecha de vencimiento opcional
- Múltiples redenciones
- Adjuntar multimedia
- Historial de redención

**Entidades relacionadas:**
- `CustomCouponEntity` - Cupones personalizados
- `CouponRedemptionHistoryEntity` - Historial

---

### 3. Cápsulas del Tiempo (TimeCapsules)

**Archivos:**
- `features/TimeCapsules.kt`
- `features/TimeCapsulesActivity.kt`

**Características:**
- Mensajes programados para fecha futura
- Cápsulas colaborativas (ambos contribuyen)
- Desbloqueo por ubicación opcional
- Mensajes de voz/video
- Recordatorios de apertura

**Entidades relacionadas:**
- `TimeCapsuleEntity` - Cápsulas principales

---

### 4. Registro de Primeras Veces (FirstTimesRegistry)

**Archivos:**
- `milestones/FirstTimesRegistry.kt`
- `milestones/FirstTimesRegistryActivity.kt`

**Características:**
- Registrar hitos importantes (primer beso, primer viaje, etc.)
- Añadir fotos y descripciones
- Fecha y ubicación opcionales
- Lista de primeras veces completadas

**Entidades relacionadas:**
- `FirstTimeEntity` - Registro de primeras veces

---

### 5. Estadísticas de Amor (LoveStatistics)

**Archivos:**
- `stats/LoveStatistics.kt`
- `stats/LoveStatisticsActivity.kt`

**Estadísticas disponibles:**
- Días/meses/años juntos
- Mensajes románticos enviados
- Palabras románticas más usadas
- Abrazos/besos totales
- Logros desbloqueados
- Rachas actuales

---

### 6. Sistema de Mascotas (MascotSystem)

**Archivos:**
- `ui/mascots/MascotSystem.kt`
- `ui/mascots/MascotSystemActivity.kt`

**Características:**
- 2 mascotas: Cerdita y Koalita
- 10 estados de ánimo
- Sistema de XP y niveles
- Outfits personalizables
- Inventario de accesorios
- Tienda de outfits

**Estados de ánimo:**
- Feliz, Triste, Emocionado, Hambriento, Cansado
- Sorprendido, Enamorado, Juguetón, Relajado, Avergonzado

**Entidades relacionadas:**
- `MascotEntity` - Estado de mascota
- `MascotOutfitEntity` - Outfits disponibles
- `MascotInventoryEntity` - Inventario equipado

---

### 7. Efectos Románticos (RomanticEffects)

**Archivos:**
- `ui/RomanticEffects.kt`
- `ui/RomanticEffectsActivity.kt`

**Efectos:**
- Lluvia de corazones
- Besos voladores
- Flores que florecen
- Amanecer/Atardecer
- Luna y estrellas
- Confeti
- Nubes
- Brillos

**Características:**
- Activación automática por palabras románticas
- Animaciones Lottie
- Efectos en pantalla completa opcionales

---

## 🧭 Navegación

### RomanticNavigation

**Archivo:** `navigation/RomanticNavigation.kt`

Sistema de navegación tipado para el hub romántico:

```kotlin
sealed class RomanticFeature {
    object RelationshipDays : RomanticFeature()
    object LoveNotes : RomanticFeature()
    object TimeCapsules : RomanticFeature()
    object LoveCoupons : RomanticFeature()
    object AffectionCounter : RomanticFeature()
    object LoveCardsGame : RomanticFeature()
    object RomanticEffects : RomanticFeature()
    object MascotSystem : RomanticFeature()
    object RelationshipTree : RomanticFeature()
    object FirstTimesRegistry : RomanticFeature()
    object LoveStatistics : RomanticFeature()
    object RomanticSettings : RomanticFeature()
}
```

### RomanticHubActivity

**Archivo:** `ui/RomanticHubActivity.kt`

Actividad principal que contiene el hub romántico:
- Grid de funcionalidades
- Navegación a cada feature
- Integración con RomanticHubScreen

---

## 🎁 Recursos

### Strings

**Archivos:**
- `library/ui-strings/src/main/res/values/strings-romantic.xml` (Inglés)
- `library/ui-strings/src/main/res/values-es/strings-romantic-es.xml` (Español)

**Total:** 1,094 strings (547 por idioma)

**Categorías:**
- Strings generales románticos
- Strings de bienvenida
- Strings de onboarding
- Strings de features específicas

### Colores

**Archivo:** `library/ui-styles/src/main/res/values/colors_romantic.xml`

**Colores principales:**
- PigPink (#FFFFB6C1) - Rosa cerdita
- KoalaGray (#FF778899) - Gris koalita
- LoveRed (#FFFF6B6B) - Rojo amor
- SoftYellow (#FFFFFACD) - Amarillo suave
- MintGreen (#FF98FF98) - Verde menta
- Lavender (#FFE6E6FA) - Lavanda

### Animaciones Lottie

**Archivos en:** `vector/src/main/res/raw/`

**Animaciones existentes (8):**
- anim_heart_large.raw
- anim_heart_small.raw
- anim_hug_normal.raw
- anim_hug_long.raw
- anim_hug_tight.raw
- anim_hug_spin.raw
- anim_kiss_normal.raw
- anim_kiss_passionate.raw

### Temas

**Archivo:** `ui/themes/RomanticThemes.kt`

Temas Material 3 personalizados:
- CerditaTheme (claro/oscuro)
- Colores románticos
- Tipografía personalizada

---

## 📱 Actividades Registradas

**Archivo:** `vector/src/main/AndroidManifest.xml`

13 activities románticas registradas:

1. `RomanticHubActivity` - Hub principal
2. `WelcomeActivity` - Pantalla de bienvenida
3. `RomanticOnboardingActivity` - Onboarding
4. `RomanticSettingsActivity` - Configuración
5. `LoveNotesActivity` - Notas de amor
6. `TimeCapsulesActivity` - Cápsulas del tiempo
7. `LoveCouponsActivity` - Cupones de amor
8. `AffectionCounterActivity` - Contador de afecto
9. `LoveCardsGameActivity` - Juego de cartas
10. `CoupleGamesActivity` - Juegos de pareja
11. `RomanticEffectsActivity` - Efectos visuales
12. `MascotSystemActivity` - Sistema de mascotas
13. `RelationshipTreeActivity` - Árbol de relación
14. `FirstTimesRegistryActivity` - Primeras veces
15. `LoveStatisticsActivity` - Estadísticas

---

## 🔧 Configuración Técnica

### Dependencias

El módulo romántico utiliza:
- **Jetpack Compose** - UI declarativa
- **Lottie Compose** - Animaciones
- **Room** - Base de datos local
- **Hilt** - Inyección de dependencias
- **Mavericks** - ViewModels
- **Material 3** - Componentes de diseño
- **DataStore** - Preferencias

### Permisos

**Archivo:** `vector/src/main/AndroidManifest.xml`

Permisos requeridos:
- Internet (ya existente para Matrix)
- Ubicación (opcional, para geofencing en LoveNotes)

### Migraciones Room

**Versión actual:** 4

**Migraciones:**
- MIGRATION_1_2: Entidades iniciales
- MIGRATION_2_3: Entidades de juegos
- MIGRATION_3_4: Entidades de features avanzadas

---

## 📊 Estadísticas del Módulo Romántico

| Métrica | Cantidad |
|---------|----------|
| Archivos Kotlin | 57 |
| Entidades Room | 33 |
| Métodos en DAO | 60+ |
| Strings EN/ES | 1,094 |
| Activities | 15 |
| UI Screens (Compose) | 20+ |
| ViewModels | 10+ |
| Animaciones Lottie | 8 |

---

## 📝 Notas Adicionales

### Características Eliminadas del Proyecto Base

Para crear una experiencia enfocada en parejas, se eliminaron:
- Grupos/comunidades (features/spaces/)
- Encuestas (features/poll/)
- Analytics/telemetría
- Herramientas de desarrollo
- Múltiples sesiones
- Gestión avanzada de dispositivos
- Hilos de conversación
- Idiomas: Solo Inglés y Español

### Próximas Funcionalidades (Pendientes)

- Sincronización Matrix entre parejas
- Geofencing real para LoveNotes
- Animaciones Lottie adicionales
- Tests unitarios
- Edición/eliminación en FirstTimesRegistry

---

**Última actualización:** Marzo 2026
**Versión del módulo:** 2.0.0
