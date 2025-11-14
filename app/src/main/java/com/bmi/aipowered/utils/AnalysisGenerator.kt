package com.kreggscode.wearosbmi.utils

import com.kreggscode.wearosbmi.model.BMICategory

object AnalysisGenerator {
    fun generateAnalysis(bmi: Float, category: BMICategory): String {
        return when (category) {
            BMICategory.UNDERWEIGHT -> getUnderweightAnalysis(bmi)
            BMICategory.NORMAL -> getNormalAnalysis(bmi)
            BMICategory.OVERWEIGHT -> getOverweightAnalysis(bmi)
            BMICategory.OBESE -> getObeseAnalysis(bmi)
        }
    }

    private fun getUnderweightAnalysis(bmi: Float): String {
        return """
📊 Your BMI: ${String.format("%.1f", bmi)}
Category: Underweight

💪 Health Assessment
Your BMI indicates you may be underweight. This could be due to various factors including metabolism, genetics, or dietary habits. Being underweight may increase the risk of nutritional deficiencies, weakened immune system, and bone health issues.

🎯 Recommendations
• Increase caloric intake with nutrient-dense foods
• Include protein-rich foods in every meal
• Consider strength training to build muscle mass
• Eat frequent, smaller meals throughout the day
• Consult a healthcare provider or nutritionist

⚠️ Important Note
BMI is a screening tool and doesn't account for muscle mass, bone density, or overall body composition. Always consult healthcare professionals for personalized advice.
        """.trimIndent()
    }

    private fun getNormalAnalysis(bmi: Float): String {
        return """
📊 Your BMI: ${String.format("%.1f", bmi)}
Category: Normal Weight

💪 Health Assessment
Excellent! Your BMI falls within the healthy weight range. This suggests a good balance between height and weight, which is associated with lower risk of weight-related health conditions. Maintaining this range supports overall health and wellbeing.

🎯 Recommendations
• Maintain current healthy habits
• Exercise 150 minutes per week (moderate intensity)
• Follow a balanced diet with variety
• Stay hydrated (8 glasses of water daily)
• Get 7-9 hours of quality sleep
• Regular health check-ups

⚠️ Important Note
BMI is a screening tool and doesn't account for muscle mass, bone density, or overall body composition. Continue healthy lifestyle habits and consult healthcare professionals for personalized advice.
        """.trimIndent()
    }

    private fun getOverweightAnalysis(bmi: Float): String {
        return """
📊 Your BMI: ${String.format("%.1f", bmi)}
Category: Overweight

💪 Health Assessment
Your BMI indicates you're in the overweight range. This may increase the risk of developing health conditions such as heart disease, type 2 diabetes, and high blood pressure. However, small lifestyle changes can make a significant positive impact.

🎯 Recommendations
• Aim for gradual weight loss (0.5-1 kg per week)
• Reduce portion sizes and calorie intake
• Increase physical activity to 200+ minutes weekly
• Choose whole foods over processed options
• Limit sugary drinks and high-fat foods
• Track your progress and celebrate small wins
• Consider consulting a healthcare provider

⚠️ Important Note
BMI is a screening tool and doesn't account for muscle mass, bone density, or overall body composition. Some athletes may have high BMI due to muscle mass. Consult healthcare professionals for personalized advice.
        """.trimIndent()
    }

    private fun getObeseAnalysis(bmi: Float): String {
        return """
📊 Your BMI: ${String.format("%.1f", bmi)}
Category: Obese

💪 Health Assessment
Your BMI indicates obesity, which significantly increases the risk of serious health conditions including heart disease, stroke, type 2 diabetes, certain cancers, and joint problems. However, even modest weight loss (5-10%) can greatly improve health outcomes.

🎯 Recommendations
• Consult healthcare provider for personalized plan
• Set realistic, achievable weight loss goals
• Focus on sustainable lifestyle changes
• Increase daily physical activity gradually
• Consider working with a registered dietitian
• Join support groups or weight management programs
• Monitor blood pressure and blood sugar regularly
• Be patient and kind to yourself

⚠️ Important Note
BMI is a screening tool and doesn't account for muscle mass, bone density, or overall body composition. Professional medical guidance is strongly recommended for safe and effective weight management.
        """.trimIndent()
    }
}
