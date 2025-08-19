package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewSlideTextBinding

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/11/21 12:43 PM
 */
// import kotlinx.android.synthetic.main.view_slide_text.view.*
class SlideTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mDistance = 0f
    private var mLastViewId = 0
    private val duration: Long = 200
    private var mListener: Listener? = null
    private lateinit var mBinding: ViewSlideTextBinding

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        mBinding = ViewSlideTextBinding.inflate(LayoutInflater.from(context), this, true)
        //LayoutInflater.from(context).inflate(R.layout.view_slide_text, this, true)
        setOpenVar()
        mLastViewId = mBinding.tvVideo.id

        mBinding.tvTake.post {
            onClickTake(mBinding.tvTake)
        }

        mBinding.tvVideo.setOnClickListener {
            onClickVideo(it as TextView)
        }

        mBinding.tvTake.setOnClickListener {
            onClickTake(it as TextView)
        }

        mBinding.tvStyle.setOnClickListener {
            if (mLastViewId == it.id) return@setOnClickListener

            val layoutParams: ConstraintLayout.LayoutParams =
                mBinding.tvTake.layoutParams as ConstraintLayout.LayoutParams
            mDistance = (layoutParams.leftMargin + it.width).toFloat()

            if (mLastViewId == mBinding.tvVideo.id) mDistance += (layoutParams.leftMargin + it.width).toFloat()

            startSwitchModeAnimation(false)
            mLastViewId = it.id
            setNormal(mBinding.tvStyle)
            mListener?.onClickMenuListener(2)
        }
    }

    lateinit var mTvStyle: TextView
    lateinit var mTvTake: TextView
    private fun setOpenVar() {
        mTvStyle = mBinding.tvStyle
        mTvTake = mBinding.tvTake
    }

    fun performClickStyle() {
        mBinding.tvStyle.post {
            postDelayed({
                mBinding.tvStyle.performClick()
            }, duration + 1)
        }
    }

    private fun onClickTake(it: TextView) {
        if (mLastViewId == it.id) return

        val layoutParams: ConstraintLayout.LayoutParams =
            mBinding.tvTake.layoutParams as ConstraintLayout.LayoutParams
        mDistance = (layoutParams.leftMargin + it.width).toFloat()

        if (mLastViewId == mBinding.tvStyle.id)
            startSwitchModeAnimation(true)
        else
            startSwitchModeAnimation(false)
        mLastViewId = it.id
        setNormal(it)
        mListener?.onClickMenuListener(1)
    }

    private fun onClickVideo(it: TextView) {
        if (mLastViewId == it.id) return

        val layoutParams: ConstraintLayout.LayoutParams =
            mBinding.tvTake.layoutParams as ConstraintLayout.LayoutParams
        mDistance = (layoutParams.leftMargin + it.width).toFloat()
        if (mLastViewId == mBinding.tvStyle.id) mDistance += (layoutParams.leftMargin + it.width).toFloat()

        startSwitchModeAnimation(true)
        mLastViewId = it.id
        setNormal(it)
        mListener?.onClickMenuListener(0)
    }

    private fun setNormal(view: TextView) {
        mBinding.tvTake.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        mBinding.tvVideo.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        //mBinding.tvStyle.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)

        view.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    }

    private fun startSwitchModeAnimation(isRight: Boolean) {
        if (isRight) {
            mBinding.tvTake.animate().translationXBy(mDistance).setDuration(duration).start()
            mBinding.tvVideo.animate().translationXBy(mDistance).setDuration(duration).start()
            //mBinding.tvStyle.animate().translationXBy(mDistance).setDuration(duration).start()
        } else {
            mBinding.tvTake.animate().translationXBy(-mDistance).setDuration(duration).start()
            mBinding.tvVideo.animate().translationXBy(-mDistance).setDuration(duration).start()
            //mBinding.tvStyle.animate().translationXBy(-mDistance).setDuration(duration).start()
        }
    }

    interface Listener {
        open fun onClickMenuListener(position: Int)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun showText() {
        mBinding.gpText.visibility = View.VISIBLE
    }

    fun hideText() {
        mBinding.gpText.visibility = View.INVISIBLE
    }
}