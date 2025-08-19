package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.Color
import android.text.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.button.MaterialButton
import com.softsugar.stmobile.model.STColor
import com.softsugar.stmobile.model.STEffectsTryOnRegionInfo
import com.skydoves.colorpickerview.listeners.ColorListener
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewTryonColorPickerBinding
import softsugar.senseme.com.effects.utils.ReplaceViewHelper
import java.lang.Exception
import java.lang.StringBuilder

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/8/24 4:08 下午
 */
private const val TAG = "TryOnColorPickerView"

class TryOnColorPickerView : LinearLayout {
    /*
     * open api
     * 1 fun setSeekBarValue(strength: Float, light: Float, midTone: Float, color: STColor) : 设置View初始颜色
     *
     */

    companion object {
        const val DEF_HSV_S = 0.7f
        const val DEF_HSV_V = 0.7f

        const val DEF_STRENGTH = 0.7f
    }

    private lateinit var mType: EffectType
    private var mId: Int = 1// 区域ID

    private var mState: State? = null
    private var mStrengthSeekBarViewMap = HashMap<Int, TryOnSeekBarView>()

    // 区域按钮
    private var mPartViewList = HashMap<Int, PartCircleBtn>()
    // view_tryon_color_picker
    private lateinit var mBinding: ViewTryonColorPickerBinding

    private fun refreshColorResult(fromUser: Boolean) {
        var color = mBinding.colorPickerView.color
        color = convertV(color, mBinding.gradientSeekbarV.getValue())
        LogUtils.iTag(TAG, "refreshColorResult: ${mBinding.gradientSeekbarS.getValue()}")
        color = convertS(color, mBinding.gradientSeekbarS.getValue())

        refreshSeekBarBg()

        val stColor = STColor(
            Color.red(color).toFloat(),
            Color.green(color).toFloat(),
            Color.blue(color).toFloat(),
            Color.alpha(color).toFloat()
        )
        LogUtils.iTag(TAG, "stColor:${stColor} ")
        LogUtils.iTag(TAG, "rgba(${stColor.r},${stColor.g}, ${stColor.b}, ${stColor.a})")
        if (fromUser)
            mListener?.onSelectedColor(mType, color, stColor)

        if (fromUser) {
            refreshRGBEdit(stColor)
        }

        // 区域颜色回调

        if (fromUser && mBinding.tvQuyu.visibility == View.VISIBLE) {
            LogUtils.iTag(TAG, "refreshColorResult: 区域id")
            mListener?.onColorSelectedRegion(mType, mId, stColor)
        }
    }

    constructor(context: Context, effectType: EffectType) : super(context) {
        this.mType = effectType
        when (effectType) {
            EffectType.TYPE_TRY_ON_CHUNXIAN -> {
                mState = LipLineState()
            }
            EffectType.TYPE_TRY_ON_LIP -> {
                mState = LipState()
            }
            EffectType.TYPE_TRY_ON_HAIR -> {
                mState = HairState()
            }
            else -> {
                mState = LipLineState()
            }
        }
        init(context)
    }

