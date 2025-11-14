package com.kreggscode.wearosbmi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import com.kreggscode.wearosbmi.model.BMICategory
import com.kreggscode.wearosbmi.ui.components.CircularProgressIndicator
import com.kreggscode.wearosbmi.ui.components.GradientButton
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.viewmodel.BMIViewModel

@Composable
fun ResultsScreen(
    viewModel: BMIViewModel,
    onAIAnalysis: () -> Unit,
    onTracker: () -> Unit,
    onDietPlans: () -> Unit,
    onMealPlans: () -> Unit,
    onChatWithAI: () -> Unit,
    onBack: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val bmiData = viewModel.bmiData.value

    // Auto-scroll to top when screen opens
    LaunchedEffect(Unit) {
        listState.scrollToItem(0)
    }

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
                top = 4.dp,
                bottom = 8.dp,
                start = 4.dp,
                end = 4.dp
            )
        ) {
        item {
            CircularProgressIndicator(
                bmi = bmiData.bmi,
                category = bmiData.category,
                isDarkTheme = viewModel.isDarkTheme.value
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                BMICategory.values().forEach { category ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Text(
                                text = "● ",
                                color = category.getColor(viewModel.isDarkTheme.value),
                                fontSize = 12.sp
                            )
                            Text(
                                text = category.label,
                                fontSize = 11.sp,
                                color = if (category == bmiData.category) {
                                    BMITheme.colors.primaryText
                                } else {
                                    BMITheme.colors.secondaryText
                                },
                                fontWeight = if (category == bmiData.category) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        }
                        Text(
                            text = category.range,
                            fontSize = 11.sp,
                            color = BMITheme.colors.secondaryText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            GradientButton(
                text = "💾 SAVE",
                onClick = { viewModel.saveBMIRecord() }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            GradientButton(
                text = "🤖 AI ANALYSIS",
                onClick = onAIAnalysis
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            GradientButton(
                text = "📊 HISTORY",
                onClick = onTracker
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            GradientButton(
                text = "🍽️ DIET PLANS",
                onClick = onDietPlans
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            GradientButton(
                text = "🍳 MEAL PLANS",
                onClick = onMealPlans
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            GradientButton(
                text = "💬 CHAT WITH AI",
                onClick = onChatWithAI
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            GradientButton(
                text = "← BACK",
                onClick = onBack
            )
        }
        }

    }
}
