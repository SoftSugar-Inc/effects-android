package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewTipBinding
import java.lang.ref.WeakReference

class TipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    companion object {
        private const val TAG = "TipView"
        // 默认提示3s
        const val TIME_TIP: Long = 3000

        // 各种样式
        const val TYPE_TEXT: Int = 0
        const val TYPE_SUCCESS: Int = 1
        const val TYPE_FAIL = 2
        const val TYPE_DOWNLOADING = 3
        // 不支持屏幕旋转的提示
        const val TYPE_GAN_TIP = 4
        // gan 无人脸 提示 5 秒
        const val TYPE_GAN_NO_FACE = 5
        const val TYPE_GAN_NO_NET = 6
        const val TYPE_TRY_ON_SHOES = 7
    }

    //import kotlinx.android.synthetic.main.view_tip.view.*
    private lateinit var mBinding: ViewTipBinding

    init {
        val startTime = System.currentTimeMillis()
        initView(context)
        visibility = View.INVISIBLE
        LogUtils.iTag(TAG, "init total cost time:" + (System.currentTimeMillis() - startTime))
    }

    private var mHideTipRunnable: HideTipRunnable? = null

    private fun initView(context: Context) {
        mBinding = ViewTipBinding.inflate(LayoutInflater.from(context), this, true)
        // LayoutInflater.from(context).inflate(R.layout.view_tip, this, true)
        mHideTipRunnable = HideTipRunnable(this)
        mBinding.tipViewProgressbar.indeterminateDrawable
            .setColorFilter(
                ContextCompat.getColor(context,
                R.color.color_f383fd), PorterDuff.Mode.MULTIPLY)
    }

    private class HideTipRunnable(instance: TipView) : Runnable {
        private val mReference: WeakReference<TipView> = WeakReference(instance)

        override fun run() {
            val tipView = mReference.get()
            tipView?.visibility = View.GONE
        }
    }

    fun hideTip() {
        visibility = View.GONE
        if (mHideTipRunnable != null)
            removeCallbacks(mHideTipRunnable)
    }

    // 隐藏gan旋转提示
    fun hideGanRotateTip() {
        if (mType == TYPE_GAN_TIP && visibility == View.VISIBLE) {
            visibility = View.GONE
            if (mHideTipRunnable != null)
                removeCallbacks(mHideTipRunnable)
        }
    }

    var mType = 0
    fun showTip(type: Int, text: CharSequence) {
        mType = type
        mBinding.tvTip.text = text
        mBinding.ivIcon.visibility = View.VISIBLE
        mBinding.tipViewProgressbar.visibility = View.GONE
        when (type) {
            TYPE_TEXT -> {
                mBinding.ivIcon.visibility = View.GONE
                showTipAutoHide()
            }
            TYPE_SUCCESS -> {
                mBinding.ivIcon.setImageResource(R.drawable.ic_tip_ok)
                showTipAutoHide()
            }
            TYPE_FAIL -> {
                mBinding.ivIcon.setImageResource(R.drawable.ic_tip_fail)
                showTipAutoHide()
            }
            TYPE_DOWNLOADING -> {
                removeCallbacks(mHideTipRunnable)
                showTip()
                mBinding.tipViewProgressbar.visibility = View.VISIBLE
                mBinding.ivIcon.visibility = View.GONE
            }
            TYPE_GAN_TIP -> {
                mBinding.ivIcon.setImageResource(R.drawable.ic_tip_fail)
                mBinding.ivIcon.visibility = View.VISIBLE
                removeCallbacks(mHideTipRunnable)
                showTip()
            }
            TYPE_GAN_NO_FACE -> {// gan提示 拍摄清晰人脸
                mBinding.ivIcon.setImageResource(R.drawable.ic_tip_fail)
                mBinding.ivIcon.visibility = View.VISIBLE
                removeCallbacks(mHideTipRunnable)
                showTip()
                visibility = View.VISIBLE
                postDelayed(mHideTipRunnable, 5000)
            }
            TYPE_GAN_NO_NET -> {// gan 无网络
                mBinding.ivIcon.setImageResource(R.drawable.ic_tip_fail)
                mBinding.ivIcon.visibility = View.VISIBLE
                removeCallbacks(mHideTipRunnable)
                showTip()
                visibility = View.VISIBLE
                postDelayed(mHideTipRunnable, 5000)
            }
            TYPE_TRY_ON_SHOES -> {// 光脚体验更加
                mBinding.ivIcon.setImageResource(R.drawable.ic_tip_shoes)
                mBinding.ivIcon.visibility = View.VISIBLE
                removeCallbacks(mHideTipRunnable)
                showTip()
                visibility = View.VISIBLE
                postDelayed(mHideTipRunnable, 3000)
            }
        }
    }

    private fun showTip() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
    }

    private fun showTipAutoHide() {
        visibility = View.VISIBLE
        removeCallbacks(mHideTipRunnable)
        postDelayed(mHideTipRunnable, TIME_TIP)
    }
}