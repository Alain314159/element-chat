package com.mardous.cerlita.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor

// ===== CERDITA PINK =====
val CerditaPink = Color(0xFFFF69B4)
val CerditaPinkLight = Color(0xFFFFB6D9)
val CerditaPinkDark = Color(0xFFC51162)

// Cuerpo Cerdita
val CerditaBody = Color(0xFFFFB6D9)
val CerditaBodyShadow = Color(0xFFFF69B4)
val CerditaSnout = Color(0xFFFF69B4)
val CerditaEars = Color(0xFFFF1493)
val CerditaCheeks = Color(0xFFFF69B4)

// ===== KOALITA GREEN =====
val KoalitaGreen = Color(0xFF4CAF50)
val KoalitaGreenLight = Color(0xFF81C784)
val KoalitaGreenDark = Color(0xFF2E7D32)

// Cuerpo Koalita
val KoalitaBody = Color(0xFF90A4AE)
val KoalitaBodyShadow = Color(0xFF546E7A)
val KoalitaEars = Color(0xFF78909C)
val KoalitaEarsFluff = Color(0xFFECEFF1)
val KoalitaNose = Color(0xFF37474F)

// ===== AMOR ROMÁNTICO =====
val LoveRed = Color(0xFFE91E63)
val LovePink = Color(0xFFFF1493)
val LoveGolden = Color(0xFFFFD700)

// ===== FLORES =====
val RoseRed = Color(0xFFD32F2F)
val TulipPink = Color(0xFFFF69B4)
val SunflowerYellow = Color(0xFFFFEB3B)
val DaisyWhite = Color(0xFFFFFFFF)
val LavenderPurple = Color(0xFF9C27B0)
val CherryBlossom = Color(0xFFFFB7C5)
val LotusPink = Color(0xFFFFC0CB)

// ===== NEUTROS =====
val PureWhite = Color(0xFFFFFFFF)
val SoftWhite = Color(0xFFFFF5F7)
val LightGray = Color(0xFFF5F5F5)
val MediumGray = Color(0xFF9E9E9E)
val DarkGray = Color(0xFF424242)
val PureBlack = Color(0xFF000000)

// ===== FONDOS =====
val BackgroundLight = Color(0xFFFFF9FC)
val BackgroundDark = Color(0xFF1A1A2E)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF2D2D44)

// ===== CHAT =====
val ChatBubbleMe = Color(0xFFFF69B4)
val ChatBubbleOther = Color(0xFFE0E0E0)
val ChatTextMe = Color(0xFFFFFFFF)
val ChatTextOther = Color(0xFF000000)

// ===== ESTADOS =====
val Success = Color(0xFF4CAF50)
val Error = Color(0xFFE91E63)
val Warning = Color(0xFFFF9800)
val Info = Color(0xFF2196F3)

// ===== GRADIENTES =====
val CerditaGradient = Brush.verticalGradient(
    colors = listOf(CerditaPinkLight, CerditaPink, CerditaPinkDark)
)

val KoalitaGradient = Brush.verticalGradient(
    colors = listOf(KoalitaGreenLight, KoalitaGreen, KoalitaGreenDark)
)

val LoveGradient = Brush.linearGradient(
    colors = listOf(LovePink, LoveRed),
    start = androidx.compose.ui.geometry.Offset(0f, 0f),
    end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
)

val SunsetGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFF9A8B), Color(0xFFFF6B9D), Color(0xFFC44569))
)

val NightGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460))
)

// ===== COLORES DE TEMA =====
data class CerlitaColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color,
    val onError: Color,
    val chatBubbleMe: Color,
    val chatBubbleOther: Color,
    val gradient: Brush
)

val CerditaLightColors = CerlitaColors(
    primary = CerditaPink,
    onPrimary = PureWhite,
    primaryContainer = CerditaPinkLight,
    onPrimaryContainer = CerditaPinkDark,
    secondary = KoalitaGreen,
    onSecondary = PureWhite,
    secondaryContainer = KoalitaGreenLight,
    onSecondaryContainer = KoalitaGreenDark,
    background = BackgroundLight,
    onBackground = DarkGray,
    surface = SurfaceLight,
    onSurface = DarkGray,
    error = Error,
    onError = PureWhite,
    chatBubbleMe = ChatBubbleMe,
    chatBubbleOther = ChatBubbleOther,
    gradient = CerditaGradient
)

val KoalitaDarkColors = CerlitaColors(
    primary = KoalitaGreenLight,
    onPrimary = KoalitaGreenDark,
    primaryContainer = KoalitaGreen,
    onPrimaryContainer = KoalitaGreenLight,
    secondary = CerditaPink,
    onSecondary = PureWhite,
    secondaryContainer = CerditaPinkDark,
    onSecondaryContainer = CerditaPinkLight,
    background = BackgroundDark,
    onBackground = SoftWhite,
    surface = SurfaceDark,
    onSurface = SoftWhite,
    error = Color(0xFFCF6679),
    onError = PureBlack,
    chatBubbleMe = ChatBubbleMe,
    chatBubbleOther = Color(0xFF424242),
    gradient = KoalitaGradient
)

val RomanticColors = CerlitaColors(
    primary = LovePink,
    onPrimary = PureWhite,
    primaryContainer = LoveRed,
    onPrimaryContainer = LoveGolden,
    secondary = LoveGolden,
    onSecondary = Color(0xFF5D4037),
    secondaryContainer = Color(0xFFFFECB3),
    onSecondaryContainer = Color(0xFF795548),
    background = Color(0xFFFFF5F7),
    onBackground = Color(0xFF4A148C),
    surface = Color(0xFFFFF9FC),
    onSurface = Color(0xFF4A148C),
    error = LoveRed,
    onError = PureWhite,
    chatBubbleMe = LoveRed,
    chatBubbleOther = Color(0xFFF8BBD0),
    gradient = LoveGradient
)
