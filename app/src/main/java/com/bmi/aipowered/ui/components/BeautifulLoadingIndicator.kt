package com.kreggscode.wearosbmi.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.ui.theme.DarkPrimaryBackground

@Composable
fun BeautifulLoadingIndicator(
    modifier: Modifier = Modifier,
    message: String = "Loading..."
) {
    val isDarkTheme = BMITheme.colors.primaryBackground == DarkPrimaryBackground
    
    // Animated rotation
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Pulsing scale
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    // Glow alpha
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer glow ring
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2 - 4.dp.toPx()
                
                drawCircle(
                    color = if (isDarkTheme) {
                        Color(0xFFFCD34D).copy(alpha = glowAlpha * 0.3f)
                    } else {
                        Color(0xFFFF9500).copy(alpha = glowAlpha * 0.3f)
                    },
                    radius = radius + 8.dp.toPx(),
                    center = center
                )
            }
            
            // Main rotating circle
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationZ = rotation
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                val center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2 - 4.dp.toPx()
                
                // Draw beautiful gradient arc
                drawArc(
                    color = if (isDarkTheme) {
                        Color(0xFFFCD34D).copy(alpha = glowAlpha)
                    } else {
                        Color(0xFFFF9500).copy(alpha = glowAlpha)
                    },
                    startAngle = rotation,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
                
                // Secondary arc
                drawArc(
                    color = if (isDarkTheme) {
                        Color(0xFFF59E0B).copy(alpha = glowAlpha * 0.6f)
                    } else {
                        Color(0xFFFFB84D).copy(alpha = glowAlpha * 0.6f)
                    },
                    startAngle = rotation + 90f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        androidx.wear.compose.material.Text(
            text = message,
            fontSize = 12.sp,
            color = BMITheme.colors.secondaryText
        )
    }
}

