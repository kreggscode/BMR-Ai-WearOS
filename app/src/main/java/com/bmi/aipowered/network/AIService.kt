package com.kreggscode.wearosbmi.network

import com.kreggscode.wearosbmi.model.BMICategory
import com.kreggscode.wearosbmi.utils.AnalysisGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AIService {
    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    suspend fun getAnalysis(
        bmi: Float,
        category: BMICategory,
        height: Float,
        weight: Float
    ): String = withContext(Dispatchers.IO) {
        try {
            val prompt = generatePrompt(bmi, category.label, height, weight)
            val messagesArray = org.json.JSONArray().apply {
                put(org.json.JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            }
            val jsonBody = JSONObject().apply {
                put("model", "openai")
                put("messages", messagesArray)
                put("temperature", 1.0)
                put("max_tokens", 300)
            }
            
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url("https://text.pollinations.ai/openai")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string() ?: throw Exception("Empty response")
                    val jsonResponse = JSONObject(responseBody)
                    jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                } else {
                    throw Exception("API Error: ${it.code}")
                }
            }
        } catch (e: Exception) {
            // Fallback to intelligent placeholder on any error
            AnalysisGenerator.generateAnalysis(bmi, category)
        }
    }

    suspend fun getDietPlans(
        bmi: Float,
        category: BMICategory
    ): List<String> = withContext(Dispatchers.IO) {
        try {
            // Use POST with JSON for better handling
            val prompt = "Give 5 diet tips for $category BMI. Each tip max 50 chars. Format: tip1, tip2, tip3, tip4, tip5"
            val messagesArray = org.json.JSONArray().apply {
                put(org.json.JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            }
            val jsonBody = JSONObject().apply {
                put("model", "openai")
                put("messages", messagesArray)
                put("temperature", 1.0)
                put("max_tokens", 200)
            }
            
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url("https://text.pollinations.ai/openai")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string() ?: throw Exception("Empty response")
                    val jsonResponse = JSONObject(responseBody)
                    val content = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    
                    // Parse response into list of diet plan items
                    val parsed = content.split(",", "\n", "•", "-", "1.", "2.", "3.", "4.", "5.")
                        .map { line -> line.trim() }
                        .filter { line -> line.isNotEmpty() && line.length > 5 && !line.matches(Regex("^\\d+$")) }
                        .take(5)
                    if (parsed.isNotEmpty()) parsed else throw Exception("No valid items")
                } else {
                    throw Exception("API Error: ${it.code}")
                }
            }
        } catch (e: Exception) {
            // Return empty list, will use placeholder
            emptyList()
        }
    }

    private fun generatePrompt(bmi: Float, category: String, height: Float, weight: Float): String {
        return """
Provide a brief, personalized health analysis for a smartwatch display.

User Details:
- BMI: $bmi
- Category: $category
- Height: ${height}cm
- Weight: ${weight}kg

Please include:
1. Brief health assessment (2-3 sentences)
2. Top 3-4 actionable recommendations
3. Important note about BMI limitations

Keep the response concise and suitable for a small smartwatch screen.
Use emojis for visual appeal: 📊 💪 🎯 ⚠️
        """.trimIndent()
    }

    suspend fun getDietPlansForCategory(
        bmi: Float,
        category: BMICategory,
        dietCategory: String
    ): List<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = "Give 5 diet tips for $dietCategory plan. BMI: $bmi ($category). Each tip max 50 chars. Format: tip1, tip2, tip3, tip4, tip5"
            val messagesArray = org.json.JSONArray().apply {
                put(org.json.JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            }
            val jsonBody = JSONObject().apply {
                put("model", "openai")
                put("messages", messagesArray)
                put("temperature", 1.0)
                put("max_tokens", 200)
            }
            
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url("https://text.pollinations.ai/openai")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string() ?: throw Exception("Empty response")
                    val jsonResponse = JSONObject(responseBody)
                    val content = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    
                    val parsed = content.split(",", "\n", "•", "-", "1.", "2.", "3.", "4.", "5.")
                        .map { line -> line.trim() }
                        .filter { line -> line.isNotEmpty() && line.length > 5 && !line.matches(Regex("^\\d+$")) }
                        .take(5)
                    if (parsed.isNotEmpty()) parsed else throw Exception("No valid items")
                } else {
                    throw Exception("API Error: ${it.code}")
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMealPlansForCategory(
        bmi: Float,
        category: BMICategory,
        mealCategory: String
    ): List<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = "Give 4 meals for $mealCategory plan. BMI: $bmi ($category). Format: Breakfast meal, Lunch meal, Dinner meal, Snacks. Max 40 chars each."
            val messagesArray = org.json.JSONArray().apply {
                put(org.json.JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            }
            val jsonBody = JSONObject().apply {
                put("model", "openai")
                put("messages", messagesArray)
                put("temperature", 1.0)
                put("max_tokens", 200)
            }
            
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url("https://text.pollinations.ai/openai")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string() ?: throw Exception("Empty response")
                    val jsonResponse = JSONObject(responseBody)
                    val content = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    
                    val parsed = content.split(",", "\n", ":", "•", "-", "Breakfast", "Lunch", "Dinner", "Snacks")
                        .map { line -> line.trim() }
                        .filter { line -> line.isNotEmpty() && line.length > 5 }
                        .take(4)
                    if (parsed.isNotEmpty()) parsed else throw Exception("No valid items")
                } else {
                    throw Exception("API Error: ${it.code}")
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMealPlans(
        bmi: Float,
        category: BMICategory
    ): List<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = "Give 4 meals for $category BMI. Format: Breakfast meal, Lunch meal, Dinner meal, Snacks. Max 40 chars each."
            val messagesArray = org.json.JSONArray().apply {
                put(org.json.JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            }
            val jsonBody = JSONObject().apply {
                put("model", "openai")
                put("messages", messagesArray)
                put("temperature", 1.0)
                put("max_tokens", 200)
            }
            
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url("https://text.pollinations.ai/openai")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string() ?: throw Exception("Empty response")
                    val jsonResponse = JSONObject(responseBody)
                    val content = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    
                    val parsed = content.split(",", "\n", ":", "•", "-", "Breakfast", "Lunch", "Dinner", "Snacks")
                        .map { line -> line.trim() }
                        .filter { line -> line.isNotEmpty() && line.length > 5 }
                        .take(4)
                    if (parsed.isNotEmpty()) parsed else throw Exception("No valid items")
                } else {
                    throw Exception("API Error: ${it.code}")
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getChatResponse(
        message: String,
        bmi: Float,
        category: BMICategory
    ): String = withContext(Dispatchers.IO) {
        try {
            val prompt = "You are a health assistant. User BMI: $bmi ($category). Question: $message. Give a helpful answer in max 100 characters."
            val messagesArray = org.json.JSONArray().apply {
                put(org.json.JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            }
            val jsonBody = JSONObject().apply {
                put("model", "openai")
                put("messages", messagesArray)
                put("temperature", 1.0)
                put("max_tokens", 150)
            }
            
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url("https://text.pollinations.ai/openai")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string() ?: throw Exception("Empty response")
                    val jsonResponse = JSONObject(responseBody)
                    val content = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    content.take(200)
                } else {
                    throw Exception("API Error: ${it.code}")
                }
            }
        } catch (e: Exception) {
            "I understand. How can I help you with your BMI and health goals?"
        }
    }

}
