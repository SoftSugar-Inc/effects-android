package softsugar.senseme.com.effects.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.utils.Constants.ASSET_PATH_TERMS
import softsugar.senseme.com.effects.webview.MyWebViewClient
import softsugar.senseme.com.effects.webview.WebViewInit

class TermsActivity : AppCompatActivity() {

    private lateinit var mWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        findViewById<TextView>(R.id.tv_tool_bar_title).text = "使用条款"
        mWebView = findViewById(R.id.mWebView)
        findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }

        WebViewInit.initWebSettings(mWebView)
        mWebView.webViewClient = MyWebViewClient()
        mWebView.loadUrl(ASSET_PATH_TERMS)
    }

    override fun onDestroy() {
        WebViewInit.onDestroy(mWebView)
        super.onDestroy()
    }

}