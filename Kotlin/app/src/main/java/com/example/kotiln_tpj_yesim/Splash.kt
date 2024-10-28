package com.example.kotiln_tpj_yesim


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotiln_tpj_yesim.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animatedText = binding.animatedText
        animatedText.visibility = TextView.VISIBLE

        val textAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f
        )
        textAnimation.duration = 1500
        animatedText.startAnimation(textAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}