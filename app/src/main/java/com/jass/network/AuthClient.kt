package com.jass.network

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object AuthClient {
    private const val SERVER = "http://51.21.131.7:3000"
    private val client = OkHttpClient()
    private val JSON_TYPE = MediaType.parse("application/json; charset=utf-8")

    data class AuthResult(val valid: Boolean, val reason: String = "", val remainingDays: Int = 0, val features: List<String> = emptyList())

    fun validate(key: String, deviceId: String, callback: (AuthResult) -> Unit) {
        val body = JSONObject().put("key", key).put("deviceId", deviceId).toString()
        val request = Request.Builder().url("$SERVER/connect").post(RequestBody.create(JSON_TYPE, body)).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { callback(AuthResult(valid = false, reason = "network_error")) }
            override fun onResponse(call: Call, response: Response) {
                try {
                    val json = JSONObject(response.body()!!.string())
                    if (json.getBoolean("valid")) {
                        val features = mutableListOf<String>()
                        val arr = json.optJSONArray("features")
                        arr?.let { for (i in 0 until it.length()) features.add(it.getString(i)) }
                        callback(AuthResult(valid = true, remainingDays = json.getInt("remainingDays"), features = features))
                    } else { callback(AuthResult(valid = false, reason = json.getString("reason"))) }
                } catch (e: Exception) { callback(AuthResult(valid = false, reason = "parse_error")) }
            }
        })
    }
}
