package softsugar.senseme.com.effects.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * @Description WebView封装
 * @Author Lu Guoqiang
 * @Time 12/9/20 8:42 PM
 */
@SuppressLint("SetJavaScriptEnabled")
object WebViewInit {

    fun initWebSettings(webView: WebView) {
        with(webView) {
            isHorizontalScrollBarEnabled = false
            isScrollbarFadingEnabled = true

            overScrollMode = View.OVER_SCROLL_NEVER
        }

        with(webView.settings) {
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            defaultTextEncodingName = "utf-8"
            javaScriptCanOpenWindowsAutomatically = true
            userAgentString += "WebViewD"

            builtInZoomControls = false
            displayZoomControls = false
            setSupportZoom(false)

            allowFileAccess = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
            allowContentAccess = true

            //setAppCacheEnabled(true)
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    fun onDestroy(webView: WebView?) {
        webView?.run {
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            clearHistory()
            (parent as ViewGroup).removeView(this)
            destroy()
        }
    }
}

class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
    ): Boolean {
        val url = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            request?.url.toString()
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
        return when {
            url.startsWith("http://") || url.startsWith("https://") -> {
                view?.loadUrl(url)
                false
            }
            else -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    view?.context?.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }
        }
    }
}
