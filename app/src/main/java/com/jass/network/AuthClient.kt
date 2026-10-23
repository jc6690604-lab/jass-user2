package com.jass.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object AuthClient {
    private const val SERVER = "http://51.21.131.7:3000"
    private val client = OkHttpClient()

    fun validateKey(key: String, deviceId: String): Boolean {
        return try {
            val json = """{"key":"$key","device":"$deviceId"}"""
            val body = json.toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$SERVER/connect")
                .post(body)
                .build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            response.isSuccessful && responseBody.contains("success")
        } catch (e: Exception) {
            false
        }
    }
}
