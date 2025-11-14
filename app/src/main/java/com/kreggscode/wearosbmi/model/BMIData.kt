package com.kreggscode.wearosbmi.model

data class BMIData(
    val height: Float = 170f, // in cm
    val weight: Float = 70f,  // in kg
    val bmi: Float = 0f,
    val category: BMICategory = BMICategory.NORMAL
)
