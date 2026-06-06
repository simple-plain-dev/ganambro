package com.example.ganambro.ui.ujian

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ganambro.exit.ExitContext
import com.example.ganambro.exit.ExitCoordinator

/**
 * Halaman Ujian yang memuat Situs Ujian (Google Sites) di WebView.
 *
 * Toolbar di atas WebView: Home, Back, Forward, Exit.
 * Tidak ada URL bar — navigasi hanya via tombol.
 * Error handling: jika internet putus, tampilkan pesan + tombol keluar.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun UjianScreen(
    examUrl: String,
    exitCoordinator: ExitCoordinator,
    onExitApp: () -> Unit,
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var isError by remember { mutableStateOf(false) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A1A1A))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ToolbarButton("⟲", enabled = true) {
                webView?.loadUrl(examUrl)
            }
            ToolbarButton("←", enabled = canGoBack) {
                webView?.goBack()
            }
            ToolbarButton("→", enabled = canGoForward) {
                webView?.goForward()
            }
            // Spacer to push Exit to the right
            Box(modifier = Modifier.weight(1f))
            ToolbarButton("✕", enabled = true, color = Color.Red) {
                exitCoordinator.exit(ExitContext.Ujian)
                onExitApp()
            }
        }

        // WebView atau error
        if (isError) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Koneksi internet terputus",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                    Text(
                        text = "Periksa koneksi Anda dan coba lagi.",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                    Button(
                        onClick = onExitApp,
                        modifier = Modifier.padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    ) {
                        Text("Keluar")
                    }
                }
            }
        } else {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowFileAccess = false
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                isError = false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                errorCode: Int,
                                description: String?,
                                failingUrl: String?,
                            ) {
                                isError = true
                            }

                            @Suppress("DEPRECATION")
                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: android.webkit.WebResourceError?,
                            ) {
                                if (request?.isForMainFrame == true) {
                                    isError = true
                                }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                canGoBack = view?.canGoBack() ?: false
                                canGoForward = view?.canGoForward() ?: false
                            }
                        }
                        loadUrl(examUrl)
                    }.also { webView = it }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun ToolbarButton(
    text: String,
    enabled: Boolean,
    color: Color = Color.White,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = color,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Gray,
        ),
    ) {
        Text(text = text, fontSize = 20.sp, fontFamily = FontFamily.Monospace)
    }
}
