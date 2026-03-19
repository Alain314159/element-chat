# WelcomeScreen y Onboarding Romántico - Cerlita

## 📋 Resumen

Implementación del **WelcomeScreen** y **Onboarding Romántico** de 3 pantallas para la app Cerlita, usando Jetpack Compose y Lottie animations.

---

## 📁 Archivos Creados

### 1. Recursos (Strings)
- `/vector/src/main/res/values/strings-romantic-welcome.xml` - Strings en inglés
- `/vector/src/main/res/values-es/strings-romantic-welcome.xml` - Strings en español

### 2. WelcomeScreen
- `/vector/src/main/java/im/vector/app/features/romantic/ui/screens/welcome/WelcomeScreen.kt`
- `/vector/src/main/java/im/vector/app/features/romantic/ui/screens/welcome/WelcomeActivity.kt`

### 3. Onboarding
- `/vector/src/main/java/im/vector/app/features/romantic/onboarding/RomanticOnboardingViewModel.kt`
- `/vector/src/main/java/im/vector/app/features/romantic/onboarding/RomanticOnboardingActivity.kt`
- `/vector/src/main/java/im/vector/app/features/romantic/onboarding/RomanticOnboardingScreen1.kt` - Conecta
- `/vector/src/main/java/im/vector/app/features/romantic/onboarding/RomanticOnboardingScreen2.kt` - Comparte
- `/vector/src/main/java/im/vector/app/features/romantic/onboarding/RomanticOnboardingScreen3.kt` - Hagan crecer

### 4. AndroidManifest
- Registradas `WelcomeActivity` y `RomanticOnboardingActivity`

---

## 🎨 Implementación

### WelcomeScreen

**Características:**
- Background con gradiente rosa-amarillo
- Animación Lottie (placeholder: 🐷💕🐨 si no existe el archivo)
- Título: "Cerdita 💕"
- Subtítulo: "Chat romántico para ustedes dos"
- Botón "Comenzar" que navega al Onboarding

**Animación Lottie requerida:**
- `res/raw/anim_welcome.raw` - Cerdita + Koalita saludando (3s loop)

### Onboarding (3 pantallas con ViewPager2)

**Pantalla 1: Conecta con tu pareja**
- Gradiente: Rosa suave → Rosa → Amarillo
- Placeholder: 🐷💕🐨
- Descripción: Chat privado para la pareja

**Pantalla 2: Comparte momentos**
- Gradiente: Lavanda → Rosa → Dorado
- Placeholder: 💕✨
- Features: Abrazos, besos, efectos

**Pantalla 3: Hagan crecer su amor**
- Gradiente: Verde → Verde claro → Rosa
- Placeholder: 🌳💕
- Features: Logros, recuerdos, árbol

**Navegación:**
- Botón "Saltar" (páginas 1-2)
- Botón "Siguiente" / "Comenzar"
- Indicadores de página (puntos)

---

## 💾 Persistencia con DataStore

### Claves de preferencias:
```kotlin
OnboardingPreferencesKeys.ONBOARDING_COMPLETED  // boolean
OnboardingPreferencesKeys.ONBOARDING_VERSION    // boolean
```

### Flujo de guardado:
1. Usuario completa las 3 pantallas O presiona "Comenzar"
2. `completeOnboarding()` guarda `true` en DataStore
3. Próxima apertura: verifica si `ONBOARDING_COMPLETED == true`
4. Si está completo, salta directamente al RomanticHub

### Funciones de utilidad:
```kotlin
suspend fun isOnboardingCompleted(context: Context): Boolean
fun onboardingStatusFlow(context: Context): Flow<Boolean>
```

---

## 🎬 Animaciones Lottie

### Animaciones requeridas (crear en `res/raw/`):

| Archivo | Descripción | Placeholder |
|---------|-------------|-------------|
| `anim_welcome.raw` | Cerdita + Koalita saludando | 🐷💕🐨 |
| `anim_onboarding_connect.raw` | Cerdita + Koalita abrazándose | 💕 |
| `anim_onboarding_share.raw` | Corazón con efectos | ✨ |
| `anim_onboarding_growth.raw` | Árbol creciendo | 🌳 |

### Cómo obtener animaciones:
1. **LottieFiles.com** - Buscar animaciones gratis
2. **After Effects + Bodymovin** - Crear personalizadas
3. **Contratar animador** - Para Cerdita/Koalita específicos

