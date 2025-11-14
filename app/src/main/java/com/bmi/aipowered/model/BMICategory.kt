package com.kreggscode.wearosbmi.model

import androidx.compose.ui.graphics.Color

enum class BMICategory(
    val label: String,
    val range: String,
    val colorLight: Color,
    val colorDark: Color
) {
    UNDERWEIGHT(
        label = "Underweight",
        range = "< 18.5",
        colorLight = Color(0xFF3B82F6), // Blue
        colorDark = Color(0xFF60A5FA)
    ),
    NORMAL(
        label = "Normal",
        range = "18.5 - 24.9",
        colorLight = Color(0xFF10B981), // Green
        colorDark = Color(0xFF34D399)
    ),
    OVERWEIGHT(
        label = "Overweight",
        range = "25.0 - 29.9",
        colorLight = Color(0xFFF59E0B), // Amber
        colorDark = Color(0xFFFBBF24)
    ),
    OBESE(
        label = "Obese",
        range = "≥ 30.0",
        colorLight = Color(0xFFEF4444), // Red
        colorDark = Color(0xFFF87171)
    );

    companion object {
        fun fromBMI(bmi: Float): BMICategory {
            return when {
                bmi < 18.5f -> UNDERWEIGHT
                bmi < 25.0f -> NORMAL
                bmi < 30.0f -> OVERWEIGHT
                else -> OBESE
            }
        }
    }

    fun getColor(isDark: Boolean): Color {
        return if (isDark) colorDark else colorLight
    }
}
