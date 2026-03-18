# Integración del HugButton en RoomDetailActivity

## Resumen

El HugButton (botón de abrazos románticos) ha sido integrado exitosamente en la pantalla principal del chat de Cerlita.

## Archivos Modificados/Creados

### 1. Archivos Existentes Modificados

#### `/vector/src/main/java/im/vector/app/features/romantic/ui/HugButton.kt`
- **Cambios:** Actualizado para aceptar `isEnabled` parameter y mejorar el manejo de errores
- **Propósito:** Componente principal del botón de abrazos en Jetpack Compose

#### `/vector/src/main/res/layout/fragment_timeline.xml`
- **Cambios:** Añadido `HugButtonView` en la parte inferior central, encima del botón "jump to bottom"
- **Ubicación:** Entre `viewKonfetti` y `jumpToBottomView`
- **Visibilidad:** Inicialmente `gone`, se activa programáticamente

### 2. Archivos Nuevos Creados

#### `/vector/src/main/java/im/vector/app/features/romantic/ui/HugButtonView.kt`
- **Propósito:** Wrapper Android View para usar el HugButton (Compose) en layouts XML
- **Características:**
  - Envuelve el HugButton en un `ComposeView`
  - Expone métodos Java/Kotlin tradicionales (`setOnHugClickListener`, `setHugType`, etc.)
  - Maneja el ciclo de vida de Compose automáticamente

#### `/vector/src/main/java/im/vector/app/features/romantic/ui/HugButtonViewModel.kt`
- **Propósito:** ViewModel (Mavericks) para manejar la lógica del HugButton
- **Responsabilidades:**
  - Enviar mensajes de abrazo al chat
  - Registrar abrazos en la base de datos romántica
  - Manejar estados (Idle, Sending, Success, Error)
  - Calcular nivel de cariño basado en interacciones
- **Acciones:** `SendHug`, `ResetState`

#### `/vector/src/main/java/im/vector/app/features/home/room/detail/HugButtonIntegration.kt`
- **Propósito:** Clase de utilidad para integrar el HugButton en el TimelineFragment
- **Características:**
  - Configura visibilidad y listeners
  - Observa estados del ViewModel
  - Maneja feedback háptico
  - Proporciona métodos `show()` y `hide()`

#### `/vector/src/main/java/im/vector/app/features/home/room/detail/TimelineFragment.kt`
- **Cambios:**
  - Añadidas importaciones para clases del HugButton
  - Declarado `hugButtonViewModel` y `hugButtonIntegration`
  - Añadido método `setupHugButton()`
  - Llamada a `setupHugButton()` en `onViewCreated()`

## Cómo Funciona

### Flujo de Envío de Abrazo

```
1. Usuario presiona HugButton
   ↓
2. HugButtonView notifica a HugButtonIntegration
   ↓
3. HugButtonIntegration llama a HugButtonViewModel.handle(SendHug)
   ↓
4. ViewModel:
   - Cambia estado a "Sending"
   - Obtiene mensaje según HugType
   - Envía mensaje al chat vía Matrix SDK
   - Registra abrazo en RomanticRepository
   - Actualiza estadísticas
   - Cambia estado a "Success" o "Error"
   ↓
5. UI observa cambio de estado:
   - Éxito: Muestra animación, re-habilita botón
   - Error: Muestra error, re-habilita botón
```

### Tipos de Abrazo

| Tipo | Mensaje | Animación |
|------|---------|-----------|
| NORMAL | "Te envío un abrazo 🐷🤗🐨" | anim_hug_normal.raw |
| LONG | "Te mando un abrazo bien largo 💕" | anim_hug_long.raw |
| TIGHT | "Te abrazo muy fuerte 🤗💖" | anim_hug_tight.raw |
| SPIN | "¡Abrazo con giro! 🐷🤗🐨💫" | anim_hug_spin.raw |

## Ubicación en la UI

