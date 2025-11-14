package com.kreggscode.wearosbmi.utils

import com.kreggscode.wearosbmi.model.BMICategory
import kotlin.math.pow

object BMICalculator {
    fun calculate(heightCm: Float, weightKg: Float): Float {
        val heightM = heightCm / 100f
        return weightKg / (heightM.pow(2))
    }
    
    fun calculateImperial(heightInches: Float, weightLbs: Float): Float {
        // Convert to metric first
        val heightCm = heightInches * 2.54f
        val weightKg = weightLbs * 0.453592f
        return calculate(heightCm, weightKg)
    }

    fun getCategory(bmi: Float): BMICategory {
        return BMICategory.fromBMI(bmi)
    }

    fun getProgressPercentage(bmi: Float): Float {
        // Map BMI to circular progress (0-100%)
        // Using a scale where 15 = 0% and 40 = 100%
        return ((bmi - 15f) / 25f * 100f).coerceIn(0f, 100f)
    }
}
