package softsugar.senseme.com.effects.view.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewChooseBgBinding
import softsugar.senseme.com.effects.state.AtyStateContext
import softsugar.senseme.com.effects.state.TryOnCameraAtyState
import softsugar.senseme.com.effects.utils.ContextHolder
import softsugar.senseme.com.effects.utils.GlideEngine
import softsugar.senseme.com.effects.utils.GlideUtils
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.config.SelectMimeType

class ChooseBgView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    companion object {
        private const val TAG = "ChooseBgView"
        const val FIRST_LOAD = 0
    }
    //import kotlinx.android.synthetic.main.view_choose_bg.view.*
    private lateinit var mBinding: ViewChooseBgBinding

    init {
        val startTime = System.currentTimeMillis()
        initView(context)
        initData()
        initListener()
        LogUtils.iTag(TAG, "init total cost time:" + (System.currentTimeMillis() - startTime))
    }

    private var mPageNum = 2
    private var lastSelectedPosition = 0

    private fun setSelected(selected: Boolean, position: Int) {
        this.setSelected(selected, position, true)
    }

    private fun setSelected(selected: Boolean, position: Int, needCallback: Boolean) {
        LogUtils.d(
            TAG,
            "setSelected() called with: selected = $selected, position = $position, needCallback = $needCallback"
        )
        val student = mAdapter.data[position]
        // 先设置选中
        student.isChecked = selected
        mAdapter.notifyItemChanged(position)
        if (selected)
        lastSelectedPosition = position

        if (needCallback && selected) {
            GlobalScope.launch(Dispatchers.IO) {
                var bitmap = GlideUtils.compressBitmap3(student.realPath)
                GlobalScope.launch(Dispatchers.Main) {
                    mListener?.onItemClick(selected, student, position, bitmap)
                }
            }
        }
    }

    private var mIsVideo = false

    fun show(isVideo: Boolean) {
        lastSelectedPosition = 0
        LogUtils.d(TAG, "show() called with: isVideo = $isVideo")
        mIsVideo = isVideo
        mPageNum = 1
        if (isVideo) {// 视频
            mAdapter.data.clear()
            addDefaultData()
            visibility = View.VISIBLE
            queryData(FIRST_LOAD, mPageNum, isVideo)
        } else {
            LogUtils.i("lastSelectedPosition:" + lastSelectedPosition)
            mAdapter.data.clear()
            addDefaultData()
            queryData(FIRST_LOAD, mPageNum, isVideo)

            ContextHolder.setCurrentBg("")
            visibility = View.VISIBLE
            setSelected(false, lastSelectedPosition, false)
            setSelected(true, 0)
        }
    }

    fun hide() {
        LogUtils.d(TAG, "hide() called")
        setSelected(false, lastSelectedPosition, false)
        setSelected(true, 0, false)
        mBinding.mRecyclerView.adapter = mAdapter
        visibility = View.INVISIBLE
    }

    private fun initListener() {
        mAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, _: View?, position: Int ->
            val content = adapter.data[position] as LocalMedia
            if (position == 0) {
                if (!content.isChecked) {// 防止重复点击
                    setSelected(false, lastSelectedPosition)
                    setSelected(true, position)
                }
            } else {
                if (content.isChecked) {// 2.1 处于选中状态 To 反选
                    setSelected(false, position)
                    setSelected(true, 0)
                } else {// 2.2 处于未选中状态- To 选中
                    LogUtils.i("lastSelectedPosition-:" + lastSelectedPosition)
                    setSelected(false, lastSelectedPosition)
                    setSelected(true, position)
                }
            }
        }

        mBinding.llAdd.setOnClickListener {
            if (mIsVideo) {
                PictureSelector.create(context as Activity)
                    .openGallery(SelectMimeType.ofVideo())
                    .setSelectionMode(SelectModeConfig.SINGLE)
                    .isDisplayCamera(false)
                    //.isCamera(false)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .forResult(PictureConfig.CHOOSE_REQUEST)
            } else {
                PictureSelector.create(context as Activity)
                    .openGallery(SelectMimeType.ofImage())
                    .setSelectionMode(SelectModeConfig.SINGLE)
                    //.isDisplayCamera(false)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .forResult(PictureConfig.CHOOSE_REQUEST)
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun queryData(loadType: Int, pageNum: Int, isVideo: Boolean) {
        LogUtils.iTag(TAG, "queryData() called with: loadType = $loadType, pageNum = $pageNum")
        if (isVideo) {
            PictureSelector.create(ActivityUtils.getTopActivity())
                .openGallery(SelectMimeType.ofVideo())
        } else {
            PictureSelector.create(ActivityUtils.getTopActivity())
                .openGallery(SelectMimeType.ofImage())
        }
        // TODO 图片选择器升级
        //folderWindow = FolderPopWindow(context)
        //val folder = folderWindow?.getFolder(0)
        //val bucketId = folder?.bucketId ?: -1
        // TODO 图片选择器升级
//        LocalMediaPageLoader.getInstance(context).loadPageMediaData(
//            bucketId, pageNum, Integer.MAX_VALUE
//        ) { data, _, _ ->
//            LogUtils.iTag(TAG, "onComplete: ${data.size}")
//            mAdapter.loadMoreData(data)
//        }
    }

    //         // TODO 图片选择器升级
    //private var folderWindow: FolderPopWindow? = null

    private lateinit var mAdapter: ChooseBgAdapter

    private fun initData() {
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL

        mBinding.mRecyclerView.layoutManager = manager

        mAdapter = ChooseBgAdapter()

        mBinding.mRecyclerView.adapter = mAdapter
    }

    private fun addDefaultData() {
        val data = ArrayList<LocalMedia>()
        data.add(LocalMedia().apply {
            isChecked = true
            realPath = "file:///android_asset/defaultBg/choose_bg_default1.jpg"
        })
        data.add(LocalMedia().apply {
            realPath = "file:///android_asset/defaultBg/choose_bg_default2.jpg"
        })
        data.add(LocalMedia().apply {
            realPath = "file:///android_asset/defaultBg/choose_bg_default3.JPEG"
        })
        mAdapter.firstRefresh(data)
    }

    private fun initView(context: Context) {
        mBinding = ViewChooseBgBinding.inflate(LayoutInflater.from(context), this, true)
        // LayoutInflater.from(context).inflate(R.layout.view_choose_bg, this, true)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (AtyStateContext.getInstance().state is TryOnCameraAtyState) return
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val result = PictureSelector.obtainSelectorList(data)
                    if (result.isNotEmpty()) {
                        val localMedia: LocalMedia = result[0]

                        if (!hasItem(localMedia.id)) {
                            mAdapter.addData(3, localMedia)
                        }

                        smoothPosition(localMedia.id)
                        LogUtils.iTag(TAG, "onActivityResult: $localMedia")
                    }
                    LogUtils.iTag(TAG, "onActivityResult: $result")
                }
            }
        }
    }

    private fun hasItem(id: Long): Boolean {
        for (item in mAdapter.data) {
            if (item.id == id) {
                return true
            }
        }
        return false
    }

    private fun smoothPosition(id: Long) {
        for ((index, item) in mAdapter.data.withIndex()) {
            if (item.isChecked) {
                setSelected(false, index)
            }
            if (item.id == id) {
                setSelected(true, index)
                smoothMoveToPosition(mBinding.mRecyclerView, index)
            }
        }
    }

    private var mShouldScroll: Boolean? = false
    private var mToPosition: Int? = null
    private fun smoothMoveToPosition(mRecyclerView: RecyclerView, position: Int) {
        val firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0))
        val lastItem =
            mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.childCount - 1))
        if (position < firstItem) {
            mRecyclerView.smoothScrollToPosition(position)
        } else if (position <= lastItem) {
            val movePosition = position - firstItem
            if (movePosition >= 0 && movePosition < mRecyclerView.childCount) {
                val top = mRecyclerView.getChildAt(movePosition).top
                mRecyclerView.smoothScrollBy(0, top)
            }
        } else {
            mRecyclerView.smoothScrollToPosition(position)
            mToPosition = position
            mShouldScroll = true
        }
    }

    interface Listener {
        fun onItemClick(boolean: Boolean, data: LocalMedia, position: Int, bitmap: Bitmap?)
    }

    private var mListener: Listener? = null
    fun setListener(listener: Listener) {
        mListener = listener
    }

}
