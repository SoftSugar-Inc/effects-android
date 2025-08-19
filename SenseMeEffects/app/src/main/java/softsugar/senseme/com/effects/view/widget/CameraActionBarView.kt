package softsugar.senseme.com.effects.view.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.util.AttributeSet
import com.blankj.utilcode.util.LogUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.LifecycleObserver
import com.blankj.utilcode.util.ClickUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig

import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.activity.CameraActivity

import softsugar.senseme.com.effects.activity.TermsActivity
import softsugar.senseme.com.effects.databinding.ViewCameraActionBarBinding
import softsugar.senseme.com.effects.display.CameraDisplayImpl
import softsugar.senseme.com.effects.state.AtyStateContext
import softsugar.senseme.com.effects.utils.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/10/21 7:53 PM
 */
open class CameraActionBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), LifecycleObserver {
    companion object {
        private const val TAG = "CameraActionBarView"
    }

    private var mActivity: CameraActivity? = null
    private lateinit var mBinding: ViewCameraActionBarBinding
    public var resolutionView: Group?=null
    var tvDebugInfo: TextView

    init {
        val startTime = System.currentTimeMillis()

        if ((context as Activity) is CameraActivity)
            mActivity = context as CameraActivity
        initView(context)
        initListener()

        visibilityTopSettingPart(View.INVISIBLE)
        AtyStateContext.getInstance().initView(this)
        LogUtils.i("init total cost time:" + (System.currentTimeMillis() - startTime))

        tvDebugInfo = mBinding.tvDebugInfo
    }

    fun initViewForTryOn() {
        mBinding.ivImgTryonOptionsSwitch.visibility = View.VISIBLE
        mBinding.ivVideoTryonOptionsSwitch.visibility = View.VISIBLE
    }

    private fun initListener() {
        mBinding.ivImgTryonOptionsSwitch.setOnClickListener {
            PictureSelector.create(mActivity)
                .openGallery(SelectMimeType.ofImage())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setImageEngine(GlideEngine.createGlideEngine())
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }
        mBinding.ivVideoTryonOptionsSwitch.setOnClickListener {
            PictureSelector.create(mActivity)
                .openGallery(SelectMimeType.ofVideo())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setCameraVideoFormat(PictureMimeType.MP4)
                .setFilterVideoMaxSecond(60)
                .setFilterVideoMinSecond(3)
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }
        // 切换camera2
        mBinding.llCamera.setOnClickListener {
            mBinding.llCamera.isSelected = !mBinding.llCamera.isSelected
            ContextHolder.setCamera2Mode(mBinding.llCamera.isSelected)
            MultiLanguageUtils.restart(mActivity)
        }

        mBinding.ivBack.setOnClickListener {
            (context as Activity).finish()
        }
        mBinding.ivScale.setOnClickListener {
            if (mBinding.resolutionView.visibility == VISIBLE) {
                mBinding.resolutionView.visibility = INVISIBLE
            } else {
                visibilityTopSettingPart(View.INVISIBLE)
                mBinding.resolutionView.visibility = VISIBLE
            }
        }

        mBinding.ivSettingOptionsSwitch.setOnClickListener(object : ClickUtils.OnMultiClickListener(3) {
            override fun onTriggerClick(v: View?) {
            }

            override fun onBeforeTriggerClick(v: View?, count: Int) {
                if (mBinding.llPerform.visibility == VISIBLE) {
                    visibilityTopSettingPart(View.INVISIBLE)
                } else {
                    mBinding.resolutionView.visibility = INVISIBLE
                    mBinding.resolutionView.postDelayed({
                        visibilityTopSettingPart(VISIBLE)
                    }, 100)
                }
            }
        })
    }

    enum class Type {
        IMG, PREVIEW
    }

    fun setShowType(type: Type) {
        when (type) {
            Type.IMG -> {
                mBinding.gpPreview.visibility = INVISIBLE
                mBinding.ivSaveImg.visibility = VISIBLE
            }
            Type.PREVIEW -> {
                mBinding.ivSaveImg.visibility = INVISIBLE
                mBinding.gpPreview.visibility = VISIBLE
            }
        }
    }

    fun hiddenBtn() {
        mBinding.ivBack.visibility = View.INVISIBLE
        mBinding.ivSettingOptionsSwitch.visibility = View.INVISIBLE
        mBinding.ivScale.visibility = View.INVISIBLE
        mBinding.tvChangeCamera.visibility = View.INVISIBLE
    }

