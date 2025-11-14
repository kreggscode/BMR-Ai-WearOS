package com.kreggscode.wearosbmi.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.kreggscode.wearosbmi.model.BMICategory
import com.kreggscode.wearosbmi.ui.theme.BMITheme

@Composable
fun CircularProgressIndicator(
    bmi: Float,
    category: BMICategory,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val progress = (bmi - 15f) / 25f
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1500),
        label = "progress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val categoryColor = category.getColor(isDarkTheme)
    // Make colors brighter and more vibrant
    val brightCategoryColor = when {
        categoryColor == Color(0xFF60A5FA) -> Color(0xFF3B82F6) // Blue - brighter
        categoryColor == Color(0xFF10B981) -> Color(0xFF059669) // Green - brighter
        categoryColor == Color(0xFFF59E0B) -> Color(0xFFFFD700) // Yellow/Orange - brighter gold
        categoryColor == Color(0xFFEF4444) -> Color(0xFFDC2626) // Red - brighter
        else -> categoryColor
    }
    val glowColor = brightCategoryColor.copy(alpha = glowAlpha)

    Box(
        modifier = modifier.size(130.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow layer - brighter and more visible
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = glowColor.copy(alpha = (0.5f * glowAlpha).coerceIn(0f, 1f)),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Middle glow layer - brighter
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = glowColor.copy(alpha = (0.7f * glowAlpha).coerceIn(0f, 1f)),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Background circle
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = if (isDarkTheme) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.15f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Main progress circle with bright gradient
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gradientColors = listOf(
                brightCategoryColor,
                brightCategoryColor.copy(alpha = 0.95f),
                brightCategoryColor.copy(alpha = 0.85f),
                brightCategoryColor.copy(alpha = 0.75f)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    colors = gradientColors,
                    center = center
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Center content with glow
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(6.dp)
        ) {
            Text(
                text = String.format("%.1f", bmi),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = brightCategoryColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "BMI",
                fontSize = 10.sp,
                color = if (isDarkTheme) BMITheme.colors.secondaryText else Color(0xFF000000)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = category.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = brightCategoryColor
            )
        }
    }
}
