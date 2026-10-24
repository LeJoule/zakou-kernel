package com.zakou.kernel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MainActivity extends Activity {

    private static final String APP_ORIGIN = "https://appassets.androidplatform.net/";
    private static final String APP_HOST = "appassets.androidplatform.net";
    private static final int CAMERA_PERMISSION_REQUEST = 41;

    private WebView webView;
    private View loadingView;
    private boolean pageReady;
    private PermissionRequest pendingWebPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.parseColor("#120A14"));
        getWindow().setNavigationBarColor(Color.parseColor("#120A14"));

        WebView.setWebContentsDebuggingEnabled(false);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.BLACK);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setSupportMultipleWindows(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setUserAgentString(settings.getUserAgentString() + " ZakouKernel/1.0");

        webView.addJavascriptInterface(new NativeBridge(), "ZakouNative");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (request == null) return;
                boolean wantsVideo = false;
                for (String resource : request.getResources()) {
                    if (PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(resource)) {
                        wantsVideo = true;
                        break;
                    }
                }
                if (!wantsVideo) {
                    request.deny();
                    return;
                }
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                    } catch (Exception error) {
                        request.deny();
                    }
                } else {
                    if (pendingWebPermission != null) {
                        request.deny();
                        return;
                    }
                    pendingWebPermission = request;
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri != null && APP_HOST.equals(uri.getHost())) {
                    return serveAsset(uri.getPath());
                }
                return null;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri == null) return false;
                if (APP_HOST.equals(uri.getHost())) return false;
                String scheme = uri.getScheme();
                if (!"http".equals(scheme) && !"https".equals(scheme)) return false;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    return true;
                } catch (Exception error) {
                    return false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (pageReady) return;
                pageReady = true;
                if (loadingView != null) {
                    loadingView.animate()
                        .alpha(0f)
                        .setDuration(240)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                loadingView.setVisibility(View.GONE);
                            }
                        })
                        .start();
                }
            }
        });

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.parseColor("#120A14"));
        root.addView(webView, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ));

        TextView splash = new TextView(this);
        splash.setText("杂口内核\nZAKOU KERNEL // LOADING");
        splash.setTextColor(Color.parseColor("#FFD6E7"));
        splash.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        splash.setTypeface(Typeface.DEFAULT_BOLD);
        splash.setGravity(Gravity.CENTER);
        splash.setBackgroundColor(Color.parseColor("#120A14"));
        splash.setLetterSpacing(0.08f);
        root.addView(splash, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ));
        loadingView = splash;

        setContentView(root);
        webView.loadDataWithBaseURL(APP_ORIGIN, readAsset("index.html"), "text/html", "UTF-8", null);
    }

    private class NativeBridge {
        @JavascriptInterface
        public String getDeviceInfo() {
            try {
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                JSONObject info = new JSONObject();
                info.put("brand", Build.MANUFACTURER);
                info.put("model", Build.MODEL);
                info.put("os", Build.VERSION.RELEASE);
                info.put("sdk", Build.VERSION.SDK_INT);
                info.put("kernel", System.getProperty("os.version", "—"));
                info.put("width", metrics.widthPixels);
                info.put("height", metrics.heightPixels);
                info.put("density", metrics.density);
                String webview = "";
                if (Build.VERSION.SDK_INT >= 26) {
                    android.content.pm.PackageInfo packageInfo = WebView.getCurrentWebViewPackage();
                    if (packageInfo != null) webview = packageInfo.versionName;
                }
                info.put("webview", webview);
                return info.toString();
            } catch (Exception error) {
                return "{}";
            }
        }

        @JavascriptInterface
        public void openExternal(final String url) {
            if (url == null) return;
            final Uri uri = Uri.parse(url);
            final String scheme = uri.getScheme();
            if (!"https".equalsIgnoreCase(scheme) && !"http".equalsIgnoreCase(scheme)) return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception error) {
                        // No browser installed.
                    }
                }
            });
        }

        @JavascriptInterface
        public void setLandscape(final boolean landscape) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(
                        landscape
                            ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                            : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    );
                }
            });
        }
    }

    private WebResourceResponse serveAsset(String path) {
        if (path == null || path.length() == 0 || "/".equals(path)) path = "/index.html";
        String assetName = path.startsWith("/") ? path.substring(1) : path;
        String mime = mimeFor(assetName);
        try {
            InputStream stream = getAssets().open(assetName);
            return new WebResourceResponse(mime, "UTF-8", stream);
        } catch (Exception error) {
            byte[] body = "Not found".getBytes(StandardCharsets.UTF_8);
            return new WebResourceResponse(
                "text/plain",
                "UTF-8",
                404,
                "Not Found",
                new HashMap<String, String>(),
                new ByteArrayInputStream(body)
            );
        }
    }

    private String mimeFor(String name) {
        String lower = name.toLowerCase();
        if (lower.endsWith(".html")) return "text/html";
        if (lower.endsWith(".js") || lower.endsWith(".mjs")) return "application/javascript";
        if (lower.endsWith(".wasm")) return "application/wasm";
        if (lower.endsWith(".webmanifest") || lower.endsWith(".json")) return "application/manifest+json";
        if (lower.endsWith(".css")) return "text/css";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".woff2")) return "font/woff2";
        return "application/octet-stream";
    }

    private String readAsset(String name) {
        try {
            InputStream input = getAssets().open(name);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            input.close();
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception error) {
            return "<!doctype html><meta charset=utf-8><body style='background:#120a14;color:#ff9fca;font-family:sans-serif;padding:32px'>资源载入失败，请重新安装 APK。</body>";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != CAMERA_PERMISSION_REQUEST) return;
        PermissionRequest request = pendingWebPermission;
        pendingWebPermission = null;
        if (request == null) return;
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        try {
            if (granted) {
                request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
            } else {
                request.deny();
            }
        } catch (Exception error) {
            request.deny();
        }
    }

    @Override
    public void onPause() {
        if (webView != null) {
            webView.evaluateJavascript("window.__zakouPauseCamera && window.__zakouPauseCamera();", null);
            webView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    public void onBackPressed() {
        if (webView == null) {
            super.onBackPressed();
            return;
        }
        webView.evaluateJavascript(
            "(function(){" +
            "var modal=document.querySelector('.modal.open');" +
            "if(modal){var c=modal.querySelector('[data-close-modal]');if(c){c.click();return 'modal';}}" +
            "var menu=document.getElementById('hudMenu');" +
            "if(menu&&menu.classList.contains('open')){var m=document.getElementById('hudMenuClose');if(m){m.click();return 'menu';}}" +
            "var hud=document.getElementById('screen-hud');" +
            "if(hud&&hud.classList.contains('active')){var b=document.getElementById('hudBack');if(b){b.click();return 'hud';}}" +
            "return 'app';})();",
            new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String result) {
                    if (result == null || "null".equals(result) || result.contains("app")) {
                        finish();
                    }
                }
            }
        );
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
