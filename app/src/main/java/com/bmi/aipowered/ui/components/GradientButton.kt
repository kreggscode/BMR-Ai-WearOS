package com.kreggscode.wearosbmi.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.ui.theme.DarkPrimaryBackground

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val isDarkTheme = BMITheme.colors.primaryBackground == DarkPrimaryBackground
    
    // Animated glow intensity
    val glowAlpha by animateFloatAsState(
        targetValue = if (enabled) 0.8f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    val pulseScale by animateFloatAsState(
        targetValue = if (enabled) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Beautiful yellow/black gradient for dark mode, creamish orange for light mode
    val gradientColors = if (enabled) {
        if (isDarkTheme) {
            // Dark mode: Bright yellow gradient - much more visible!
            listOf(
                Color(0xFFFFD700), Color(0xFFFFC107), Color(0xFFFFB300),
                Color(0xFFFFA000), Color(0xFFFF8F00), Color(0xFFFF6F00),
                Color(0xFFFF8F00), Color(0xFFFFA000), Color(0xFFFFB300),
                Color(0xFFFFC107), Color(0xFFFFD700), Color(0xFFFFE082),
                Color(0xFFFFD700), Color(0xFFFFC107)
            )
        } else {
            // Light mode: Creamish orange - beautiful!
            listOf(
                Color(0xFFFFE5B4), Color(0xFFFFD89B), Color(0xFFFFC966),
                Color(0xFFFFB84D), Color(0xFFFFA533), Color(0xFFFF9500),
                Color(0xFFFF8800), Color(0xFFFF7A00), Color(0xFFFF9500),
                Color(0xFFFFA533), Color(0xFFFFB84D), Color(0xFFFFC966),
                Color(0xFFFFD89B), Color(0xFFFFE5B4), Color(0xFFFFF0D6),
                Color(0xFFFFE5B4), Color(0xFFFFD89B)
            )
        }
    } else {
        listOf(
            Color.Gray.copy(alpha = 0.5f),
            Color.Gray.copy(alpha = 0.45f),
            Color.Gray.copy(alpha = 0.4f),
            Color.Gray.copy(alpha = 0.35f),
            Color.Gray.copy(alpha = 0.3f)
        )
    }
    
    val gradient = Brush.horizontalGradient(
        colors = gradientColors,
        startX = 0f,
        endX = 1000f
    )

    // Enhanced glow effect with multiple layers
    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(52.dp)
    ) {
        // Animated outer glow layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(26.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = if (enabled) {
                            if (isDarkTheme) {
                                listOf(
                                    Color(0xFFFFD700).copy(alpha = glowAlpha * 0.9f),
                                    Color(0xFFFFC107).copy(alpha = glowAlpha * 0.7f),
                                    Color(0xFFFFB300).copy(alpha = glowAlpha * 0.5f),
                                    Color(0xFFFFA000).copy(alpha = glowAlpha * 0.3f),
                                    Color.Transparent
                                )
                            } else {
                                listOf(
                                    Color(0xFFFF9500).copy(alpha = glowAlpha * 0.7f),
                                    Color(0xFFFFB84D).copy(alpha = glowAlpha * 0.5f),
                                    Color(0xFFFFC966).copy(alpha = glowAlpha * 0.3f),
                                    Color.Transparent
                                )
                            }
                        } else {
                            listOf(Color.Transparent, Color.Transparent)
                        }
                    )
                )
        )
        
        // Animated middle glow layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = if (enabled) {
                            if (isDarkTheme) {
                                listOf(
                                    Color(0xFFFFC107).copy(alpha = glowAlpha * 0.7f),
                                    Color(0xFFFFB300).copy(alpha = glowAlpha * 0.5f),
                                    Color(0xFFFFA000).copy(alpha = glowAlpha * 0.3f),
                                    Color(0xFFFF8F00).copy(alpha = glowAlpha * 0.15f),
                                    Color.Transparent
                                )
                            } else {
                                listOf(
                                    Color(0xFFFFB84D).copy(alpha = glowAlpha * 0.5f),
                                    Color(0xFFFFC966).copy(alpha = glowAlpha * 0.35f),
                                    Color(0xFFFFD89B).copy(alpha = glowAlpha * 0.15f),
                                    Color.Transparent
                                )
                            }
                        } else {
                            listOf(Color.Transparent, Color.Transparent)
                        }
                    )
                )
        )
        
        // Inner glow layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = if (enabled) {
                            if (isDarkTheme) {
                                listOf(
                                    Color(0xFFFFD700).copy(alpha = glowAlpha * 0.5f),
                                    Color(0xFFFFC107).copy(alpha = glowAlpha * 0.2f),
                                    Color.Transparent
                                )
                            } else {
                                listOf(
                                    Color(0xFFFF9500).copy(alpha = glowAlpha * 0.4f),
                                    Color(0xFFFFB84D).copy(alpha = glowAlpha * 0.15f),
                                    Color.Transparent
                                )
                            }
                        } else {
                            listOf(Color.Transparent, Color.Transparent)
                        }
                    )
                )
        )
        
        // Main button with perfect seamless gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .scale(pulseScale)
                .clip(RoundedCornerShape(21.dp))
                .background(gradient)
                .clickable(enabled = enabled) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            // Subtle inner highlight for depth
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .clip(RoundedCornerShape(topStart = 21.dp, topEnd = 21.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            )
            
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFF000000) else Color(0xFF000000), // Black text for maximum visibility
                modifier = Modifier.padding(horizontal = 6.dp),
                maxLines = 1
            )
        }
    }
}
