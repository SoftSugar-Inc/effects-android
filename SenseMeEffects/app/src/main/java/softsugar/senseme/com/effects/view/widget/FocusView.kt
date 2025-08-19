package softsugar.senseme.com.effects.view.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewFocusBinding

class FocusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private lateinit var mBinding: ViewFocusBinding

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        mBinding = ViewFocusBinding.inflate(LayoutInflater.from(context), this, true)
        // LayoutInflater.from(context).inflate(R.layout.view_focus, this, true)
        val layoutParams = FrameLayout.LayoutParams(80, 80)
        mBinding.ivFucos.layoutParams = layoutParams
    }

    fun perFormSetMeteringArea(touchX: Float, touchY: Float) {
        val params = mBinding.ivFucos.layoutParams as FrameLayout.LayoutParams
        params.setMargins(touchX.toInt() - 50, touchY.toInt() - 50, 0, 0)
        mBinding.ivFucos.layoutParams = params
        mBinding.ivFucos.visibility = View.VISIBLE
        startAnim()
    }

    private fun startAnim() {
        val animatorSet = AnimatorSet()
        val animX = ObjectAnimator.ofFloat(mBinding.ivFucos, "scaleX", 1.5f, 1.2f)
        val animY = ObjectAnimator.ofFloat(mBinding.ivFucos, "scaleY", 1.5f, 1.2f)
        animatorSet.duration = 500
        animatorSet.play(animX).with(animY)
        animatorSet.start()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                mBinding.ivFucos.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }
}