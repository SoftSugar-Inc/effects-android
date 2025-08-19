package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.IntDef
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewBottomMenuBinding
import softsugar.senseme.com.effects.utils.RippleUtils

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/18/21 11:08 AM
 */
// import kotlinx.android.synthetic.main.view_bottom_menu.view.*
class BottomMenuView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mBinding: ViewBottomMenuBinding

    companion object {
        const val BLACK_STYLE = 2
        const val WRITE_STYLE = 3

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(BLACK_STYLE, WRITE_STYLE)
        annotation class Style
    }

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        mBinding = ViewBottomMenuBinding.inflate(LayoutInflater.from(context), this, true)
        //LayoutInflater.from(context).inflate(R.layout.view_bottom_menu, this, true)
        setOpenVar()
        mBinding.dtSticker.setOnClickListener {
            visibility = View.INVISIBLE
            mListener?.onClickSticker()
        }

        mBinding.dtEffect.setOnClickListener {
            mListener?.onClickBeauty()
        }

        mBinding.dtFilter.setOnClickListener {
            mListener?.onClickFilter()
        }
    }

    private fun setOpenVar() {
    }

    var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setGrayColor() {
        mBinding.dtSticker?.apply {
            setDrawableTop(R.drawable.ic_camera_sticker_gray)
            setTextColor(Color.parseColor("#4E4E4E"))
        }

        mBinding.dtFilter?.apply {
            setDrawableTop(R.drawable.ic_camera_filter_gray)
            setTextColor(Color.parseColor("#4E4E4E"))
        }

        mBinding.dtEffect?.apply {
            setDrawableTop(R.drawable.ic_camera_effect_gray)
            setTextColor(Color.parseColor("#4E4E4E"))
        }
    }

    fun setWriteColor() {
        mBinding.dtSticker?.apply {
            setDrawableTop(R.drawable.ic_camera_sticker)
            setTextColor(Color.parseColor("#ffffff"))
        }

        mBinding.dtFilter?.apply {
            setDrawableTop(R.drawable.ic_camera_filter)
            setTextColor(Color.parseColor("#ffffff"))
        }

        mBinding.dtEffect?.apply {
            setDrawableTop(R.drawable.ic_camera_effect)
            setTextColor(Color.parseColor("#ffffff"))
        }
    }

    fun setStyle(@Style style: Int) {
        when (style) {
            BLACK_STYLE -> {
                mBinding.clRoot.setBackgroundColor(Color.parseColor("#ffffff"))
            }
            WRITE_STYLE -> {
                mBinding.clRoot.setBackgroundColor(Color.parseColor("#00000000"))
            }
        }
    }

    interface Listener {
        fun onClickSticker()
        fun onClickMakeup()
        fun onClickFilter()
        fun onClickBeauty()
    }
}