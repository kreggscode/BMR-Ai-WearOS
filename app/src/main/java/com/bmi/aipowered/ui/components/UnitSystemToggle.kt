package com.kreggscode.wearosbmi.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.ui.theme.DarkPrimaryBackground

@Composable
fun UnitSystemToggle(
    isMetric: Boolean,
    onToggle: () -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Unit system toggle
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (isDarkTheme) {
                            listOf(
                                Color(0xFF1E293B),
                                Color(0xFF334155),
                                Color(0xFF1E293B)
                            )
                        } else {
                            listOf(
                                Color(0xFFE2E8F0),
                                Color(0xFFF1F5F9),
                                Color(0xFFE2E8F0)
                            )
                        }
                    )
                )
                .clickable { onToggle() }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Metric indicator
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (isMetric) {
                                if (isDarkTheme) Color(0xFF3B82F6) else Color(0xFF0066FF)
                            } else {
                                Color.Transparent
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isMetric) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.6f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                }
                
                Text(
                    text = if (isMetric) "METRIC" else "IMPERIAL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) {
                        if (isMetric) Color(0xFF60A5FA) else Color(0xFF94A3B8)
                    } else {
                        if (isMetric) Color(0xFF0066FF) else Color(0xFF000000)
                    }
                )
                
                // Imperial indicator
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (!isMetric) {
                                if (isDarkTheme) Color(0xFF3B82F6) else Color(0xFF0066FF)
                            } else {
                                Color.Transparent
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isMetric) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.6f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }
        
        // Theme toggle - compact version
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = if (isDarkTheme) {
                            listOf(
                                Color(0xFF1E293B),
                                Color(0xFF0F172A)
                            )
                        } else {
                            listOf(
                                Color(0xFFF1F5F9),
                                Color(0xFFE2E8F0)
                            )
                        }
                    )
                )
                .clickable { onThemeToggle() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isDarkTheme) "🌙" else "☀️",
                fontSize = 18.sp
            )
        }
    }
}

