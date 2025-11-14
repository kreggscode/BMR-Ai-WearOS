package com.kreggscode.wearosbmi.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.wearosbmi.model.BMICategory
import com.kreggscode.wearosbmi.model.BMIData
import com.kreggscode.wearosbmi.model.BMIRecord
import com.kreggscode.wearosbmi.network.AIService
import com.kreggscode.wearosbmi.utils.BMICalculator
import kotlinx.coroutines.launch

class BMIViewModel : ViewModel() {
    private val aiService = AIService()

    private val _height = mutableStateOf(170f) // cm or inches (default: 170cm = ~67 inches)
    val height: State<Float> = _height

    private val _weight = mutableStateOf(70f) // kg or lbs (default: 70kg = ~154 lbs)
    val weight: State<Float> = _weight

    private val _bmiData = mutableStateOf(BMIData())
    val bmiData: State<BMIData> = _bmiData

    private val _analysisText = mutableStateOf("")
    val analysisText: State<String> = _analysisText

    private val _isLoadingAnalysis = mutableStateOf(false)
    val isLoadingAnalysis: State<Boolean> = _isLoadingAnalysis

    private val _isDarkTheme = mutableStateOf(true)
    val isDarkTheme: State<Boolean> = _isDarkTheme

    private val _isMetric = mutableStateOf(true) // true = metric, false = imperial
    val isMetric: State<Boolean> = _isMetric

    private val _bmiRecords = mutableStateOf<List<BMIRecord>>(emptyList())
    val bmiRecords: State<List<BMIRecord>> = _bmiRecords

    private val _dietPlans = mutableStateOf<List<String>>(emptyList())
    val dietPlans: State<List<String>> = _dietPlans

    private val _isLoadingDietPlans = mutableStateOf(false)
    val isLoadingDietPlans: State<Boolean> = _isLoadingDietPlans

    private val _mealPlans = mutableStateOf<List<String>>(emptyList())
    val mealPlans: State<List<String>> = _mealPlans

    private val _isLoadingMealPlans = mutableStateOf(false)
    val isLoadingMealPlans: State<Boolean> = _isLoadingMealPlans

    data class ChatMessage(val text: String, val isUser: Boolean)
    private val _chatMessages = mutableStateOf<List<ChatMessage>>(emptyList())
    val chatMessages: State<List<ChatMessage>> = _chatMessages

    private val _isLoadingChat = mutableStateOf(false)
    val isLoadingChat: State<Boolean> = _isLoadingChat

    fun updateHeight(newHeight: Float) {
        _height.value = newHeight
    }

