package softsugar.senseme.com.effects.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewButtonBinding

//import kotlinx.android.synthetic.main.view_button.view.*
class HomeBtnView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mTitle: String? = null
    private var mColor: Int? = null
    private var mIcon: Int? = null
    private var mBinding: ViewButtonBinding

    init {
        attrs?.let { initCustomAttrs(it) }
        mBinding = ViewButtonBinding.inflate(LayoutInflater.from(context), this, true)
        initView(context)
    }

    private fun initView(context: Context) {
//        LayoutInflater.from(context).inflate(R.layout.view_button, this, true)
        mBinding.tvTitle.text = mTitle
        mIcon?.let { mBinding.ivIcon.setImageResource(it) }
        mColor?.let { mBinding.ivAction.setImageResource(it) }
    }

    private fun initCustomAttrs(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.HomeBtnView)
        mTitle = array.getString(R.styleable.HomeBtnView_title)
        mColor = array.getResourceId(R.styleable.HomeBtnView_bg, R.color.black)
        // mIcon = array.getResourceId(R.styleable.HomeBtnView_icon, R.drawable.ic_main_filter)
        array.recycle()
    }

}