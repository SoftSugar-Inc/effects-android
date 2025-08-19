package softsugar.senseme.com.effects.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.blankj.utilcode.util.LogUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.appcompat.app.AlertDialog
import com.dinuscxj.progressbar.CircleProgressBar

import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewRecordBinding
import softsugar.senseme.com.effects.utils.CountDownTimerCopyFromAPI26
import softsugar.senseme.com.effects.utils.MultiLanguageUtils
import softsugar.senseme.com.effects.utils.RippleUtils
import softsugar.senseme.com.effects.view.widget.TipToast.Companion.makeText
import java.util.*
import kotlin.math.roundToLong

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/21/21 2:22 PM
 */
private const val TAG = "RecordView"

class RecordView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), IRecordView {

    companion object {
        const val BLACK_STYLE = 2
        const val WRITE_STYLE = 3

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(BLACK_STYLE, WRITE_STYLE)
        annotation class Style

        private const val MILLIS_IN_FUTURE = (60 * 1000).toLong()
        private const val COUNT_DOWN_INTERVAL: Long = 10

        const val STATUS_DEFAULT = 0
        const val STATUS_RECORDING = 1
        const val STATUS_DONE = 2
        const val STATUS_DOWNLOAD = 3

        const val MODE_TAKE_PIC = 4

        const val MODE_RECORD = 5

    }
    private lateinit var mBinding: ViewRecordBinding

    enum class StateType {
        TAKE_PIC,       // 拍照
        START_RECORD,   // 开始录制
        STOP_RECORD,    // 停止录制
        CLICK_OK,       // 点"确认"
        CLICK_SAVE,     // 点"保存"
        CLICK_DELETE    // 点"删除"
    }

    init {
        val startTime = System.currentTimeMillis()
        initView(context)
        setListener()
        LogUtils.i( "init total cost time:" + (System.currentTimeMillis() - startTime))

    }

    private fun setListener() {
        mBinding.dtOk.setOnClickListener {
            showStatus(STATUS_DOWNLOAD)
            mListener?.recordStateChangeListener(StateType.CLICK_OK)
        }
        mBinding.ivDownload.setOnClickListener {
            showStatus(STATUS_DEFAULT)
            makeText(
                context,
                MultiLanguageUtils.getStr(R.string.toast_save_video),
                Toast.LENGTH_SHORT
            ).show()
            mListener?.recordStateChangeListener(StateType.CLICK_SAVE)
        }
    }

    fun onResume() {
        LogUtils.iTag(TAG, "onResume() called")
    }

    fun onPause() {
        LogUtils.iTag(TAG, "onPause() called")
        if (mBinding.lineProgress.visibility == View.VISIBLE) {
            hide()
            mBinding.gpStatusDone.visibility = View.VISIBLE
            releaseTimer()
            mListener?.recordStateChangeListener(StateType.STOP_RECORD)
        }
    }

    private val mTimer: CountDownTimerCopyFromAPI26 =
        object : CountDownTimerCopyFromAPI26(
            MILLIS_IN_FUTURE,
            COUNT_DOWN_INTERVAL
        ) {
            override fun onTick(millisUntilFinished: Long) {
                mBinding.lineProgress.progress =
                    (MILLIS_IN_FUTURE - millisUntilFinished.toDouble()).toInt()
                val time =
                    ((MILLIS_IN_FUTURE - millisUntilFinished.toDouble()) / 1000).roundToLong()

                val formatSeconds = formatSeconds(time)
                mBinding.tvTime.text = formatSeconds
            }

            override fun onFinish() {
                hide()
                mBinding.gpStatusDone.visibility = View.VISIBLE
                releaseTimer()
                mListener?.recordStateChangeListener(StateType.STOP_RECORD)
            }
        }

    // import kotlinx.android.synthetic.main.view_bottom_menu.view.*
    //import kotlinx.android.synthetic.main.view_record.view.*
    @SuppressLint("ClickableViewAccessibility")
    private fun initView(context: Context) {
        mBinding = ViewRecordBinding.inflate(LayoutInflater.from(context), this, true)
        LayoutInflater.from(context).inflate(R.layout.view_record, this, true)
        setOpenVar()
        RippleUtils.setForeground(context, mBinding.ivDownload)
        mBinding.lineProgress.max = MILLIS_IN_FUTURE.toInt()

        // 按下开始计时
        mBinding.ibRecord.setOnClickListener {
            hide()
            mBinding.gpStatusRecording.visibility = View.VISIBLE
            mTimer.start()
            if (mStyle == BLACK_STYLE) {
                mBinding.vWrite.visibility = View.VISIBLE
            }
            mTimer.start()
            mListener?.recordStateChangeListener(StateType.START_RECORD)
        }
        mBinding.lineProgress.setOnClickListener {
            hide()
            mBinding.gpStatusDone.visibility = View.VISIBLE
            //v_write.visibility = View.VISIBLE
            releaseTimer()
            mListener?.recordStateChangeListener(StateType.STOP_RECORD)
        }

        mBinding.dtDelete.setOnClickListener {
            showDlg()
        }

        mBinding.dtCancel.setOnClickListener {
            showStatus(STATUS_DEFAULT)
            mListener?.recordStateChangeListener(StateType.CLICK_DELETE)
        }

        mBinding.ibTakePic.setOnClickListener {
            mListener?.recordStateChangeListener(StateType.TAKE_PIC)
        }
    }

