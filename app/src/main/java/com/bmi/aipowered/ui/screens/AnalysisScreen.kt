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
import com.kreggscode.wearosbmi.ui.components.BeautifulLoadingIndicator
import com.kreggscode.wearosbmi.ui.components.GradientButton
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.viewmodel.BMIViewModel

private fun formatAnalysisText(text: String): List<String> {
    // Split by double newlines or common separators
    val paragraphs = text.split("\n\n", "\n•", "\n-")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
    
    // If no proper paragraphs found, split by single newlines
    return if (paragraphs.size <= 1) {
        text.split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    } else {
        paragraphs
    }
}

@Composable
fun AnalysisScreen(
    viewModel: BMIViewModel,
    onBack: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val analysisText = viewModel.analysisText.value
    val isLoading = viewModel.isLoadingAnalysis.value

    LaunchedEffect(Unit) {
        if (analysisText.isEmpty()) {
            viewModel.generateAnalysis()
        }
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
                top = 0.dp,
                bottom = 8.dp,
                start = 4.dp,
                end = 4.dp
            )
        ) {
        if (isLoading) {
            item {
                BeautifulLoadingIndicator(
                    modifier = Modifier.padding(vertical = 20.dp),
                    message = "Generating analysis..."
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        } else {
            item {
                // Format AI analysis text with proper spacing
                val formattedText = formatAnalysisText(analysisText)
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    formattedText.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            fontSize = 13.sp,
                            color = BMITheme.colors.primaryText,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
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