    fun showBtn() {
        mBinding.ivBack.visibility = View.VISIBLE
        mBinding.ivSettingOptionsSwitch.visibility = View.VISIBLE
        mBinding.ivScale.visibility = View.VISIBLE
        mBinding.tvChangeCamera.visibility = View.VISIBLE
    }

    lateinit var mIvSaveImg: ImageView
    lateinit var mStartBtn: ImageView
    lateinit var mStopBtn: ImageView

    private fun setOpenVar() {
        mIvSaveImg = mBinding.ivSaveImg
        mStartBtn = mBinding.ivCameraPreview
        mStopBtn = mBinding.ivCameraPause
        resolutionView = mBinding.resolutionView
    }

    private fun resetResolutionView() {
        val array = arrayOf(mBinding.dt640, mBinding.dt1280, mBinding.dt1920)
        for (view in array) {
            view.isSelected = false
        }
    }

    private fun initView(context: Context) {
        mBinding = ViewCameraActionBarBinding.inflate(LayoutInflater.from(context), this, true)
        // LayoutInflater.from(context).inflate(R.layout.view_camera_action_bar, this, true)
        setOpenVar()
        RippleUtils.setForeground(
            context, mBinding.ivSaveImg, mBinding.ivBack, mBinding.ivSettingOptionsSwitch,
            mBinding.ivScale, mBinding.dt640, mBinding.dt1280, mBinding.dt1920, mBinding.ivCameraPreview, mBinding.ivCameraPause
        )
        mBinding.dt1280.isSelected = true
        mBinding.dt640.setOnClickListener {
            if (mBinding.dt640.isSelected) return@setOnClickListener
            resetResolutionView()
            mBinding.dt640.isSelected = !mBinding.dt640.isSelected
            mBinding.resolutionView.postDelayed({
                mBinding.resolutionView.visibility = GONE
            }, 0)

            mBinding.ivScale.setImageResource(R.drawable.size640_icon)
            mListener?.onItemPreviewSizeSelected(1)
        }

        mBinding.dt1280.setOnClickListener {
            if (mBinding.dt1280.isSelected) return@setOnClickListener
            resetResolutionView()
            mBinding.dt1280.isSelected = !mBinding.dt1280.isSelected
            mBinding.resolutionView.visibility = GONE
            mBinding.ivScale.setImageResource(R.drawable.ic_camera_size)
            mBinding.resolutionView.postDelayed({
            }, 0)
            mListener?.onItemPreviewSizeSelected(0)
        }

        mBinding.dt1920.setOnClickListener {
            if (mBinding.dt1920.isSelected) return@setOnClickListener
            resetResolutionView()
            mBinding.dt1920.isSelected = !mBinding.dt1920.isSelected
            mBinding.resolutionView.postDelayed({
                mBinding.resolutionView.visibility = GONE
            }, 0)
            mBinding.ivScale.setImageResource(R.drawable.size1920_icon)
            mListener?.onItemPreviewSizeSelected(2)
        }

        mBinding.llPerform.setOnClickListener {
            mBinding.llPerform.isSelected = !mBinding.llPerform.isSelected
            mBinding.llPerform.postDelayed({
                visibilityTopSettingPart(View.GONE)
            }, 500)
            if (mBinding.llPerform.isSelected) {
                mBinding.tvDebugInfo.visibility = VISIBLE
            } else {
                mBinding.tvDebugInfo.visibility = INVISIBLE
            }
        }

        mBinding.llLanguage.setOnClickListener {
//            ll_language.isSelected = !ll_language.isSelected
//
//            if (ll_language.isSelected)
//                MultiLanguageUtils.setEnglish()
//            else
//                MultiLanguageUtils.setChinese()
//            MultiLanguageUtils.restart(mActivity)

            BottomSheetDialog(context).run {
                // 点击外部可以消失
                setCancelable(true)
                // 去除遮罩
                //this.window?.setDimAmount(0f)
                val view = layoutInflater.inflate(R.layout.view_language, null)
                view.findViewById<TextView>(R.id.tv_chines).setOnClickListener {
                    MultiLanguageUtils.setChinese()
                    MultiLanguageUtils.restart(mActivity)
                }
                view.findViewById<TextView>(R.id.tv_en).setOnClickListener {
                    MultiLanguageUtils.setEnglish()
                    MultiLanguageUtils.restart(mActivity)
                }
                view.findViewById<TextView>(R.id.tv_korean).setOnClickListener {
                    MultiLanguageUtils.setKorean()
                    MultiLanguageUtils.restart(mActivity)
                }
                view.findViewById<TextView>(R.id.tv_japan).setOnClickListener {
                    MultiLanguageUtils.setJapan()
                    MultiLanguageUtils.restart(mActivity)
                }
                //view.setBackgroundColor(resources.getColor(android.R.color.holo_red_dark))
                // 点击取消
                view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                    dismiss()
                }
                setContentView(view)
                show()
            }
        }

