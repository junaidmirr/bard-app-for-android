package com.blunt.bardai

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blunt.bardai.R

class MainActivity2 : AppCompatActivity() {

    private lateinit var webView: WebView

    private var uploadMessage: ValueCallback<Array<Uri>>? = null

    private val IMAGE_MIME_TYPES = arrayOf("image/jpeg", "image/png", "image/jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity2)

        webView = findViewById(R.id.webview)
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {

                if(uploadMessage != null) {
                    uploadMessage?.onReceiveValue(null)
                    uploadMessage = null
                }

                uploadMessage = filePathCallback

                val intent = fileChooserParams?.createIntent()

                try {
                    intent?.let {
                        startActivityForResult(it, FILE_REQUEST_CODE)
                    }
                } catch(e: ActivityNotFoundException) {
                    uploadMessage = null
                    Toast.makeText(this@MainActivity2, "Cannot open file chooser", Toast.LENGTH_LONG).show()
                    return false
                }

                return true
            }

        }

        webView.loadUrl("https://bard.google.com")
        webView.settings.javaScriptEnabled = true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == FILE_REQUEST_CODE) {

            if(uploadMessage == null) return

            val result = if(resultCode != RESULT_OK) {
                null
            } else {
                data?.data // URI of selected file
            }

            if(result != null) {

                // Check if image MIME type
                val mimeType = contentResolver.getType(result)

                if(IMAGE_MIME_TYPES.contains(mimeType)) {

                    // Image selected, upload
                    uploadMessage?.onReceiveValue(arrayOf(result))

                } else {

                    // Invalid image type
                    Toast.makeText(this, "Invalid image selected", Toast.LENGTH_SHORT).show()

                }

            } else {
                // No image selected
                uploadMessage?.onReceiveValue(null)
            }

            uploadMessage = null

        }

    }

    companion object {
        private const val FILE_REQUEST_CODE = 101
    }

}