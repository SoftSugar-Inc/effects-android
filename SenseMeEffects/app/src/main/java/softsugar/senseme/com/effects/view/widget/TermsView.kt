package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.widget.LinearLayout
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewTermsBinding

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/17/21 8:29 PM
 */
@Suppress("DEPRECATION")
class TermsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        initView(context)
    }
    private lateinit var mBinding: ViewTermsBinding
    
    private fun initView(context: Context) {
        mBinding = ViewTermsBinding.inflate(LayoutInflater.from(context), this, true)
        // LayoutInflater.from(context).inflate(R.layout.view_terms, this, true)
        mBinding.wvDocs.loadUrl("file:///android_asset/SenseME_Provisions_v1.0.html")
        mBinding.wvDocs.settings.textSize = WebSettings.TextSize.SMALLER
        mBinding.tvBackBtn.setOnClickListener {
            visibility = View.INVISIBLE
        }
    }

}