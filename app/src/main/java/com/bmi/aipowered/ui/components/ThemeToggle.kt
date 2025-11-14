package com.kreggscode.wearosbmi.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun ThemeToggle(
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isDarkTheme) 0f else 360f,
        animationSpec = tween(durationMillis = 500),
        label = "rotation"
    )

    val glowColor = if (isDarkTheme) {
        Color(0xFFFCD34D).copy(alpha = 0.6f) // Yellow glow for dark mode
    } else {
        Color(0xFF3B82F6).copy(alpha = 0.6f) // Blue glow for light mode
    }

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.3f),
                        if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF1F5F9),
                        if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFE2E8F0)
                    )
                )
            )
            .clickable { onToggle() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .rotate(rotation)
        ) {
            if (isDarkTheme) {
                // Moon icon with glow
                Text(
                    text = "🌙",
                    fontSize = 24.sp,
                    color = Color(0xFFFCD34D)
                )
            } else {
                // Sun icon with glow
                Text(
                    text = "☀️",
                    fontSize = 24.sp,
                    color = Color(0xFFF59E0B)
                )
            }
        }
    }
}
