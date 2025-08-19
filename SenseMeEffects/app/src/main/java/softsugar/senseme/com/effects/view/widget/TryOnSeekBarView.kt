package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.util.AttributeSet
import com.blankj.utilcode.util.LogUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.constraintlayout.widget.Group
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewTryOnSeekbarBinding

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/8/24 12:30 下午
 */
private const val TAG = "TryOnSeekBarView"

// import kotlinx.android.synthetic.main.view_try_on_seekbar.view.*
class TryOnSeekBarView : LinearLayout {

    private lateinit var mType: EffectType
    private var mState: State? = null
    var mIndex: Int = 0
    private lateinit var gpStrength: Group
    private lateinit var gpLight: Group
    private lateinit var mBinding: ViewTryOnSeekbarBinding

    constructor(context: Context, effectType: EffectType, index: Int) : super(context) {
        val startTime = System.currentTimeMillis()
        mIndex = index
        mState = when (effectType) {
            EffectType.TYPE_TRY_ON_CHUNXIAN -> {
                LipLineState()
            }
            EffectType.TYPE_TRY_ON_LIP -> {
                LipState()
            }
            EffectType.TYPE_TRY_ON_HAIR -> {
                HairState()
            }
            else -> {
                EyeShadowState()
            }
        }
        LogUtils.i("cost time mState:${System.currentTimeMillis() - startTime}")
        this.mType = effectType
        init(context)
        LogUtils.i("cost time:${System.currentTimeMillis() - startTime}")
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val startTime = System.currentTimeMillis()
        initView(context)

        setOpenVar()
        initData()
        initListener()
        LogUtils.i("init cost time:${System.currentTimeMillis() - startTime}")
    }

    private fun setOpenVar() {
        gpStrength = mBinding.gpStrength
        gpLight = mBinding.gpLight
    }

    private fun initListener() {
        mBinding.tbStrength.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                LogUtils.iTag(TAG, "onProgressChanged: ${mType.desc}")
                LogUtils.i(String.format(resources.getString(R.string.log_tryon_strength), "${mType.desc}", "$progress"))
                mListener?.onProgressChanged(
                    mType,
                    TryOnSeekBarType.TYPE_STRENGTH,
                    progress,
                    fromUser
                )
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        mBinding.tbLight.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                mListener?.onProgressChanged(
                    mType,
                    TryOnSeekBarType.TYPE_HIGHLIGHT,
                    progress,
                    fromUser
                )
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        mBinding.tbLipWidth.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                mListener?.onProgressChanged(
                    mType,
                    TryOnSeekBarType.TYPE_LINE_WIDTH_RATIO,
                    progress,
                    fromUser
                )
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    fun setSeekBarValue(strength: Float, light: Float, lineWidthRatio: Float) {
        LogUtils.iTag(TAG, "setSeekBarValue() called with: strength = $strength, light = $light")
        mBinding.tbStrength.setValue(strength)
        mBinding.tbLight.setValue(light)
        mBinding.tbLipWidth.setValue(lineWidthRatio)
    }

    fun setSeekBarValue(strength: Float) {
        mBinding.tbStrength.setValue(strength)
    }

    private fun initData() {
    }

    private fun initView(context: Context) {
        val startTime = System.currentTimeMillis()
        mBinding = ViewTryOnSeekbarBinding.inflate(LayoutInflater.from(context), this, true)
        // LayoutInflater.from(context).inflate(R.layout.view_try_on_seekbar, this, true)
        LogUtils.i("initView cost time:${System.currentTimeMillis() - startTime}")
    }

    fun setHidden() {
        mBinding.gpStrength.visibility = View.INVISIBLE
        mBinding.gpLight.visibility = View.INVISIBLE
    }

    // position position=0(质地) position = 1:颜色  position = null 为强度
    fun showSeekBar(index: Int?) {
        mState?.showSeekBar(this, index)
    }

    /* start *************************************/
    fun showSeekBarForLipLineState(index: Int?) {// 唇线（只有强度） 还有宽度
        LogUtils.iTag(TAG, "showSeekBarForLipLineState() called with: index = $index")
        visibility = View.VISIBLE
        mBinding.gpStrength.visibility = View.VISIBLE
        mBinding.gpLight.visibility = View.INVISIBLE

        mBinding.clLipWidth.visibility = View.VISIBLE
    }

    fun showSeekBarForEyeShadowState(index: Int?) {// 眼影（只有强度） 也是默认的
        LogUtils.iTag(TAG, "showSeekBarForEyeShadowState() called with: index = $index")
        visibility = View.VISIBLE
        mBinding.gpStrength.visibility = View.VISIBLE
        mBinding.gpLight.visibility = View.INVISIBLE

        mBinding.clLipWidth.visibility = View.INVISIBLE
    }

    fun showSeekBarForLipState(position: Int?) {// 口红（只有强度）(质地-光泽度  颜色-强度)
        LogUtils.iTag(TAG, "showSeekBarForLipState() called with: status = $position")
        visibility = View.VISIBLE
        mBinding.gpStrength.visibility = View.VISIBLE
        mBinding.gpLight.visibility = View.INVISIBLE

        when (position) {
            0 -> {
                gpLight.visibility = View.VISIBLE
                mBinding.gpStrength.visibility = View.INVISIBLE
            }
            else -> {
                mBinding.gpStrength.visibility = View.VISIBLE
            }
        }
        mBinding.clLipWidth.visibility = View.INVISIBLE
    }

    fun showSeekBarForHairState(index: Int?) {// 染发
        visibility = View.VISIBLE
        LogUtils.iTag(TAG, "showSeekBarForHairState() called with: index = $index")
        mBinding.gpStrength.visibility = View.INVISIBLE
        gpLight.visibility = View.VISIBLE
        mBinding.gpStrength.visibility = View.VISIBLE
        gpLight.visibility = View.INVISIBLE

        mBinding.clLipWidth.visibility = View.INVISIBLE
    }
    /* end ***************************************/

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onProgressChanged(
            type: EffectType,
            seekType: TryOnSeekBarType,
            progress: Float,
            fromUser: Boolean
        )
    }

    interface State {
        // 展示
        fun showSeekBar(view: TryOnSeekBarView, position: Int?)
    }

    class LipLineState : State {// 唇线
        override fun showSeekBar(view: TryOnSeekBarView, position: Int?) {
            view.showSeekBarForLipLineState(position)
        }
    }

    class EyeShadowState : State {// 眼影
        override fun showSeekBar(view: TryOnSeekBarView, position: Int?) {
            view.showSeekBarForEyeShadowState(position)
        }
    }

    class LipState : State {// 口红
        override fun showSeekBar(view: TryOnSeekBarView, position: Int?) {
            view.showSeekBarForLipState(position)
        }
    }

    class HairState : State {// 头发
        override fun showSeekBar(view: TryOnSeekBarView, position: Int?) {
            view.showSeekBarForHairState(position)
        }
    }
}