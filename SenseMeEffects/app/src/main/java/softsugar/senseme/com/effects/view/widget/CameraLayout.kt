package softsugar.senseme.com.effects.view.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import com.blankj.utilcode.util.LogUtils
import android.view.LayoutInflater
import android.view.View
import android.webkit.URLUtil
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.hjq.toast.Toaster
import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
//import kotlinx.android.synthetic.main.view_camera_action_bar.view.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.activity.*
import softsugar.senseme.com.effects.databinding.LayoutCameraBinding
import softsugar.senseme.com.effects.event.EventDownload
import softsugar.senseme.com.effects.event.EventReplay
import softsugar.senseme.com.effects.state.*
import softsugar.senseme.com.effects.utils.Constants
import softsugar.senseme.com.effects.utils.ContextHolder
import softsugar.senseme.com.effects.utils.EventBusUtils
import softsugar.senseme.com.effects.utils.PropertiesUtil.getBooleanValue
import softsugar.senseme.com.effects.view.StickerItem
import softsugar.senseme.com.effects.view.StickerState
import java.io.File

open class CameraLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs), LifecycleObserver {

    companion object {
        private const val TAG = "CameraLayoutT"
    }

    private var mActivity: CameraActivity? = null
    private lateinit var mBinding: LayoutCameraBinding

    var stickerView: StickerView
    var basicEffectView: BasicEffectView
    var filterView: FilterView
    var record_view: RecordView
    var cameraBar: CameraActionBarView
    var bottomMenuView: BottomMenuView
    var clSticker: ConstraintLayout
    var llBottomText: SlideTextView

    init {
        val startTime = System.currentTimeMillis()
        if ((context as Activity) is CameraActivity)
            mActivity = context as CameraActivity
        initView(context)
        initListener()
        AtyStateContext.getInstance().initView(this)
        LogUtils.i("CameraLayout init cost time:" + (System.currentTimeMillis() - startTime))

        stickerView = mBinding.stickerView
        filterView = mBinding.filterView
        basicEffectView = mBinding.basicEffectView
        record_view = mBinding.recordView
        cameraBar = mBinding.cameraBar
        bottomMenuView = mBinding.bottomMenuView
        clSticker = mBinding.clSticker
        llBottomText = mBinding.llBottomText
    }

    // TryOn-图片模式下视图
    fun initViewForImgTryOn() {
        LogUtils.iTag(TAG, "initViewForImgTryOn() called")
        mBinding.cameraBar.setShowType(CameraActionBarView.Type.PREVIEW)
        mBinding.recordView.visibility = GONE
        mBinding.bottomMenuView.post { mBinding.bottomMenuView.visibility = GONE }
        mBinding.llBottomText.post { mBinding.llBottomText.visibility = GONE }

        mBinding.cameraBar.setShowType(CameraActionBarView.Type.IMG)
    }

    private fun initListener() {
        // 重新确定
        if (AtyStateContext.getInstance().state is CameraAtyState) {
            val state = AtyStateContext.getInstance().state as CameraAtyState
        } else {
        }

        if (ContextHolder.isCamera2Mode()) {
            mBinding.verticalSeekbar.visibility = View.INVISIBLE
        }
    }

    private fun initView(context: Context) {
        val startTime = System.currentTimeMillis()
        mBinding = LayoutCameraBinding.inflate(LayoutInflater.from(context), this, true)
        // LayoutInflater.from(context).inflate(R.layout.layout_camera, this, true)
        LogUtils.i("CameraLayout initView cost time: ${System.currentTimeMillis() - startTime}")
    }

    fun closeTableView() {
        mBinding.basicEffectView.visibility = INVISIBLE
        mBinding.clSticker.visibility = INVISIBLE
        mBinding.cameraBar.visibilityTopSettingPart(INVISIBLE)
        //         resolutionView.visibility = INVISIBLE
        mBinding.cameraBar.resolutionView?.visibility = INVISIBLE
        mBinding.filterView.visibility = INVISIBLE
        // 图片模式 视频模式 隐藏风格
        if (AtyStateContext.getInstance().state is ImgAtyState || AtyStateContext.getInstance().state is VideoAtyState) {
        }
    }

