package com.zakou.kernel

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.webkit.WebViewAssetLoader
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var pendingPermissionRequest: PermissionRequest? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val request = pendingPermissionRequest
        pendingPermissionRequest = null
        try {
            if (granted && request != null) {
                request.grant(arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE))
            } else {
                request?.deny()
            }
        } catch (error: Exception) {
            // The WebView may have navigated away while the system dialog was open.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.parseColor("#120A14")
        window.navigationBarColor = Color.parseColor("#120A14")

        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)

        webView = WebView(this).apply {
            setBackgroundColor(Color.BLACK)
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                allowFileAccess = false
                allowContentAccess = false
                javaScriptCanOpenWindowsAutomatically = false
                setSupportMultipleWindows(false)
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                cacheMode = WebSettings.LOAD_DEFAULT
                userAgentString = userAgentString + " ZakouKernel/1.0"
            }
        }

        webView.addJavascriptInterface(NativeBridge(), "ZakouNative")

        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                if (request == null) return
                val wantsVideo = request.resources.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE)
                if (!wantsVideo) {
                    request.deny()
                    return
                }
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        request.grant(arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE))
                    } catch (error: Exception) {
                        request.deny()
                    }
                } else {
                    if (pendingPermissionRequest != null) {
                        request.deny()
                        return
                    }
                    pendingPermissionRequest = request
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                val url = request?.url ?: return null
                return assetLoader.shouldInterceptRequest(url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url ?: return false
                if (!request.isForMainFrame) return false
                if (url.host == "appassets.androidplatform.net") return false
                if (url.scheme != "http" && url.scheme != "https") return false
                return try {
                    startActivity(Intent(Intent.ACTION_VIEW, url))
                    true
                } catch (error: Exception) {
                    false
                }
            }
        }

        setContentView(webView)
        webView.loadUrl("https://appassets.androidplatform.net/assets/www/index.html")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                webView.evaluateJavascript(
                    """
                    (function () {
                      var modal = document.querySelector('.modal.open');
                      if (modal) {
                        var modalClose = modal.querySelector('[data-close-modal]');
                        if (modalClose) { modalClose.click(); return 'modal'; }
                      }
                      var menu = document.getElementById('hudMenu');
                      if (menu && menu.classList.contains('open')) {
                        var menuClose = document.getElementById('hudMenuClose');
                        if (menuClose) { menuClose.click(); return 'menu'; }
                      }
                      var hud = document.getElementById('screen-hud');
                      if (hud && hud.classList.contains('active')) {
                        var hudBack = document.getElementById('hudBack');
                        if (hudBack) { hudBack.click(); return 'hud'; }
                      }
                      return 'app';
                    })();
                    """.trimIndent()
                ) { result ->
                    if (result == null || result == "null" || result.contains("app")) {
                        finish()
                    }
                }
            }
        })
    }

    private inner class NativeBridge {
        @JavascriptInterface
        fun getDeviceInfo(): String {
            return try {
                val metrics: DisplayMetrics = resources.displayMetrics
                val info = JSONObject()
                info.put("brand", Build.MANUFACTURER)
                info.put("model", Build.MODEL)
                info.put("os", Build.VERSION.RELEASE)
                info.put("sdk", Build.VERSION.SDK_INT)
                info.put("kernel", System.getProperty("os.version") ?: "—")
                info.put("width", metrics.widthPixels)
                info.put("height", metrics.heightPixels)
                info.put("density", metrics.density)
                var webview = ""
                if (Build.VERSION.SDK_INT >= 26) {
                    val packageInfo = WebView.getCurrentWebViewPackage()
                    webview = packageInfo?.versionName ?: ""
                }
                info.put("webview", webview)
                info.toString()
            } catch (error: Exception) {
                "{}"
            }
        }

        @JavascriptInterface
        fun openExternal(url: String?) {
            if (url == null) return
            val uri = android.net.Uri.parse(url)
            val scheme = uri.scheme
            if (!"https".equals(scheme, ignoreCase = true) && !"http".equals(scheme, ignoreCase = true)) return
            runOnUiThread {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } catch (error: Exception) {
                    // No browser installed.
                }
            }
        }

        @JavascriptInterface
        fun setLandscape(landscape: Boolean) {
            runOnUiThread {
                requestedOrientation = if (landscape) {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        }
    }

    override fun onPause() {
        if (::webView.isInitialized) {
            webView.evaluateJavascript("window.__zakouPauseCamera && window.__zakouPauseCamera();", null)
            webView.onPause()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (::webView.isInitialized) webView.onResume()
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.loadUrl("about:blank")
            webView.destroy()
        }
        super.onDestroy()
    }
}
