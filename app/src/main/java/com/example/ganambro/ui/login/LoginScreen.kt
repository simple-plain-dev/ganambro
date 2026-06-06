package com.example.ganambro.ui.login

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Login screen with a Google Sign-In WebView and a Skip button.
 *
 * Note (MVP): Google may block OAuth in WebView. If so, the screen still allows skip.
 * No back button — Peserta can only go forward to Menu.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(onLoginResult: (LoginResult) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // WebView for Google Sign-In
        Box(modifier = Modifier.weight(1f)) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        webViewClient = object : WebViewClient() {
                            // Detect successful login by URL patterns
                            override fun onPageFinished(view: WebView?, url: String?) {
                                if (url?.contains("accounts.google.com/signin/oauth/consent") == false
                                    && url?.contains("google.com") == true
                                ) {
                                    onLoginResult(LoginResult.Sukses(null))
                                }
                            }
                        }
                        loadUrl("https://accounts.google.com/signin")
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Skip button
        Button(
            onClick = { onLoginResult(LoginResult.Anonim) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Skip — Masuk sebagai Anonim")
        }
    }
}