    fun showStatusByMode() {
        val state = AtyStateContext.getInstance().state
        if (AtyStateContext.getInstance().state is TryOnImgAtyState) return
        //LogUtils.iTag(TAG, "showStatusByMode: $mode , ${AtyStateContext.getInstance().state} ")
        //if (mode == Constants.ATY_TYPE_IMAGE || mode == Constants.ATY_TYPE_VIDEO) {
        if (state is ImgAtyState || state is VideoAtyState) {
//            mBinding.viewTryOn.visibility = INVISIBLE
            mBinding.cameraBar.setShowType(CameraActionBarView.Type.IMG)
            mBinding.llBottomText.hideText()
            mBinding.recordView.visibility = INVISIBLE
            // mBinding.bottomMenuView.mIvStyle.visibility = VISIBLE
        //} else if (mode == Constants.ATY_TYPE_CAMERA) {
        } else if (state is CameraAtyState) {
//            mBinding.viewTryOn.visibility = INVISIBLE
            mBinding.cameraBar.setShowType(CameraActionBarView.Type.PREVIEW)
            mBinding.recordView.visibility = VISIBLE
            mBinding.bottomMenuView.visibility = VISIBLE
            mBinding.llBottomText.showText()
        //} else if (mode == Constants.ATY_TYPE_TRY_ON) {
        } else if (state is TryOnCameraAtyState) {
//            mBinding.viewTryOn.visibility = VISIBLE
            mBinding.cameraBar.setShowType(CameraActionBarView.Type.PREVIEW)
            mBinding.recordView.visibility = GONE
            mBinding.bottomMenuView.post { mBinding.bottomMenuView.visibility = GONE }
            mBinding.llBottomText.post { mBinding.llBottomText.visibility = GONE }
        }
    }

    // 录制视频时候UI状态控制
    fun recordViewStatus(status: RecordView.StateType) {
        when {
            status === RecordView.StateType.TAKE_PIC -> {
                mBinding.gpBottomMenu.visibility = VISIBLE
            }
            status === RecordView.StateType.CLICK_DELETE -> {
                mBinding.bottomMenuView.visibility = VISIBLE
                mBinding.llBottomText.showText()
            }
            status === RecordView.StateType.CLICK_OK -> {
                mBinding.llBottomText.hideText()
            }
            status === RecordView.StateType.START_RECORD -> {
                mBinding.cameraBar.hiddenBtn()
                mBinding.llBottomText.hideText()
                mBinding.gpBottomMenu.visibility = INVISIBLE
            }
            status === RecordView.StateType.CLICK_SAVE -> {
                mBinding.cameraBar.showBtn()
                mBinding.llBottomText.showText()
                mBinding.gpBottomMenu.visibility = VISIBLE
            }
        }
    }

    // 点击特效、美妆、滤镜、美颜隐藏菜单
    fun hideMenuView() {
        mBinding.ibShowOriginalBottom.visibility = INVISIBLE
        mBinding.llBottomText.hideText()
        mBinding.recordView.visibility = INVISIBLE
        mBinding.bottomMenuView.visibility = INVISIBLE
    }

    // 主要在预览模式下，点击屏幕，控制底部菜单交互
    fun showMenuView() {
        val state = AtyStateContext.getInstance().state
        // TryOn 模式触摸屏幕无需处理
        if (state is TryOnCameraAtyState || state is TryOnCameraAtyState) return
        if (mBinding.recordView.mCircleProgressBar.visibility == VISIBLE ||
            mBinding.recordView.btnDownload.visibility == VISIBLE
        ) return
        //if (mode == Constants.ATY_TYPE_IMAGE || mode == Constants.ATY_TYPE_VIDEO) {
        if (state is ImgAtyState || state is VideoAtyState) {
            mBinding.llBottomText.hideText()
            mBinding.recordView.visibility = INVISIBLE
        } else {
            mBinding.recordView.visibility = VISIBLE
            mBinding.llBottomText.showText()
        }
        mBinding.gpBottomMenu.visibility = VISIBLE
    }

