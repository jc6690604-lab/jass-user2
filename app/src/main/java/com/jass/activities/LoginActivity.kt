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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val keyInput = findViewById<EditText>(R.id.keyInput)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val key = keyInput.text.toString().trim()
            if (key.isEmpty()) { Toast.makeText(this, "Key daalo", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            AuthClient.validate(key, DeviceUtils.getDeviceId(this)) { result ->
                runOnUiThread {
                    if (result.valid) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Invalid key: ${result.reason}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
