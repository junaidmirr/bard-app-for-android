package com.blunt.bardai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blunt.bardai.R

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var splashImage: ImageView
    private lateinit var fadeIn: Animation
    private val FILE_CHOOSER_RESULT_CODE = 1
    private var fileUploadCallback: ValueCallback<Array<android.net.Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }

        webView = findViewById(R.id.webview)
        splashImage = findViewById(R.id.splashImage)
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        fadeIn.repeatCount = Animation.INFINITE
        fadeIn.repeatMode = Animation.REVERSE

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<android.net.Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                fileUploadCallback = filePathCallback
                val intent = fileChooserParams?.createIntent()
                if (intent != null) {
                    startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE)
                }
                return true
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://bard.google.com/chat")

        webView.visibility = View.INVISIBLE
        splashImage.visibility = View.VISIBLE
        splashImage.startAnimation(fadeIn)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val progress = webView.progress
                if (progress == 100) {
                    webView.visibility = View.VISIBLE
                    splashImage.visibility = View.GONE
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, continue with the app
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                val result = data?.data?.let { arrayOf(it) }
                fileUploadCallback?.onReceiveValue(result)
                fileUploadCallback = null
            } else {
                fileUploadCallback?.onReceiveValue(null)
                fileUploadCallback = null
            }
        }
    }
}
