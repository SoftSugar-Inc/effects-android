package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.Color
import android.text.*
import com.blankj.utilcode.util.LogUtils
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.FloatRange
import com.softsugar.stmobile.model.STColor
import com.skydoves.colorpickerview.listeners.ColorListener
//import kotlinx.android.synthetic.main.view_tryon_inner_color_picker.view.mBinding.etB
//import kotlinx.android.synthetic.main.view_tryon_inner_color_picker.view.et_g
//import kotlinx.android.synthetic.main.view_tryon_inner_color_picker.view.mBinding.etR
//import kotlinx.android.synthetic.main.view_tryon_inner_color_picker.view.mBinding.gradientSeekbarS
//import kotlinx.android.synthetic.main.view_tryon_inner_color_picker.view.mBinding.gradientSeekbarV
//import kotlinx.android.synthetic.main.view_tryon_inner_color_picker.view.ib_back
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewTryonInnerColorPickerBinding
import java.lang.Exception
import java.lang.StringBuilder

class TryOnInnerColorPicker : LinearLayout {

    companion object {
        const val DEF_HSV_S = 0.7f
        const val DEF_HSV_V = 0.7f

        const val DEF_STRENGTH = 0.7f
        private const val TAG = "TryOnInnerColorPicker"
    }

    private var mRegionId = 1
    private var mType: EffectType? = null
    // view_tryon_inner_color_picker
    private lateinit var mBinding: ViewTryonInnerColorPickerBinding

    constructor(context: Context, type: EffectType, regionId: Int) : super(context) {
        LogUtils.iTag(TAG, "null() called with: context = $context, index = $regionId")
        mType = type
        this.mRegionId = regionId
        init(context)
    }

    private fun init(context: Context) {
        mBinding = ViewTryonInnerColorPickerBinding.inflate(LayoutInflater.from(context), this, true)
        LayoutInflater.from(context).inflate(R.layout.view_tryon_inner_color_picker, this, true)
        refreshSeekBarBg()
        initListener()

        mBinding.etR.filters = arrayOf<InputFilter>(RGBColorSectionFilter())
        mBinding.etG.filters = arrayOf<InputFilter>(RGBColorSectionFilter())
        mBinding.etB.filters = arrayOf<InputFilter>(RGBColorSectionFilter())
        mBinding.etR.addTextChangedListener(InputTextWatcher(this, mBinding.etR))
        mBinding.etG.addTextChangedListener(InputTextWatcher(this, mBinding.etG))
        mBinding.etB.addTextChangedListener(InputTextWatcher(this, mBinding.etB))
    }

    private fun setEditSelectionPosition(editText: EditText) {
        editText.requestFocus()
        editText.isFocusableInTouchMode = true
        editText.setSelection(editText.text.length)
    }

    class InputTextWatcher(private val tryView: TryOnInnerColorPicker, private val etView: EditText) : TextWatcher {
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

    private fun checkRGB(fromUser: Boolean) {
        LogUtils.iTag(TAG, "checkRGB() called")
        val strR = mBinding.etR.text.toString().trim()
        val strG = mBinding.etG.text.toString().trim()
        val strB = mBinding.etB.text.toString().trim()
        if (!TextUtils.isEmpty(strR) && !TextUtils.isEmpty(strG) && !TextUtils.isEmpty(strB)) {
            val intR = strR.toFloat()
            val intG = strG.toFloat()
            val intB = strB.toFloat()
            LogUtils.iTag(TAG, "checkRGB: ($intR, $intG, $intB)")
            if (intR <= 255 && intG <= 255 && intB <= 255) {
                val stColor = STColor(intR, intG, intB, 255f)
                mType?.let {
                    LogUtils.iTag(TAG, "checkRGB: $mRegionId $stColor")
                    mListener?.onColorSelectedRegion(it, mRegionId, stColor)
                }
                initColorViewBySTcolor(stColor)
            } else {
                Toast.makeText(context, "数字范围0-255", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshRGBEdit(color: STColor) {
        mBinding.etR.tag = "Value changed by program"
        mBinding.etR.setText("${color.r.toInt()}")
        mBinding.etG.setText("${color.g.toInt()}")
        mBinding.etB.setText("${color.b.toInt()}")
        mBinding.etR.tag = null
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

    private fun initListener() {
        mBinding.ibBack.setOnClickListener {
            //visibility = View.GONE
            resetZero()
            LogUtils.iTag(TAG, "initListener: ${mType?.code}")
            mType?.let { it1 -> mListener?.onClickBack(it1) }
        }
        mBinding.colorPickerView.post {
            mBinding.colorPickerView.setColorListener(object : ColorListener {
                override fun onColorSelected(envelope: Int, fromUser: Boolean) {
                    LogUtils.iTag(TAG, "onColorSelected: $envelope")
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

    }

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
//        if (fromUser)
//            mListener?.onSelectedColor(mType, color, stColor)

        // 区域颜色回调
        if (fromUser) {
            LogUtils.iTag(TAG, "refreshColorResult: mRegionId:${mRegionId}")
            mType?.let { mListener?.onColorSelectedRegion(it, mRegionId, stColor) }
        }

        if (fromUser) {
            refreshRGBEdit(stColor)
        }
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

    fun initColorViewBySTcolor(color: STColor) {
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

        refreshRGBEdit(color)

    }

    interface Listener {
        fun onColorSelectedRegion(type: EffectType, regionId: Int, stColor: STColor)
        fun onClickBack(type: EffectType)// 调色盘上的返回btn
    }

    private var mListener: Listener? = null
    fun setListener(listener: Listener) {
        mListener = listener
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
            try {
                if (!TextUtils.isEmpty(destText) && !TextUtils.isEmpty(sourceText) && destText.substring(
                        0,
                        dstart
                    ).toInt() == 0
                ) {
                    return ""
                }
            }catch (e:Exception) {
                LogUtils.i("error---number --$destText")
                return ""
            }
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