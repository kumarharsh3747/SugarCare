package com.example.sugarfree

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {

    private const val API_KEY = "AIzaSyCkNZw4u-LnkaJeZaiMZUDCLuilMtQ0_go" // Secure API key storage

    // Define allowed health-related topics
    private val healthKeywords = listOf(
        "nutrition", "diet", "sugar intake", "diabetes", "calories",
        "exercise", "BMI", "fitness", "healthy eating", "blood sugar",
        "carbs", "insulin", "weight loss", "protein", "metabolism"
    )

    // Function to check if the prompt is health-related
    private fun isHealthRelated(prompt: String): Boolean {
        return healthKeywords.any { keyword -> prompt.contains(keyword, ignoreCase = true) }
    }

    suspend fun getResponse(prompt: String): Chat {
        if (!isHealthRelated(prompt)) {
            return Chat(
                prompt = "Sorry, I can only provide information on health and nutrition topics.",
                bitmap = null,
                isFromUser = false
            )
        }

        val generativeModel = GenerativeModel(modelName = "gemini-pro", apiKey = API_KEY)

        return try {
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }

//             val responseText = response.candidates
//                ?.firstOrNull()
//                ?.content?.parts
//                ?.firstOrNull()
//                ?.text ?: "error"

            // Validate AI response
            if (!isHealthRelated(response.text ?: "error",)) {
                return Chat(
                    prompt = "I'm focused on health and wellness. Please ask about nutrition, fitness, or related topics!",
                    bitmap = null,
                    isFromUser = false
                )
            }

            Chat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )
        } catch (e: Exception) {
            Chat(
                prompt = e.localizedMessage ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }
    }

    suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): Chat {
        if (!isHealthRelated(prompt)) {
            return Chat(
                prompt = "Sorry, I can only process health-related questions.",
                bitmap = null,
                isFromUser = false
            )
        }

        val generativeModel = GenerativeModel(modelName = "gemini-pro-vision", apiKey = API_KEY)

        return try {
            val inputContent = content {
                image(bitmap)  // Ensure proper image encoding
                text(prompt)
            }

            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(inputContent)
            }

//            val responseText = response.candidates
//                ?.firstOrNull()
//                ?.content?.parts
//                ?.firstOrNull()
//                ?.text ?: "error"

            // Validate AI response
            if (!isHealthRelated(response.text ?: "error",)) {
                return Chat(
                    prompt = "I'm focused on health and wellness. Please ask about nutrition, fitness, or related topics!",
                    bitmap = null,
                    isFromUser = false
                )
            }

            Chat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )
        } catch (e: Exception) {
            Chat(
                prompt = e.localizedMessage ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }
    }
}
