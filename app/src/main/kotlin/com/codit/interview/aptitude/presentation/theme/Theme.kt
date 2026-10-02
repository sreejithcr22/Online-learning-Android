package com.codit.interview.aptitude.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codit.interview.aptitude.domain.model.AppTheme

/*
 * The palette below is carried over from the legacy XML themes:
 *   light -> #4caf50 primary, #f5f5f5 surfaces
 *   dark  -> #37474f primary, #455a64 card surfaces
 * Everything is now a Material 3 scheme instead of theme attributes resolved at runtime.
 */

private val LightColors = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = Color(0xFF0A2E12),
    secondary = Color(0xFF00ACC1),
    onSecondary = Color.White,
    error = Color(0xFFD50000),
    onError = Color.White,
    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color(0xFF616161),
    outline = Color(0xFFBDBDBD),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF26C6DA),
    onPrimary = Color(0xFF00232B),
    primaryContainer = Color(0xFF455A64),
    onPrimaryContainer = Color(0xFFE0F7FA),
    secondary = Color(0xFF4DD0E1),
    onSecondary = Color(0xFF00232B),
    error = Color(0xFFFF5252),
    onError = Color(0xFF2B0000),
    background = Color(0xFF37474F),
    onBackground = Color.White,
    surface = Color(0xFF455A64),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF37474F),
    onSurfaceVariant = Color(0xFFB0BEC5),
    outline = Color(0xFF78909C),
)

private val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    bodyLarge = TextStyle(fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontSize = 13.sp, lineHeight = 19.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    labelSmall = TextStyle(fontSize = 11.sp, color = Color(0xFF757575)),
)

/**
 * Semantic colours that Material 3's scheme does not model but the design needs:
 * "was this answer right/wrong/unattempted".
 */
data class StatusColors(
    val correct: Color,
    val wrong: Color,
    val notAttempted: Color,
    val cardSurface: Color,
    val subtleSurface: Color,
    val onSubtleSurface: Color,
    val accent: Color,
    val isDark: Boolean,
)

private val LightStatusColors = StatusColors(
    correct = Color(0xFF00C853),
    wrong = Color(0xFFD32F2F),
    notAttempted = Color(0xFFBDBDBD),
    cardSurface = Color.White,
    subtleSurface = Color(0xFFF5F5F5),
    onSubtleSurface = Color(0xFF424242),
    accent = Color(0xFF43A047),
    isDark = false,
)

private val DarkStatusColors = StatusColors(
    correct = Color(0xFF00E676),
    wrong = Color(0xFFFF5252),
    notAttempted = Color(0xFF546E7A),
    cardSurface = Color(0xFF455A64),
    subtleSurface = Color(0xFF37474F),
    onSubtleSurface = Color(0xFFCFD8DC),
    accent = Color(0xFF00ACC1),
    isDark = true,
)

val LocalStatusColors = staticCompositionLocalOf { LightStatusColors }

/** Shared spacing scale so padding stays consistent across the screens. */
object Spacing {
    val tiny = 4.dp
    val small = 8.dp
    val medium = 12.dp
    val large = 16.dp
    val extraLarge = 24.dp
}

@Composable
fun AptitudeTheme(
    theme: AppTheme = if (isSystemInDarkTheme()) AppTheme.DARK else AppTheme.LIGHT,
    content: @Composable () -> Unit,
) {
    val colors = if (theme == AppTheme.DARK) DarkColors else LightColors
    val statusColors = if (theme == AppTheme.DARK) DarkStatusColors else LightStatusColors

    CompositionLocalProvider(LocalStatusColors provides statusColors) {
        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            content = content,
        )
    }
}

/** Shorthand for the semantic status palette. */
val MaterialTheme.statusColors: StatusColors
    @Composable get() = LocalStatusColors.current
