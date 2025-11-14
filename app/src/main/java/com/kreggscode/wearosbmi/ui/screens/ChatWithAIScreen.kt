package com.kreggscode.wearosbmi.ui.screens

import androidx.compose.foundation.background
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

@Composable
fun ChatWithAIScreen(
    viewModel: BMIViewModel,
    onBack: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val chatMessages = viewModel.chatMessages.value
    val isLoadingChat = viewModel.isLoadingChat.value

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
            // Chat messages
            chatMessages.forEach { message ->
                item {
                    ChatMessageCard(
                        text = message.text,
                        isUser = message.isUser
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (isLoadingChat) {
                item {
                    BeautifulLoadingIndicator(
                        modifier = Modifier.padding(vertical = 12.dp),
                        message = "AI is thinking..."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                Text(
                    text = "Quick Questions:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BMITheme.colors.primaryText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Dynamic questions based on BMI category
            val quickQuestions = viewModel.getDynamicQuestions()

            quickQuestions.forEach { question ->
                item {
                    GradientButton(
                        text = question,
                        onClick = {
                            viewModel.sendChatMessage(question)
                        },
                        enabled = !isLoadingChat,
                        modifier = Modifier.fillMaxWidth(0.95f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
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

@Composable
private fun ChatMessageCard(text: String, isUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .background(
                color = if (isUser) {
                    BMITheme.colors.accent.copy(alpha = 0.2f)
                } else {
                    BMITheme.colors.cardBackground
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = BMITheme.colors.primaryText,
            lineHeight = 18.sp
        )
    }
}