    lateinit var mCircleProgressBar: CircleProgressBar
    lateinit var btnDownload: ImageView
    lateinit var ibTakePic:ImageButton

    private fun setOpenVar() {
        mCircleProgressBar = mBinding.lineProgress
        btnDownload = mBinding.ivDownload
        ibTakePic = mBinding.ibTakePic
    }

    private fun hide() {
        mBinding.gpStatusDefault.visibility = View.INVISIBLE
        mBinding.gpStatusRecording.visibility = View.INVISIBLE
        mBinding.gpStatusDone.visibility = View.INVISIBLE
        mBinding.gpStatusDownload.visibility = View.INVISIBLE
    }

    fun formatSeconds(seconds: Long): String {
        val standardTime: String
        when {
            seconds <= 0 -> {
                standardTime = "00:00"
            }
            seconds < 60 -> {
                standardTime = java.lang.String.format(Locale.getDefault(), "00:%02d", seconds % 60)
            }
            seconds < 3600 -> {
                standardTime = java.lang.String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    seconds / 60,
                    seconds % 60
                )
            }
            else -> {
                standardTime = java.lang.String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d",
                    seconds / 3600,
                    seconds % 3600 / 60,
                    seconds % 60
                )
            }
        }
        return standardTime
    }

    private fun releaseTimer() {
        mTimer.cancel()
    }

    private fun showDlg() {
        val localBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        localBuilder.setMessage(MultiLanguageUtils.getStr(R.string.dlg_give_up))
        localBuilder.setPositiveButton(
            MultiLanguageUtils.getStr(R.string.dlg_ok2)
        ) { _, _ ->
            showStatus(STATUS_DEFAULT)
            mBinding.vWrite.visibility = View.INVISIBLE
            mListener?.recordStateChangeListener(StateType.CLICK_DELETE)
        }
        localBuilder.setNegativeButton(
            MultiLanguageUtils.getStr(R.string.dlg_cancel)
        ) { _, _ ->
        }
        localBuilder.setCancelable(false).create()
        localBuilder.show()
    }

    override fun showMode(mode: Int) {
        when (mode) {
            MODE_TAKE_PIC -> {
                hide()
                mBinding.ibTakePic.visibility = View.VISIBLE
            }
            MODE_RECORD -> {
                mBinding.ibTakePic.visibility = View.INVISIBLE
                showStatus(STATUS_DEFAULT)
            }
        }
    }

    override fun showStatus(status: Int) {
        hide()
        when (status) {
            STATUS_DEFAULT -> {
                mBinding.vWrite.visibility = View.INVISIBLE
                mBinding.gpStatusDefault.visibility = View.VISIBLE
            }
            STATUS_RECORDING -> {
                mBinding.gpStatusRecording.visibility = View.VISIBLE
            }
            STATUS_DONE -> {
                mBinding.gpStatusDone.visibility = View.VISIBLE
            }
            STATUS_DOWNLOAD -> {
                mBinding.gpStatusDownload.visibility = View.VISIBLE
            }
        }
    }

    interface Listener {
        fun recordStateChangeListener(status: StateType)
    }

    var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private var mStyle = 0 // WRITE_STYLE

    fun setStyle(style: Int) {
        mStyle = style
        when (style) {
            0 -> { // BLACK_STYLE
                mBinding.ivDownload.setImageResource(R.drawable.ic_camera_download_black)
                mBinding.ibRecord.setImageResource(R.drawable.ic_record_640)
                mBinding.dtDelete.setDrawableTop(R.drawable.ic_camera_delete_black)
                mBinding.dtCancel.setDrawableTop(R.drawable.ic_camera_cancel_black)
                mBinding.dtCancel.setTextColor(Color.parseColor("#000000"))
                mBinding.dtDelete.setTextColor(Color.parseColor("#000000"))

                mBinding.dtOk.setDrawableTop(R.drawable.ic_camera_ok_black)
                mBinding.dtOk.setTextColor(Color.parseColor("#000000"))
                //cl_root.setBackgroundColor(Color.parseColor("#ffffff"))
                mBinding.lineProgress.setProgressBackgroundColor(context.resources.getColor(R.color.color_ffdbdbdb))
            }
            1 -> { // WRITE_STYLE
                mBinding.lineProgress.setProgressBackgroundColor(context.resources.getColor(R.color.color_ffffff))
                mBinding.dtDelete.setDrawableTop(R.drawable.ic_camera_delete)
                mBinding.ibRecord.setImageResource(R.drawable.ic_record)
                mBinding.ivDownload.setImageResource(R.drawable.ic_camera_download)
                mBinding.dtCancel.setDrawableTop(R.drawable.ic_camera_cancel)
                mBinding.dtCancel.setTextColor(Color.parseColor("#ffffff"))
                mBinding.dtDelete.setTextColor(Color.parseColor("#ffffff"))
                //cl_root.setBackgroundColor(Color.parseColor("#00000000"))

                mBinding.dtOk.setDrawableTop(R.drawable.ic_camera_ok)
                mBinding.dtOk.setTextColor(Color.parseColor("#ffffff"))
            }
        }
    }

}