    private fun init(context: Context) {
        val startTime = System.currentTimeMillis()

        initView(context)

        initListener()
        mBinding.colorPickerView.post {
            refreshSeekBarBg()
        }

        mBinding.gradientSeekbarV.setProgress(DEF_HSV_V)
        mBinding.gradientSeekbarS.setProgress(DEF_HSV_S)
        LogUtils.iTag(TAG, "init total cost time:" + (System.currentTimeMillis() - startTime))

//        mBinding.colorPickerView.setColorListener(object : ColorListener {
//            override fun onColorSelected(color: Int, fromUser: Boolean) {
//                refreshSeekBarBg()
//            }
//        })
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context)
    }

    private fun initListener() {
        mBinding.ibBack.setOnClickListener {
            //visibility = View.GONE
            resetZero()
            mListener?.onClickBack(mType)
        }
        mBinding.colorPickerView.post {
            mBinding.colorPickerView.setColorListener(object : ColorListener {
                override fun onColorSelected(envelope: Int, fromUser: Boolean) {
                    LogUtils.iTag(TAG, "onColorSelected: $envelope")
                    var refreshEditText = false
                    if (fromUser) refreshEditText = true
                    refreshColorResult(fromUser)
                }
            })
        }

        for (view in arrayOf(mBinding.gradientSeekbarS, mBinding.gradientSeekbarV)) {
            view.setListener(object : GradientSeekbar.Listener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Float,
                    fromUser: Boolean
                ) {
                    refreshColorResult(fromUser)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        for (view in arrayOf(mBinding.sbStrength, mBinding.sbHightLight, mBinding.sbMidTone)) {
            view.setListener(object : TextThumbSeekBar.Listener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Float,
                    fromUser: Boolean
                ) {
                    val seekBarType: TryOnSeekBarType = when (view) {
                        mBinding.sbStrength -> {
                            TryOnSeekBarType.TYPE_STRENGTH
                        }
                        mBinding.sbHightLight -> {
                            TryOnSeekBarType.TYPE_HIGHLIGHT
                        }
                        else -> {
                            TryOnSeekBarType.TYPE_MIDTONE
                        }
                    }
                    mListener?.onProgressChanged(
                        EffectType.TYPE_TRY_ON_HAIR,
                        seekBarType, progress, fromUser
                    )
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            })
        }

        for (tab in arrayOf(mBinding.tvStrength, mBinding.tvGz, mBinding.tvMidTone)) {
            tab.setOnClickListener {
                onStrengthTabClick(it)
            }
        }
        mBinding.tvMidTone.post {
            onStrengthTabClick(mBinding.tvMidTone)
        }
    }

    // 获取区域对应的seekbar view
    private fun getPartSeekBarView(regionId: Int): TryOnSeekBarView? {
        if (mStrengthSeekBarViewMap[regionId] == null) {
            mStrengthSeekBarViewMap[regionId] = TryOnSeekBarView(
                context,
                mType, regionId
            ).apply {
                showSeekBar(null)
                setStrengthProgressListenerForRegion(this, regionId)
            }
        }
        return mStrengthSeekBarViewMap[regionId]
    }

    // <key：为区域ID>
    private var mInnerColorPickerViewMap = HashMap<Int, TryOnInnerColorPicker>()

    // 获取区域对应的seekbar view
    private fun getInnerColorPickerView(regionId: Int): TryOnInnerColorPicker? {
        LogUtils.iTag(TAG, "getInnerColorPickerView() called with: regionId = $regionId")
        if (mInnerColorPickerViewMap[regionId] == null) {
            mInnerColorPickerViewMap[regionId] = TryOnInnerColorPicker(
                context,
                mType, regionId
            ).apply {
                setInnerColorPickerViewLIstener(this)
            }
        }
        return mInnerColorPickerViewMap[regionId]
    }

    private fun setInnerColorPickerViewLIstener(view: TryOnInnerColorPicker) {
        view.setListener(object : TryOnInnerColorPicker.Listener {
            override fun onColorSelectedRegion(type: EffectType, regionId: Int, stColor: STColor) {
                mListener?.onColorSelectedRegion(type, regionId, stColor)
            }

            override fun onClickBack(type: EffectType) {
                visibility = View.INVISIBLE
                mListener?.onClickBack(type)
            }
        })
    }

    private fun setStrengthProgressListener(view: TryOnSeekBarView) {
        view.setListener(object : TryOnSeekBarView.Listener {
            override fun onProgressChanged(
                type: EffectType,
                seekType: TryOnSeekBarType,
                progress: Float,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    LogUtils.iTag(
                        TAG,
                        "onProgressChanged() called with: type = $type, seekType = $seekType, progress = $progress, fromUser = $fromUser"
                    )
                    mListener?.onProgressStrengthRegion(mType, view.mIndex, progress)
                }
            }
        })
    }

    private fun setStrengthProgressListenerForRegion(view: TryOnSeekBarView, regionId: Int) {
        view.setListener(object : TryOnSeekBarView.Listener {
            override fun onProgressChanged(
                type: EffectType,
                seekType: TryOnSeekBarType,
                progress: Float,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    LogUtils.iTag(
                        TAG,
                        "onProgressChanged() called with: type = $type, seekType = $seekType, progress = $progress, fromUser = $fromUser"
                    )
                    LogUtils.iTag(TAG, "onProgressChanged: regionId:${view.mIndex} progress: ${progress}")
                    mListener?.onProgressStrengthRegion(mType, regionId, progress)
                }
            }
        })
    }

    private fun onStrengthTabClick(view: View) {
        if (view.isSelected) return

        for (tabView in arrayOf(mBinding.tvStrength, mBinding.tvGz, mBinding.tvMidTone)) {
            tabView.isSelected = false
            tabView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))

        }
        mBinding.sbStrength.visibility = View.INVISIBLE
        mBinding.sbHightLight.visibility = View.INVISIBLE
        mBinding.sbMidTone.visibility = View.INVISIBLE

        when (view) {
            mBinding.tvStrength -> {
                mBinding.sbStrength.visibility = View.VISIBLE
            }
            mBinding.tvGz -> {
                mBinding.sbHightLight.visibility = View.VISIBLE
            }
            mBinding.tvMidTone -> {
                mBinding.sbMidTone.visibility = View.VISIBLE
            }
        }
        view.isSelected = true
        view.setBackgroundResource(R.drawable.shape_tryon_btn)
    }

    private fun getHSV(color: Int): FloatArray {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return hsv
    }

    private fun convertS(color: Int, @FloatRange(from = 0.0, to = 1.0) scale: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[1] = scale
        return Color.HSVToColor(255, hsv)
    }

    private fun convertV(color: Int, @FloatRange(from = 0.0, to = 1.0) scale: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = scale
        return Color.HSVToColor(255, hsv)
    }

    private fun refreshSeekBarBg() {
        mBinding.gradientSeekbarV.setColor(
            convertV(mBinding.colorPickerView.color, 0f),
            convertV(mBinding.colorPickerView.color, 1f)
        )

        mBinding.gradientSeekbarS.setColor(
            convertS(mBinding.colorPickerView.color, 0f),
            convertS(mBinding.colorPickerView.color, 1f)
        )
    }

    lateinit var ibBack: ImageButton

    private fun setOpenVar() {
        ibBack = mBinding.ibBack
    }

    /* start *************************************/
    fun initSelfStateViewForLipLineState() {// 唇线 显示强度条
        mBinding.colorPickerSeekbar.visibility = View.INVISIBLE
        mBinding.flSeekbarContainer.visibility = View.VISIBLE
    }

    fun initSelfStateViewForLipState() {
        mBinding.tvQuyu.visibility = View.INVISIBLE
    }

    fun initSelfStateViewForHairState() {// 强度/光泽度/灰白度
        LogUtils.iTag(TAG, "initSelfStateViewForHairState() called")
        mBinding.tvQuyu.visibility = View.INVISIBLE
        mBinding.flSeekbarContainer.visibility = View.INVISIBLE
    }
    /* end *************************************/


    private fun initView(context: Context) {
        mBinding = ViewTryonColorPickerBinding.inflate(LayoutInflater.from(context), this, true)
        // LayoutInflater.from(context).inflate(R.layout.view_tryon_color_picker, this, true)
        setOpenVar()

        if (mType == EffectType.TYPE_TRY_ON_LIP) {// lip
            mBinding.colorPickerSeekbar.visibility = View.INVISIBLE
        } else {// hair
            mBinding.colorPickerSeekbar.visibility = View.VISIBLE
        }
        for (view in arrayOf(mBinding.sbStrength, mBinding.sbHightLight, mBinding.sbMidTone)) {
            view.setValue(DEF_STRENGTH)
        }
        mState?.initSelfStateView(this)

        mBinding.etR.filters = arrayOf<InputFilter>(RGBColorSectionFilter())
        mBinding.etG.filters = arrayOf<InputFilter>(RGBColorSectionFilter())
        mBinding.etB.filters = arrayOf<InputFilter>(RGBColorSectionFilter())
        mBinding.etR.addTextChangedListener(InputTextWatcher(this, mBinding.etR))
        mBinding.etG.addTextChangedListener(InputTextWatcher(this, mBinding.etG))
        mBinding.etB.addTextChangedListener(InputTextWatcher(this, mBinding.etB))
    }

    private fun checkRGB(fromUser: Boolean) {
        LogUtils.iTag(TAG, "checkRGB() called ${fromUser}")
        val strR = mBinding.etR.text.toString().trim()
        val strG = mBinding.etG.text.toString().trim()
        val strB = mBinding.etB.text.toString().trim()
        if (!TextUtils.isEmpty(strR) && !TextUtils.isEmpty(strG) && !TextUtils.isEmpty(strB)) {
            val intR = strR.toFloat()
            val intG = strG.toFloat()
            val intB = strB.toFloat()
            LogUtils.iTag(TAG, "checkRGB: ($intR, $intG, $intB)")
            if (intR <= 255 && intG <= 255 && intB <= 255) {
                val stColor = STColor(intR, intG, intB, 0f)
                mListener?.onSelectedColor(mType, 0, stColor)
                initColorViewBySTcolor(stColor)
            } else {
                Toast.makeText(context, "数字范围0-255", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class InputTextWatcher(private val tryView: TryOnColorPickerView, private val etView: EditText) : TextWatcher {
        companion object {
            private const val TAG = "InputTextWatcher"
        }

        private var fromUser: Boolean = false

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            fromUser = tryView.mBinding.etR.tag == null
            if (fromUser)
                tryView.checkRGB(fromUser)
            tryView.setEditSelectionPosition(etView)
        }
    }

    private fun getBtnTryOnPartView(regionId: Int): PartCircleBtn? {
        if (mPartViewList[regionId] == null)
            mPartViewList[regionId] = PartCircleBtn(context, regionId).apply {
                initBtnListener(this)
            }
        return mPartViewList[regionId]
    }

    private fun initBtnListener(view: PartCircleBtn) {
        val btn = view.findViewById<MaterialButton>(R.id.mbtn_location)
        btn.setOnClickListener {
            LogUtils.iTag(TAG, "initBtnListener() called index:")
            onPartItemClick(view)

            //mTryOnRegionInfoArray?.get(view.mIndex)?.let { it1 -> initColorViewBySTcolor(it1.color) }
        }
    }

    private fun onPartItemClick(view: PartCircleBtn) {
        if (view.isSelected)
            return
        for (item in mPartViewList.values) {
            item.isSelected = false
        }
        view.isSelected = true
        ReplaceViewHelper.toReplaceView(
            getInnerColorPickerView(view.mRegionId),
            mBinding.flContainerColorPicker
        )
        ReplaceViewHelper.toReplaceView(
            getPartSeekBarView(view.mRegionId),
            mBinding.flSeekbarContainer
        )
        //mListener?.onClickRegionBtn(mType, mId)
    }

    private var mListener: Listener? = null

    interface Listener {
        fun onSelectedColor(type: EffectType, color: Int, stColor: STColor)
        fun onProgressChanged(
            type: EffectType,
            seekType: TryOnSeekBarType,
            progress: Float,
            fromUser: Boolean
        )

        fun onProgressStrengthRegion(type: EffectType, regionId: Int, strength: Float)

        // setTryOnRegionColor(EffectType type, int regionId, STColor stColor);
        // 区域颜色调节
        fun onColorSelectedRegion(type: EffectType, regionId: Int, stColor: STColor)

        // 点击区域回调，用于刷新数据
        //fun onClickRegionBtn(type:EffectType, regionId :Int)
        fun onClickBack(type: EffectType)// 调色盘上的返回btn
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    // 区域信息
    fun showQuYuInfo(count: Int, array: Array<STEffectsTryOnRegionInfo>) {
        array.sortWith(compareBy({ it.regionId }, { it.regionId }))
        if (count == 0) {
            mBinding.tvQuyu.visibility = View.INVISIBLE
            return
        }
        ReplaceViewHelper.removeAllViewsInLayout(mBinding.llPartContainer)
        for (i in 0 until count) {
            mBinding.llPartContainer.addView(getBtnTryOnPartView(array[i].regionId).apply {
                isSelected = false
            })
//            ReplaceViewHelper.toReplaceView(, mBinding.llPartContainer)
            LogUtils.iTag(TAG, "showQuYuInfo: ${array[i]}")
            getPartSeekBarView(array[i].regionId)?.setSeekBarValue(array[i].strength)
            getInnerColorPickerView(array[i].regionId)?.initColorViewBySTcolor(array[i].color)
            getInnerColorPickerView(array[i].regionId)?.visibility = View.VISIBLE
        }

        // 默认选中区域1
        getBtnTryOnPartView(1)?.let { onPartItemClick(it) }

        // 只有1个区域，则隐藏
        if (count == 1) {
            mBinding.tvQuyu.visibility = View.INVISIBLE
            getBtnTryOnPartView(1)?.visibility = View.INVISIBLE
        } else {
            mBinding.tvQuyu.visibility = View.VISIBLE
            getBtnTryOnPartView(1)?.visibility = View.VISIBLE
        }

    }

    fun setSeekBarStrengthValue(strength: Float) {
        mBinding.sbStrength.setValue(strength)
    }

    fun setSeekBarValue(strength: Float, light: Float, midTone: Float, color: STColor) {
        mBinding.sbStrength.setValue(strength)
        mBinding.sbHightLight.setValue(light)
        mBinding.sbMidTone.setValue(midTone)

        initColorViewBySTcolor(color)
        refreshRGBEdit(color)
    }

    private fun refreshRGBEdit(color: STColor) {
        mBinding.etR.tag = "Value changed by program"
        mBinding.etR.setText("${color.r.toInt()}")
        mBinding.etG.setText("${color.g.toInt()}")
        mBinding.etB.setText("${color.b.toInt()}")
        mBinding.etR.tag = null

//        setEditSelectionPosition(mBinding.etB)
    }

    private fun setEditSelectionPosition(editText: EditText) {
        editText.requestFocus()
        editText.isFocusableInTouchMode = true
        editText.setSelection(editText.text.length)
    }

    private fun initColorViewBySTcolor(color: STColor) {
        LogUtils.iTag(TAG, "initColorViewBySTcolor() called with: color = $color")
        val colorI = Color.rgb(
            color.r.toInt(),
            color.g.toInt(),
            color.b.toInt()
        )
        mBinding.colorPickerView.setInitialColor(
            colorI
        )

        val hsv = getHSV(colorI)
        mBinding.gradientSeekbarV.setValue(hsv[2])
        mBinding.gradientSeekbarS.setValue(hsv[1])
    }

    fun setSeekBarValue(strength: Float) {
        mBinding.sbStrength.setValue(strength)
    }

    interface State {
        fun initSelfStateView(view: TryOnColorPickerView)// initView()
    }

    class LipLineState : State {
        override fun initSelfStateView(view: TryOnColorPickerView) {
            view.initSelfStateViewForLipLineState()
        }
    }

    class HairState : State {
        override fun initSelfStateView(view: TryOnColorPickerView) {
            view.initSelfStateViewForHairState()
        }
    }

    class LipState : State {
        override fun initSelfStateView(view: TryOnColorPickerView) {
            view.initSelfStateViewForLipState()
        }
    }

    inner class RGBColorSectionFilter : InputFilter {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence {
            val sourceText = source.toString()
            val destText = dest.toString()
//            if (!TextUtils.isEmpty(destText) && !TextUtils.isEmpty(sourceText) && destText.length>1 && destText.substring(
//                    0,
//                    dstart
//                ).toInt() == 0
//            ) {
//                return ""
//            }
            val totalText = StringBuilder()
            totalText.append(destText.substring(0, dstart))
                .append(sourceText)
                .append(destText.substring(dstart, destText.length))
            try {
                if (totalText.toString().toInt() > 255) {
                    return ""
                }
            } catch (e: Exception) {
                return ""
            }
            return if ("" == source.toString()) {
                ""
            } else "" + source.toString().toInt()
        }
    }

    private fun resetZero() {
        mBinding.etR.tag = "Value changed by program"
        val strR = mBinding.etR.text.toString().trim()
        val strG = mBinding.etG.text.toString().trim()
        val strB = mBinding.etB.text.toString().trim()
        if (TextUtils.isEmpty(strR)) {
            mBinding.etR.setText("0")
        }
        if (TextUtils.isEmpty(strG)) {
            mBinding.etG.setText("0")
        }
        if (TextUtils.isEmpty(strB)) {
            mBinding.etB.setText("0")
        }
        mBinding.etR.tag = null
    }
}