El HugButton se ubica:
- **Posición:** Centro inferior de la pantalla del chat
- **Encima de:** Botón "jump to bottom"
- **Debajo de:** Área de notificaciones
- **Estilo:** Botón flotante rosa pastel (#FFFFB6C1) con ícono de corazón

## Dependencias Requeridas

### Ya Existentes en el Proyecto
- ✅ Jetpack Compose (ya configurado)
- ✅ Lottie Compose (animaciones)
- ✅ Mavericks (ViewModels)
- ✅ Hilt (inyección de dependencias)
- ✅ Material 3 (componentes UI)
- ✅ Room (base de datos romántica)

### Recursos Necesarios
- ✅ `anim_hug_normal.raw` - Animación abrazo normal
- ✅ `anim_hug_long.raw` - Animación abrazo largo
- ✅ `anim_hug_tight.raw` - Animación abrazo fuerte
- ✅ `anim_hug_spin.raw` - Animación abrazo con giro

## Permisos

No se requieren permisos adicionales. El HugButton usa:
- Permisos de red existentes (para enviar mensajes Matrix)
- Base de datos romántica existente (Room)

## Integración con Hilt

El HugButtonViewModel usa inyección asistida de Mavericks:

```kotlin
@AssistedInject constructor(
    @Assisted initialState: HugButtonViewState,
    private val romanticRepository: RomanticRepository,
    private val session: Session
)
```

El factory se declara en el companion object:
```kotlin
companion object {
    val factory: MavericksAssistedViewModelFactory<...> = 
        hiltMavericksViewModelFactory()
}
```

## Estados del HugButton

```kotlin
sealed class HugButtonState {
    object Idle : HugButtonState()           // Estado normal
    object Sending : HugButtonState()        // Enviando mensaje
    data class Success(val message: String)  // Éxito
    data class Error(val errorMessage: String) // Error
}
```

## Manejo de Errores

El HugButton maneja los siguientes errores:
1. **Sala no encontrada:** Room es null
2. **Error de envío:** Excepción al enviar mensaje Matrix
3. **Error de base de datos:** Excepción al registrar abrazo

En todos los casos:
- El botón se re-habilita automáticamente
- Se registra el error en Timber
- Se resetea el estado después de 2-3 segundos

## Pruebas

### Pruebas Manuales
1. Abrir chat de pareja
2. Presionar botón de abrazo (corazón rosa)
3. Verificar que:
   - Se envía mensaje automático
   - Se muestra animación
   - Se registra en estadísticas

### Pruebas Automáticas (por implementar)
```kotlin
@Test
fun `sendHug should send message and register hug`() = runTest {
    // TODO: Implementar tests unitarios
}
```

## Consideraciones de Rendimiento

1. **Lottie:** Las animaciones se cargan bajo demanda
2. **ViewModel:** Usa coroutineScope para operaciones async
3. **Estado:** Se mantiene en memoria con Mavericks
4. **Base de datos:** Room con Flow para observación reactiva

## Futuras Mejoras

- [ ] Añadir selector de tipo de abrazo (popup)
- [ ] Mostrar contador de abrazos enviados
- [ ] Animaciones personalizadas por nivel de cariño
- [ ] Sonidos de abrazo opcionales
- [ ] Estadísticas en tiempo real
- [ ] Logros por cantidad de abrazos

## Troubleshooting

### El botón no aparece
- Verificar que `visibility = View.VISIBLE` en `setupVisibility()`
- Revisir logs de Timber para errores de inicialización

### El botón no envía abrazos
- Verificar conexión a internet
- Confirmar que la sala existe en Matrix
- Revisar permisos de escritura en la sala

### La animación no se muestra
- Verificar que los archivos `.raw` existen en `res/raw/`
- Comprobar que Lottie está configurado correctamente

## Referencias

- [HugButton.kt](./vector/src/main/java/im/vector/app/features/romantic/ui/HugButton.kt)
- [HugButtonViewModel.kt](./vector/src/main/java/im/vector/app/features/romantic/ui/HugButtonViewModel.kt)
- [TimelineFragment.kt](./vector/src/main/java/im/vector/app/features/home/room/detail/TimelineFragment.kt)
- [RomanticRepository.kt](./vector/src/main/java/im/vector/app/features/romantic/data/repository/RomanticRepository.kt)

---

**Fecha de integración:** 2026-03-18
**Versión:** 1.0.0
**Estado:** ✅ Completado
