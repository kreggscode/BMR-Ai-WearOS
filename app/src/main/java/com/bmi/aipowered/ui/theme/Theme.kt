package com.kreggscode.wearosbmi.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme

data class BMIColors(
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val cardBackground: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val accent: Color,
    val success: Color,
    val warning: Color,
    val danger: Color
)

val LocalBMIColors = staticCompositionLocalOf {
    BMIColors(
        primaryBackground = DarkPrimaryBackground,
        secondaryBackground = DarkSecondaryBackground,
        cardBackground = DarkCardBackground,
        primaryText = DarkPrimaryText,
        secondaryText = DarkSecondaryText,
        accent = DarkAccent,
        success = DarkSuccess,
        warning = DarkWarning,
        danger = DarkDanger
    )
}

@Composable
fun BMITheme(
    isDarkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) {
        BMIColors(
            primaryBackground = DarkPrimaryBackground,
            secondaryBackground = DarkSecondaryBackground,
            cardBackground = DarkCardBackground,
            primaryText = DarkPrimaryText,
            secondaryText = DarkSecondaryText,
            accent = DarkAccent,
            success = DarkSuccess,
            warning = DarkWarning,
            danger = DarkDanger
        )
    } else {
        BMIColors(
            primaryBackground = LightPrimaryBackground,
            secondaryBackground = LightSecondaryBackground,
            cardBackground = LightCardBackground,
            primaryText = LightPrimaryText,
            secondaryText = LightSecondaryText,
            accent = LightAccent,
            success = LightSuccess,
            warning = LightWarning,
            danger = LightDanger
        )
    }

    CompositionLocalProvider(LocalBMIColors provides colors) {
        MaterialTheme(
            content = content
        )
    }
}

object BMITheme {
    val colors: BMIColors
        @Composable
        get() = LocalBMIColors.current
}
