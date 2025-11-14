package com.kreggscode.wearosbmi.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import com.kreggscode.wearosbmi.ui.components.BMISlider
import com.kreggscode.wearosbmi.ui.components.GradientButton
import com.kreggscode.wearosbmi.ui.components.UnitSystemToggle
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.viewmodel.BMIViewModel

@Composable
fun CalculatorScreen(
    viewModel: BMIViewModel,
    onCalculate: () -> Unit
) {
    val listState = rememberScalingLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BMITheme.colors.primaryBackground)
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                top = 0.dp,
                bottom = 4.dp,
                start = 2.dp,
                end = 2.dp
            )
        ) {
            item {
                val infiniteTransition = rememberInfiniteTransition(label = "titleGlow")
                val glowAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.6f,
                    targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "glow"
                )
                
                val isDarkTheme = BMITheme.colors.primaryBackground == com.bmi.aipowered.ui.theme.DarkPrimaryBackground
                val glowColor = if (isDarkTheme) {
                    Color(0xFF60A5FA)
                } else {
                    Color(0xFF0066FF)
                }
                
                Text(
                    text = "BMI Calculator",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BMITheme.colors.primaryText.copy(alpha = glowAlpha),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 12.dp)
                        .drawBehind {
                            // Subtle glow effect
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        glowColor.copy(alpha = glowAlpha * 0.3f),
                                        glowColor.copy(alpha = 0f)
                                    )
                                ),
                                radius = size.width * 0.6f,
                                center = Offset(size.width / 2, size.height / 2)
                            )
                        },
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    UnitSystemToggle(
                        isMetric = viewModel.isMetric.value,
                        onToggle = { viewModel.toggleUnitSystem() },
                        isDarkTheme = viewModel.isDarkTheme.value,
                        onThemeToggle = { viewModel.toggleTheme() }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                BMISlider(
                    label = "HEIGHT",
                    value = viewModel.height.value,
                    onValueChange = { viewModel.updateHeight(it) },
                    valueRange = if (viewModel.isMetric.value) 0f..250f else 0f..98.4f,
                    steps = if (viewModel.isMetric.value) 249 else 98,
                    unit = if (viewModel.isMetric.value) "cm" else "in"
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                BMISlider(
                    label = "WEIGHT",
                    value = viewModel.weight.value,
                    onValueChange = { viewModel.updateWeight(it) },
                    valueRange = if (viewModel.isMetric.value) 0f..250f else 0f..551f,
                    steps = if (viewModel.isMetric.value) 249 else 550,
                    unit = if (viewModel.isMetric.value) "kg" else "lbs"
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                GradientButton(
                    text = "CALCULATE BMI",
                    onClick = onCalculate
                )
            }
        }

    }
}
