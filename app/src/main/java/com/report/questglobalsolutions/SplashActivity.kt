package com.report.questglobalsolutions

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.report.questglobalsolutions.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private lateinit var binding: ActivitySplashBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedToken = Token(this@SplashActivity)
        val token = sharedToken.read()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent =
                if (!token.isNullOrEmpty()) {
                    Intent(this@SplashActivity, Home::class.java)
                } else {
                    sharedToken.del()
                    Intent(this@SplashActivity, MainActivity::class.java)
                }
            startActivity(intent)
            finish()
        }, 2000)
    }
}