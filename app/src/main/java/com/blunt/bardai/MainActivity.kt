package com.blunt.bardai
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.blunt.bardai.R

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var splashImage: ImageView
    private lateinit var fadeIn: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        webView = findViewById(R.id.webview)
        splashImage = findViewById(R.id.splashImage)
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Set repeat count and mode of animation
        fadeIn.repeatCount = Animation.INFINITE
        fadeIn.repeatMode = Animation.REVERSE

        // Load website
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://bard.google.com/chat")

        // Hide WebView until page loaded
        webView.visibility = View.INVISIBLE

        // Show splash screen
        splashImage.visibility = View.VISIBLE
        splashImage.startAnimation(fadeIn)

        // Check if page finished loading
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // Get the current progress of the web page loading
                val progress = webView.progress
                // If the web page is fully loaded
                if (progress == 100) {
                    // Show WebView and hide splash screen
                    webView.visibility = View.VISIBLE
                    splashImage.visibility = View.GONE
                }
            }
        }
    }
}