### Placeholder implementation:
Cada pantalla tiene una versión `*Placeholder()` que usa emojis si la animación no existe.

---

## 🧭 Navegación

```
WelcomeActivity (launcher)
    ↓ (presiona "Comenzar")
RomanticOnboardingActivity
    ↓ (completa 3 pantallas)
RomanticHubActivity
```

### WelcomeActivity:
- Verifica `EXTRA_SKIP_WELCOME`
- Si es `true`, salta directo a Onboarding
- Si es `false`, muestra WelcomeScreen

### RomanticOnboardingActivity:
- Usa `HorizontalPager` (Compose ViewPager2)
- 3 páginas con navegación horizontal
- Botones: Saltar, Siguiente, Comenzar
- Indicadores visuales (puntos)

---

## 🎨 Temas y Colores

### Gradientes por pantalla:

**Welcome:**
```kotlin
Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFFB6C1), // Rosa cerdita
        Color(0xFFFFFACD), // Amarillo suave
        Color(0xFFFFE4B5)  // Amarillo intenso
    )
)
```

**Onboarding 1:**
```kotlin
colors = listOf(Color(0xFFFFE4E1), Color(0xFFFFB6C1), Color(0xFFFFFACD))
```

**Onboarding 2:**
```kotlin
colors = listOf(Color(0xFFFFF0F5), Color(0xFFFFB6C1), Color(0xFFFFD700))
```

**Onboarding 3:**
```kotlin
colors = listOf(Color(0xFFF0FFF0), Color(0xFF90EE90), Color(0xFFFFB6C1))
```

---

## 📱 Integración con MainActivity

### Opción 1: WelcomeActivity como launcher (actual)
```xml
<activity
    android:name=".features.romantic.ui.screens.welcome.WelcomeActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### Opción 2: Desde MainActivity (si ya hay login Matrix)
```kotlin
// En MainActivity.kt, después de verificar login
if (romanticOnboardingCompleted) {
    startActivity(RomanticHubActivity.newIntent(this))
} else {
    startActivity(WelcomeActivity.newIntent(this))
}
```

---

## 🧪 Testing

### Resetear onboarding para testing:
```kotlin
// En RomanticOnboardingViewModel
fun resetOnboarding() {
    viewModelScope.launch {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
```

### Verificar estado:
```kotlin
val completed = isOnboardingCompleted(context)
```

---

## 📦 Dependencias Requeridas

Ya instaladas en el proyecto:
```kotlin
// Lottie Compose
implementation("com.airbnb.lottie:lottie-compose:6.4.0")

// DataStore Preferences
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Jetpack Compose (ya incluido)
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
```

---

## ✅ Checklist de Implementación

- [x] WelcomeScreen.kt con Compose
- [x] WelcomeActivity.kt contenedora
- [x] RomanticOnboardingActivity.kt con ViewPager2
- [x] RomanticOnboardingScreen1.kt (Conecta)
- [x] RomanticOnboardingScreen2.kt (Comparte)
- [x] RomanticOnboardingScreen3.kt (Hagan crecer)
- [x] RomanticOnboardingViewModel.kt con DataStore
- [x] Strings en inglés y español
- [x] Registro en AndroidManifest
- [x] Placeholders para animaciones faltantes
- [x] Gradientes románticos personalizados
- [x] Navegación con botones Saltar/Siguiente/Comenzar
- [x] Indicadores de página (puntos)

---

## 🔧 Próximos Pasos Sugeridos

1. **Crear animaciones Lottie reales** para reemplazar placeholders
2. **Agregar animaciones de transición** entre pantallas
3. **Añadir opción "No mostrar de nuevo"** en settings
4. **Implementar onboarding condicional** según versión
5. **Agregar analytics** para tracking de conversión
6. **Soporte para múltiples idiomas** adicionales

---

## 📝 Notas

- **Estado actual:** Implementación completa con placeholders
- **Animaciones:** Usan emojis hasta que se creen los archivos `.raw`
- **Tema:** Usa `Theme.Vector.Romantic` (ya existente)
- **Idioma:** Strings en inglés y español incluidos
- **Accesibilidad:** Se pueden añadir `contentDescription` a elementos

---

**Fecha de implementación:** Marzo 2026
**Versión:** 1.0.0
**Estado:** ✅ Completado