        mBinding.llTk.setOnClickListener {
            visibilityTopSettingPart(GONE)
            (context as Activity).startActivity(Intent(context, TermsActivity::class.java))
        }

        val language = MultiLanguageUtils.getSPSelectedLocale().language
        mBinding.llLanguage.isSelected = language.contains("en")

        if (ContextHolder.isCamera2Mode()) {
            mBinding.llCamera.isSelected = true
            mBinding.llCamera.text = "Camera2"
        } else {
            mBinding.llCamera.text = "Camera1"
        }
    }

//    fun showTip() {
//        ToastUtils.showShort("稍后")
//    }

    private var mNeedStopCpuRate = false

    private var mCpuInofThread: Thread? = null

    fun release() {
        mNeedStopCpuRate = true
    }

    fun startShowCpuInfo(mCameraDisplay: CameraDisplayImpl?) {
        mNeedStopCpuRate = false
        mCpuInofThread = object : Thread() {
            override fun run() {
                super.run()
                while (!mNeedStopCpuRate) {
                    val cpuRate: String = if (Build.VERSION.SDK_INT <= 25) {
                        getProcessCpuRate().toString()
                    } else {
                        "null"
                    }
                    ThreadUtils.getInstance().runOnUIThread {
                        if (mCameraDisplay != null) {
                            mBinding.tvDebugInfo.text = context.getString(
                                R.string.debug_info,
                                mCameraDisplay.frameCost,
                                mCameraDisplay.fpsInfo,
                                cpuRate
                            )
                        }
                    }
                    try {
                        sleep(500)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        mCpuInofThread?.start()
    }

    private fun getTotalCpuTime(): Long {
        // 获取系统总CPU使用时间
        var cpuInfos: Array<String>? = null
        try {
            val reader = BufferedReader(
                InputStreamReader(
                    FileInputStream("/proc/stat")
                ), 1000
            )
            val load = reader.readLine()
            reader.close()
            cpuInfos = load.split(" ".toRegex()).toTypedArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cpuInfos!![2].toLong() + cpuInfos[3].toLong() + cpuInfos[4].toLong() + cpuInfos[6].toLong() + cpuInfos[5].toLong() + cpuInfos[7].toLong() + cpuInfos[8].toLong()
    }

    private fun getAppCpuTime(): Long {
        //获取应用占用的CPU时间
        var cpuInfos: Array<String>? = null
        val pid = Process.myPid()
        try {
            val reader = BufferedReader(
                InputStreamReader(
                    FileInputStream("/proc/$pid/stat")
                ), 1000
            )
            val load = reader.readLine()
            reader.close()
            cpuInfos = load.split(" ".toRegex()).toTypedArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cpuInfos!![13].toLong() + cpuInfos[14].toLong() + cpuInfos[15].toLong() + cpuInfos[16].toLong()
    }

    // settingView
    fun visibilityTopSettingPart(visibility: Int) {
        for (view in arrayOf(
            mBinding.ivTipBg,
            mBinding.ivTriangleUp,
            mBinding.llLanguage,
            mBinding.llPerform,
            mBinding.llTk,
            mBinding.llReplay,
            mBinding.llCamera
        )) {
            view.visibility = visibility
        }
        if (Build.VERSION.SDK_INT < Constants.ANDROID_MIN_SDK_VERSION) {
            mBinding.llCamera.visibility = View.GONE
        }
    }

    private var mCurrentCpuRate = 0.0f
    private fun getProcessCpuRate(): Float {
        val totalCpuTime1 = getTotalCpuTime()
        val processCpuTime1 = getAppCpuTime()
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val totalCupTime2 = getTotalCpuTime()
        val processCpuTime2 = getAppCpuTime()
        if (totalCpuTime1 != totalCupTime2) {
            val rate =
                (100 * (processCpuTime2 - processCpuTime1) / (totalCupTime2 - totalCpuTime1)).toFloat()
            if (rate >= 0.0f || rate <= 100.0f) {
                mCurrentCpuRate = rate
            }
        }
        return mCurrentCpuRate
    }

    interface Listener {
        fun onItemPreviewSizeSelected(index: Int)
    }

    var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

}