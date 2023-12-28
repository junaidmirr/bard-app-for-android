package com.blunt.bardai

import androidx.activity.ComponentActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view first
        setContentView(R.layout.main_activity)

        // Then hide the system UI
        hideSystemUI()

        val splashImage: ImageView = findViewById(R.id.splashImage)

        // Load animation
        val fadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Set animation listener to start the next activity after animation ends
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Start your main activity after the splash screen animation ends
                val intent = Intent(this@MainActivity, MainActivity2::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Start animation
        splashImage.startAnimation(fadeIn)
    }

    // Hide the system UI (status bar and navigation bar) for a fullscreen experience
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            // No need to check for Build.VERSION.SDK_INT >= Build.VERSION_CODES.S as it is already included in Build.VERSION_CODES.R
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
