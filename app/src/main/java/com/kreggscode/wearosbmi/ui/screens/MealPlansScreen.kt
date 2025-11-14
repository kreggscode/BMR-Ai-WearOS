package com.kreggscode.wearosbmi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import com.kreggscode.wearosbmi.ui.components.BeautifulLoadingIndicator
import com.kreggscode.wearosbmi.ui.components.GradientButton
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.viewmodel.BMIViewModel

enum class MealCategory(val label: String, val emoji: String) {
    WEIGHT_LOSS("Weight Loss", "⚖️"),
    WEIGHT_GAIN("Weight Gain", "📈"),
    MUSCLE_GAIN("Muscle Gain", "💪"),
    HEALTHY_MEALS("Healthy Meals", "🥗"),
    QUICK_MEALS("Quick Meals", "⚡")
}

@Composable
fun MealPlansScreen(
    viewModel: BMIViewModel,
    onBack: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    var selectedCategory by remember { mutableStateOf<MealCategory?>(null) }
    val mealPlans = viewModel.mealPlans.value
    val isLoadingMeals = viewModel.isLoadingMealPlans.value

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
                bottom = 8.dp,
                start = 4.dp,
                end = 4.dp
            )
        ) {
            if (selectedCategory == null) {
                // Show category selection
                item {
                    Text(
                        text = "Choose Meal Type:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = BMITheme.colors.secondaryText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                MealCategory.values().forEach { category ->
                    item {
                        GradientButton(
                            text = "${category.emoji} ${category.label}",
                            onClick = {
                                selectedCategory = category
                                viewModel.generateMealPlansForCategory(category)
                            },
                            modifier = Modifier.fillMaxWidth(0.95f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                // Show selected category and meals
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${selectedCategory!!.emoji} ${selectedCategory!!.label}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BMITheme.colors.primaryText
                        )
                        Text(
                            text = "←",
                            fontSize = 14.sp,
                            color = BMITheme.colors.accent,
                            modifier = Modifier.clickable {
                                selectedCategory = null
                                viewModel.clearMealPlans()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (isLoadingMeals) {
                    item {
                        BeautifulLoadingIndicator(
                            modifier = Modifier.padding(vertical = 20.dp),
                            message = "Generating meal plans..."
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } else {
                    mealPlans.forEach { meal ->
                        item {
                            MealPlanCard(meal)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                // AI Generate button
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    GradientButton(
                        text = "🤖 Generate AI Meals",
                        onClick = {
                            viewModel.generateAIMealPlansForCategory(selectedCategory!!)
                        },
                        enabled = !isLoadingMeals,
                        modifier = Modifier.fillMaxWidth(0.95f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                GradientButton(
                    text = "← BACK",
                    onClick = onBack
                )
            }
        }
    }
}

@Composable
private fun MealPlanCard(meal: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .background(
                color = BMITheme.colors.cardBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = meal,
            fontSize = 12.sp,
            color = BMITheme.colors.primaryText,
            lineHeight = 18.sp
        )
    }
}
