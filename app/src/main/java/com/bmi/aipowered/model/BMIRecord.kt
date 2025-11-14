package com.kreggscode.wearosbmi.model

data class BMIRecord(
    val id: Int = 0,
    val height: Float,
    val weight: Float,
    val bmi: Float,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
