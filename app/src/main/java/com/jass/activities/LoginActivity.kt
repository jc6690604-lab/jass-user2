package com.jass.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jass.R
import com.jass.network.AuthClient
import com.jass.utils.DeviceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val keyInput = findViewById<EditText>(R.id.keyInput)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val key = keyInput.text.toString().trim()
            if (key.isEmpty()) {
                Toast.makeText(this, "Enter key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val deviceId = DeviceUtils.getDeviceId(this)
            CoroutineScope(Dispatchers.IO).launch {
                val success = AuthClient.validateKey(key, deviceId)
                withContext(Dispatchers.Main) {
                    if (success) {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid key", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