    // 点击底部菜单（视频、拍摄、风格）
    fun onClickMenuListener(position: Int) {
        LogUtils.iTag(
            TAG,
            "onClickMenuListener() called with: position = [$position]"
        )
        when (position) {
            0 -> {
                mBinding.recordView.visibility = VISIBLE
                mBinding.recordView.showMode(RecordView.MODE_RECORD)
            }
            1 -> {
                mBinding.bottomMenuView.visibility = VISIBLE
                mBinding.recordView.visibility = VISIBLE
                mBinding.recordView.showMode(RecordView.MODE_TAKE_PIC)
            }
        }
    }



    interface Listener {
        fun onClickReplay()
        fun onClickIsMe()      // 确定目标人脸
        fun onClickResetIsMe() // IsMe中点重置
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventReplay) {
        LogUtils.iTag(
            TAG, "onMessageEvent() called with: event = [${event.selected}]"
        )
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventDownload) {
        LogUtils.iTag(
            TAG, "onMessageEvent() called with: event = [${event.url}]"
        )
        download(event.url, event.icon)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        LogUtils.iTag(TAG, "onCreate() called")
        EventBusUtils.registerEventBus(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        LogUtils.iTag(TAG, "onDestroy() called")
        EventBusUtils.unregisterEventBus(this)
    }

    private fun download(url: String, iconUrl: String) {
        val task = DownloadTask.Builder(
            url,
            context.getExternalFilesDir(null)?.absolutePath + File.separator + Constants.STICKER_SYNC,
            URLUtil.guessFileName(url, null, null)
        ).setConnectionCount(1)
            .setMinIntervalMillisCallbackProcess(100)
            .setPassIfAlreadyCompleted(false)
            .build()
        task.enqueue(object : DownloadListener {
            override fun taskStart(task: DownloadTask) {
                Toaster.show("下载中")
            }

            override fun connectTrialStart(
                task: DownloadTask,
                requestHeaderFields: MutableMap<String, MutableList<String>>
            ) {
            }

            override fun connectTrialEnd(
                task: DownloadTask,
                responseCode: Int,
                responseHeaderFields: MutableMap<String, MutableList<String>>
            ) {
            }

            override fun downloadFromBeginning(
                task: DownloadTask,
                info: BreakpointInfo,
                cause: ResumeFailedCause
            ) {
            }

            override fun downloadFromBreakpoint(task: DownloadTask, info: BreakpointInfo) {
            }

            override fun connectStart(
                task: DownloadTask,
                blockIndex: Int,
                requestHeaderFields: MutableMap<String, MutableList<String>>
            ) {
            }

            override fun connectEnd(
                task: DownloadTask,
                blockIndex: Int,
                responseCode: Int,
                responseHeaderFields: MutableMap<String, MutableList<String>>
            ) {
            }

            override fun fetchStart(task: DownloadTask, blockIndex: Int, contentLength: Long) {
            }

            override fun fetchProgress(task: DownloadTask, blockIndex: Int, increaseBytes: Long) {
                LogUtils.iTag(
                    TAG,
                    "fetchProgress() called with: task = $task, blockIndex = $blockIndex, increaseBytes = $increaseBytes"
                )
            }

            override fun fetchEnd(task: DownloadTask, blockIndex: Int, contentLength: Long) {
            }

            override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?) {
                if (cause == EndCause.ERROR) {
                    LogUtils.iTag(TAG, "taskEnd: ${realCause?.message.toString()}")
                    LogUtils.iTag(
                        TAG,
                        "taskEnd() called with: task = $task, cause = $cause, realCause = $realCause"
                    )
                    Toaster.show("网络异常，请稍后再试")
                } else {
                    LogUtils.iTag(TAG, "下载完成后存储的目录: ${task.file?.absolutePath}")
                    val item = StickerItem()
                    item.state = StickerState.DONE_STATE
                    item.path = task.file?.absolutePath
                    item.iconUrl = iconUrl
                    item.name = URLUtil.guessFileName(task.url, null, null)
                    mBinding.stickerView.setDataSync(item)
                    Toaster.show("下载成功")

                    mBinding.stickerView.setHighLight(EffectType.TYPE_STICKER_SYNC)
                    hideMenuView()
                    mBinding.clSticker.visibility = VISIBLE
                }
            }
        })
    }

}