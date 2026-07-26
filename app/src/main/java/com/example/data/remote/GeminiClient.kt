package com.example.data.remote

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun queryAiAssistant(userPrompt: String, systemPrompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "IA Médicale Dialyse: ${getFallbackAiResponse(userPrompt)}"
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", userPrompt))
                        })
                    })
                }
                put("contents", contentsArray)

                val sysInstruction = JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemPrompt))
                    })
                }
                put("systemInstruction", sysInstruction)
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (response.isSuccessful && responseString.isNotBlank()) {
                val jsonResponse = JSONObject(responseString)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", getFallbackAiResponse(userPrompt))
                    }
                }
            }

            return@withContext "IA Médicale Dialyse: ${getFallbackAiResponse(userPrompt)}"
        } catch (e: Exception) {
            return@withContext "IA Médicale Dialyse (Mode Hors-Ligne): ${getFallbackAiResponse(userPrompt)}"
        }
    }

    private fun getFallbackAiResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("potassium") || lower.contains("k+") ->
                "Le potassium (K+) est un électrolyte crucial chez le patient hémodialysé. Un taux élevé (> 5.5 meq/L) représente un risque d'arythmie cardiaque. Évitez les bananes, dattes, figues séchées, pommes de terre non trempées, et sauces tomates concentrées."
            lower.contains("poids") || lower.contains("weight") ->
                "La prise de poids interdialytique recommandée ne doit pas dépasser 4.5% à 5% du poids sec (environ 2.0 kg à 2.5 kg maximum entre deux séances). Une prise de poids excessive augmente la charge cardiaque et complique la séance de dialyse."
            lower.contains("tension") || lower.contains("blood pressure") || lower.contains("bp") ->
                "Une tension artérielle bien contrôlée protège votre accès vasculaire (fistule) et votre cœur. Signalez immédiatement tout épisode de vertige (hypotension) après la dialyse ou tout pic hypertensif (> 160/90) à domicile."
            lower.contains("lab") || lower.contains("résultat") || lower.contains("hémoglobine") ->
                "Analyse des résultats: Votre taux d'hémoglobine cible est de 10 à 12 g/dL. Une valeur sous 10 g/dL indique une anémie fréquente en insuffisance rénale, traitée par des injections d'EPO et du fer en dialyse."
            else ->
                "Conseil médical dialyse: Veillez à respecter scrupuleusement la restriction hydrique recommandée par votre néphrologue, prenez votre chélateur de phosphore (Renagel) strictement au milieu des repas, et protégez votre bras à fistule."
        }
    }
}
