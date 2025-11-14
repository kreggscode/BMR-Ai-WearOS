package com.kreggscode.wearosbmi.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.ui.theme.DarkPrimaryBackground
import kotlin.math.roundToInt

@Composable
fun BMISlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    unit: String,
    modifier: Modifier = Modifier
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(durationMillis = 200),
        label = "value"
    )
    
    val isDarkTheme = BMITheme.colors.primaryBackground == DarkPrimaryBackground

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF000000),
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Display value with proper formatting for imperial (feet'inches")
        val displayText = if (unit == "in" && animatedValue > 0) {
            val feet = (animatedValue / 12f).toInt()
            val inches = (animatedValue % 12f).toInt()
            "$feet'$inches\""
        } else {
            "${animatedValue.toInt()} $unit"
        }
        
        Text(
            text = displayText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) {
                Color(0xFF60A5FA)
            } else {
                Color(0xFF0066FF)
            },
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        // Smooth draggable slider using Material3 Slider
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Slider(
                value = value,
                onValueChange = { newValue ->
                    onValueChange(newValue)
                },
                valueRange = valueRange,
                steps = steps - 1,
                colors = SliderDefaults.colors(
                    thumbColor = if (isDarkTheme) {
                        Color(0xFF60A5FA)
                    } else {
                        Color(0xFF0066FF)
                    },
                    activeTrackColor = if (isDarkTheme) {
                        Color(0xFF3B82F6)
                    } else {
                        Color(0xFF0066FF)
                    },
                    inactiveTrackColor = if (isDarkTheme) {
                        Color(0xFF334155)
                    } else {
                        Color(0xFFE2E8F0)
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Plus/Minus buttons with better design
        Row(
            modifier = Modifier.fillMaxWidth(0.95f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Minus button
            Button(
                onClick = {
                    val step = (valueRange.endInclusive - valueRange.start) / steps
                    val newValue = (value - step).coerceIn(valueRange.start, valueRange.endInclusive)
                    onValueChange(newValue)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isDarkTheme) {
                        Color(0xFF1E293B)
                    } else {
                        Color(0xFFE2E8F0)
                    }
                ),
                modifier = Modifier.size(44.dp)
            ) {
                Text(
                    "-",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF0066FF)
                )
            }
            
            // Value range labels
            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = valueRange.start.toInt().toString(),
                    fontSize = 10.sp,
                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF000000)
                )
                Text(
                    text = "•",
                    fontSize = 8.sp,
                    color = if (isDarkTheme) Color(0xFF475569) else Color(0xFF666666)
                )
                Text(
                    text = valueRange.endInclusive.toInt().toString(),
                    fontSize = 10.sp,
                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF000000)
                )
            }
            
            // Plus button
            Button(
                onClick = {
                    val step = (valueRange.endInclusive - valueRange.start) / steps
                    val newValue = (value + step).coerceIn(valueRange.start, valueRange.endInclusive)
                    onValueChange(newValue)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isDarkTheme) {
                        Color(0xFF1E293B)
                    } else {
                        Color(0xFFE2E8F0)
                    }
                ),
                modifier = Modifier.size(44.dp)
            ) {
                Text(
                    "+",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF0066FF)
                )
            }
        }
    }
}