    fun updateWeight(newWeight: Float) {
        _weight.value = newWeight
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun toggleUnitSystem() {
        _isMetric.value = !_isMetric.value
        // Convert values when switching
        if (_isMetric.value) {
            // Convert from imperial to metric
            _height.value = _height.value * 2.54f // inches to cm
            _weight.value = _weight.value * 0.453592f // lbs to kg
        } else {
            // Convert from metric to imperial
            _height.value = _height.value / 2.54f // cm to inches
            _weight.value = _weight.value / 0.453592f // kg to lbs
        }
    }

    fun calculateBMI() {
        val bmi = if (_isMetric.value) {
            BMICalculator.calculate(_height.value, _weight.value)
        } else {
            BMICalculator.calculateImperial(_height.value, _weight.value)
        }
        val category = BMICalculator.getCategory(bmi)
        _bmiData.value = BMIData(
            height = _height.value,
            weight = _weight.value,
            bmi = bmi,
            category = category
        )
    }

    fun generateAnalysis() {
        _isLoadingAnalysis.value = true
        viewModelScope.launch {
            try {
                val analysis = aiService.getAnalysis(
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category,
                    height = _bmiData.value.height,
                    weight = _bmiData.value.weight
                )
                _analysisText.value = analysis
            } catch (e: Exception) {
                // Fallback is already handled in AIService
                _analysisText.value = "Error loading analysis. Please try again."
            } finally {
                _isLoadingAnalysis.value = false
            }
        }
    }

    fun saveBMIRecord() {
        val record = BMIRecord(
            height = _bmiData.value.height,
            weight = _bmiData.value.weight,
            bmi = _bmiData.value.bmi,
            category = _bmiData.value.category.label,
            timestamp = System.currentTimeMillis()
        )
        val updatedList = _bmiRecords.value.toMutableList()
        updatedList.add(record)
        _bmiRecords.value = updatedList
    }

    fun loadRecords() {
        // In a real app, this would load from database
        // For now, we just keep the in-memory list
    }

    fun generateDietPlans() {
        _isLoadingDietPlans.value = true
        viewModelScope.launch {
            try {
                val plans = aiService.getDietPlans(
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category
                )
                _dietPlans.value = plans
            } catch (e: Exception) {
                // Fallback to placeholder plans
                _dietPlans.value = getPlaceholderDietPlans(_bmiData.value.category)
            } finally {
                _isLoadingDietPlans.value = false
            }
        }
    }

    fun generateDietPlansForCategory(dietCategory: com.bmi.aipowered.ui.screens.DietCategory) {
        _isLoadingDietPlans.value = true
        viewModelScope.launch {
            try {
                val plans = aiService.getDietPlansForCategory(
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category,
                    dietCategory = dietCategory.label
                )
                _dietPlans.value = plans
            } catch (e: Exception) {
                // Fallback to placeholder plans based on diet category
                _dietPlans.value = getPlaceholderDietPlansForCategory(dietCategory)
            } finally {
                _isLoadingDietPlans.value = false
            }
        }
    }

    fun generateAIDietPlan(dietCategory: com.bmi.aipowered.ui.screens.DietCategory) {
        _isLoadingDietPlans.value = true
        viewModelScope.launch {
            try {
                val plans = aiService.getDietPlansForCategory(
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category,
                    dietCategory = dietCategory.label
                )
                _dietPlans.value = plans
            } catch (e: Exception) {
                _dietPlans.value = getPlaceholderDietPlansForCategory(dietCategory)
            } finally {
                _isLoadingDietPlans.value = false
            }
        }
    }

    fun clearDietPlans() {
        _dietPlans.value = emptyList()
    }

    fun generateMealPlansForCategory(mealCategory: com.bmi.aipowered.ui.screens.MealCategory) {
        _isLoadingMealPlans.value = true
        viewModelScope.launch {
            try {
                val meals = aiService.getMealPlansForCategory(
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category,
                    mealCategory = mealCategory.label
                )
                _mealPlans.value = meals
            } catch (e: Exception) {
                _mealPlans.value = getPlaceholderMealPlansForCategory(mealCategory)
            } finally {
                _isLoadingMealPlans.value = false
            }
        }
    }

    fun generateAIMealPlansForCategory(mealCategory: com.bmi.aipowered.ui.screens.MealCategory) {
        _isLoadingMealPlans.value = true
        viewModelScope.launch {
            try {
                val meals = aiService.getMealPlansForCategory(
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category,
                    mealCategory = mealCategory.label
                )
                _mealPlans.value = meals
            } catch (e: Exception) {
                _mealPlans.value = getPlaceholderMealPlansForCategory(mealCategory)
            } finally {
                _isLoadingMealPlans.value = false
            }
        }
    }

    fun clearMealPlans() {
        _mealPlans.value = emptyList()
    }

    fun generateAIMealPlans() {
        _isLoadingMealPlans.value = true
        viewModelScope.launch {
            try {
                val meals = aiService.getMealPlans(
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category
                )
                _mealPlans.value = meals
            } catch (e: Exception) {
                _mealPlans.value = getPlaceholderMealPlans(_bmiData.value.category)
            } finally {
                _isLoadingMealPlans.value = false
            }
        }
    }

    fun getDynamicQuestions(): List<String> {
        val bmi = _bmiData.value.bmi
        val category = _bmiData.value.category
        
        return when (category) {
            BMICategory.UNDERWEIGHT -> listOf(
                "How to gain healthy weight?",
                "Best foods for weight gain?",
                "Exercise for muscle building?",
                "How many calories daily?",
                "Protein intake recommendations?"
            )
            BMICategory.NORMAL -> listOf(
                "How to maintain my BMI?",
                "Best exercises for fitness?",
                "Healthy meal prep tips?",
                "How to build muscle?",
                "Nutrition for active lifestyle?"
            )
            BMICategory.OVERWEIGHT -> listOf(
                "How to lose weight safely?",
                "Best exercises for weight loss?",
                "Low calorie meal ideas?",
                "How much water daily?",
                "Cardio vs strength training?"
            )
            BMICategory.OBESE -> listOf(
                "Safe weight loss strategies?",
                "Best exercises to start?",
                "How to create calorie deficit?",
                "Portion control tips?",
                "When to see a doctor?"
            )
        }
    }

    private fun getPlaceholderDietPlansForCategory(dietCategory: com.bmi.aipowered.ui.screens.DietCategory): List<String> {
        return when (dietCategory) {
            com.bmi.aipowered.ui.screens.DietCategory.WEIGHT_LOSS -> listOf(
                "• Create 500-750 calorie deficit daily",
                "• Eat protein with every meal (30g+)",
                "• Fill half plate with vegetables",
                "• Drink water before meals",
                "• Avoid liquid calories"
            )
            com.bmi.aipowered.ui.screens.DietCategory.WEIGHT_GAIN -> listOf(
                "• Add 300-500 calories daily",
                "• Eat every 3-4 hours",
                "• Include healthy fats (nuts, avocados)",
                "• Protein at every meal",
                "• Strength training 3x/week"
            )
            com.bmi.aipowered.ui.screens.DietCategory.MUSCLE_GAIN -> listOf(
                "• 1.6-2.2g protein per kg bodyweight",
                "• Progressive strength training",
                "• Eat within 30 min post-workout",
                "• Complex carbs for energy",
                "• 7-9 hours sleep nightly"
            )
            com.bmi.aipowered.ui.screens.DietCategory.MAINTENANCE -> listOf(
                "• Track calories to maintain weight",
                "• Balanced macronutrients",
                "• Regular exercise routine",
                "• Stay hydrated",
                "• Monitor weight weekly"
            )
        }
    }

    private fun getPlaceholderDietPlans(category: BMICategory): List<String> {
        return when (category) {
            BMICategory.UNDERWEIGHT -> listOf(
                "• Increase calorie intake with healthy foods",
                "• Eat 5-6 small meals daily",
                "• Include protein-rich foods (eggs, lean meat)",
                "• Add healthy fats (nuts, avocados)",
                "• Strength training to build muscle"
            )
            BMICategory.NORMAL -> listOf(
                "• Maintain balanced diet",
                "• 5 servings of fruits/vegetables daily",
                "• Whole grains and lean proteins",
                "• Stay hydrated (8 glasses water)",
                "• Regular exercise 30 min/day"
            )
            BMICategory.OVERWEIGHT -> listOf(
                "• Reduce portion sizes by 20%",
                "• Limit processed foods and sugars",
                "• Increase vegetables and fiber",
                "• 150 min moderate exercise/week",
                "• Track daily calorie intake"
            )
            BMICategory.OBESE -> listOf(
                "• Consult healthcare provider",
                "• Create calorie deficit (500-1000/day)",
                "• Focus on whole, unprocessed foods",
                "• 200+ min exercise per week",
                "• Consider professional guidance"
            )
        }
    }

    fun generateMealPlans() {
        _isLoadingMealPlans.value = true
        viewModelScope.launch {
            try {
                val meals = aiService.getMealPlans(
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category
                )
                _mealPlans.value = meals
            } catch (e: Exception) {
                // Fallback to placeholder meals
                _mealPlans.value = getPlaceholderMealPlans(_bmiData.value.category)
            } finally {
                _isLoadingMealPlans.value = false
            }
        }
    }

    private fun getPlaceholderMealPlansForCategory(mealCategory: com.bmi.aipowered.ui.screens.MealCategory): List<String> {
        return when (mealCategory) {
            com.bmi.aipowered.ui.screens.MealCategory.WEIGHT_LOSS -> listOf(
                "Breakfast: Oatmeal with berries",
                "Lunch: Grilled chicken salad",
                "Dinner: Baked fish with veggies",
                "Snacks: Apple, green tea"
            )
            com.bmi.aipowered.ui.screens.MealCategory.WEIGHT_GAIN -> listOf(
                "Breakfast: Scrambled eggs with toast",
                "Lunch: Pasta with meat sauce",
                "Dinner: Beef stir-fry with rice",
                "Snacks: Greek yogurt, almonds"
            )
            com.bmi.aipowered.ui.screens.MealCategory.MUSCLE_GAIN -> listOf(
                "Breakfast: Protein pancakes",
                "Lunch: Chicken breast with quinoa",
                "Dinner: Salmon with sweet potato",
                "Snacks: Protein shake, trail mix"
            )
            com.bmi.aipowered.ui.screens.MealCategory.HEALTHY_MEALS -> listOf(
                "Breakfast: Greek yogurt with berries",
                "Lunch: Quinoa salad with vegetables",
                "Dinner: Grilled fish with brown rice",
                "Snacks: Fresh fruits, nuts"
            )
            com.bmi.aipowered.ui.screens.MealCategory.QUICK_MEALS -> listOf(
                "Breakfast: Smoothie bowl",
                "Lunch: Turkey wrap",
                "Dinner: One-pan chicken & veggies",
                "Snacks: Protein bar, banana"
            )
        }
    }

    private fun getPlaceholderMealPlans(category: BMICategory): List<String> {
        // Return varied meals each time
        val mealSets = when (category) {
            BMICategory.UNDERWEIGHT -> listOf(
                listOf("Breakfast: Oatmeal with nuts & honey", "Lunch: Grilled chicken with rice", "Dinner: Salmon with sweet potato", "Snacks: Protein smoothie, trail mix"),
                listOf("Breakfast: Scrambled eggs with toast", "Lunch: Pasta with meat sauce", "Dinner: Beef stir-fry with rice", "Snacks: Greek yogurt, almonds"),
                listOf("Breakfast: Pancakes with peanut butter", "Lunch: Chicken wrap with avocado", "Dinner: Pork chops with potatoes", "Snacks: Cheese, crackers")
            )
            BMICategory.NORMAL -> listOf(
                listOf("Breakfast: Greek yogurt with berries", "Lunch: Quinoa salad with vegetables", "Dinner: Lean protein with vegetables", "Snacks: Fresh fruits, nuts"),
                listOf("Breakfast: Whole grain toast with eggs", "Lunch: Turkey sandwich with salad", "Dinner: Grilled fish with brown rice", "Snacks: Apple, hummus"),
                listOf("Breakfast: Smoothie bowl with granola", "Lunch: Chicken Caesar salad", "Dinner: Vegetable stir-fry with tofu", "Snacks: Mixed nuts, banana")
            )
            BMICategory.OVERWEIGHT -> listOf(
                listOf("Breakfast: Egg whites with vegetables", "Lunch: Grilled fish with salad", "Dinner: Turkey with steamed veggies", "Snacks: Apple, low-fat yogurt"),
                listOf("Breakfast: Oatmeal with berries", "Lunch: Chicken breast with quinoa", "Dinner: Baked cod with asparagus", "Snacks: Carrots, cucumber"),
                listOf("Breakfast: Protein smoothie", "Lunch: Turkey lettuce wraps", "Dinner: Zucchini noodles with marinara", "Snacks: Berries, green tea")
            )
            BMICategory.OBESE -> listOf(
                listOf("Breakfast: Protein shake with fiber", "Lunch: Lean protein with vegetables", "Dinner: Baked chicken with greens", "Snacks: Vegetables, water"),
                listOf("Breakfast: Scrambled eggs (2) with spinach", "Lunch: Grilled chicken salad", "Dinner: Steamed fish with broccoli", "Snacks: Celery sticks"),
                listOf("Breakfast: Greek yogurt (low-fat)", "Lunch: Turkey breast with salad", "Dinner: Vegetable soup with lean meat", "Snacks: Water, herbal tea")
            )
        }
        return mealSets.random()
    }

    fun sendChatMessage(message: String) {
        val userMessage = ChatMessage(message, isUser = true)
        _chatMessages.value = _chatMessages.value + userMessage
        _isLoadingChat.value = true
        
        viewModelScope.launch {
            try {
                val response = aiService.getChatResponse(
                    message = message,
                    bmi = _bmiData.value.bmi,
                    category = _bmiData.value.category
                )
                val aiMessage = ChatMessage(response, isUser = false)
                _chatMessages.value = _chatMessages.value + aiMessage
            } catch (e: Exception) {
                val errorMessage = ChatMessage(
                    "I'm having trouble right now. Please try again later.",
                    isUser = false
                )
                _chatMessages.value = _chatMessages.value + errorMessage
            } finally {
                _isLoadingChat.value = false
            }
        }
    }
}
