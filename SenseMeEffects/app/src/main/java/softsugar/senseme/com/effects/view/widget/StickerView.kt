package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.softsugar.library.api.Material
import com.softsugar.library.sdk.listener.DownloadListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.StickerTitleEntity
import softsugar.senseme.com.effects.adapter.StickerContentAdapter
import softsugar.senseme.com.effects.adapter.StickerTitleAdapter
import softsugar.senseme.com.effects.databinding.ViewStickerBinding
import softsugar.senseme.com.effects.utils.Constants
import softsugar.senseme.com.effects.utils.ContextHolder
import softsugar.senseme.com.effects.utils.EventBusUtils
import softsugar.senseme.com.effects.utils.FileUtils
import softsugar.senseme.com.effects.utils.LocalDataStore
import softsugar.senseme.com.effects.utils.MultiLanguageUtils
import softsugar.senseme.com.effects.utils.NetworkUtils
import softsugar.senseme.com.effects.utils.SpUtils
import softsugar.senseme.com.effects.utils.SpaceItemDecoration
import softsugar.senseme.com.effects.utils.StyleDataNewUtils
import softsugar.senseme.com.effects.utils.ThreadUtils
import softsugar.senseme.com.effects.view.StickerItem
import softsugar.senseme.com.effects.view.StickerState
import java.util.EnumMap

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 7/1/21 1:12 PM
 */
class StickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "StickerView"
    }

    var mListener: Listener? = null
    private var mCurrentContentPosition: Int = 0
    private lateinit var mBinding: ViewStickerBinding
    private lateinit var mTitleAdapter: StickerTitleAdapter
    private var mCurrentContentAdapter: StickerContentAdapter? = null
    private var mCurrentTitleData: StickerTitleEntity? = null

    init {
        initView(context)
        setListener()
    }

    fun setHighLight(param1: EffectType?) {
        mTitleAdapter.setSelectedPosition(getIndexByEnum(param1))
        mBinding.mTitleRecyclerView.smoothScrollToPosition(getIndexByEnum(param1))
        param1?.let {
            getTitleEntityByEnum(param1)?.let { it1 ->
                onClickTitleAdapter(it1, getIndexByEnum(param1))
            }
        }
    }

    private fun getIndexByEnum(type: EffectType?): Int {
        var index: Int = -1
        type?.apply {
            for (i in mTitleAdapter.data.indices) {
                val beautyOptionsItem = mTitleAdapter.data[i]
                if (beautyOptionsItem.type == type) index = i
            }
        }
        return index
    }

    private fun getTitleEntityByEnum(type: EffectType?): StickerTitleEntity? {
        var item: StickerTitleEntity? = null
        type?.apply {
            for (i in mTitleAdapter.data.indices) {
                val beautyOptionsItem = mTitleAdapter.data[i]
                if (beautyOptionsItem.type == type) item = beautyOptionsItem
            }
        }
        return item
    }

    private fun initView(context: Context) {
        mBinding = ViewStickerBinding.inflate(LayoutInflater.from(context), this, true)
        initTitleRecyclerView()

        mBinding.rvStickerIcons.layoutManager = GridLayoutManager(context, 6)
        mBinding.rvStickerIcons.addItemDecoration(SpaceItemDecoration(0))
    }

    private fun onClickTitleAdapter(entity: StickerTitleEntity, position: Int) {
        mCurrentTitleData = entity
        mTitleAdapter.setSelectedPosition(position)
        mCurrentContentAdapter = mCurrentTitleData?.stickerAdapter
        mBinding.rvStickerIcons.adapter = entity.stickerAdapter

        mBinding.rvStickerIcons.layoutManager = GridLayoutManager(context, 6)
        mBinding.rvStickerIcons.addItemDecoration(SpaceItemDecoration(0))

        if (mCurrentContentAdapter?.data.isNullOrEmpty() && mCurrentTitleData?.type!=EffectType.TYPE_STICKER_LOCAL && mCurrentTitleData?.type!=EffectType.TYPE_STICKER_ADD) {
            Toast.makeText(ContextHolder.getContext(), MultiLanguageUtils.getStr(R.string.toast_sticker_pull), Toast.LENGTH_SHORT).show()
        }
        if (mCurrentTitleData?.type == EffectType.TYPE_STICKER_TRACK) {
            mListener?.onSelectedObjectTrack()
        }
    }

    private fun setAdapterListener(stickerAdapter: StickerContentAdapter) {
        stickerAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val data = stickerAdapter.data.get(position)
                mCurrentContentPosition = position

                // 文件损坏时候，会删除文件，所以这里判断是否存在，不存在重新下载
                if (!com.blankj.utilcode.util.FileUtils.isFileExists(data.path)) {
                    data.setState(StickerState.NORMAL_STATE)
                    Log.i(TAG, "file not found. retry download.");
                }

                if (data?.getState() == StickerState.NORMAL_STATE) {
                    if (!NetworkUtils.isNetworkAvailable(context)) {
                        Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show()
                        return
                    }
                    data.setState(StickerState.LOADING_STATE)
                    mCurrentTitleData?.stickerAdapter?.notifyItemChanged(position)
                    setOnClickDoneView(position, true)
                    refreshUI()
                    Material.downLoadZip(data.pkgUrl, object:DownloadListener {
                        override fun onDownloading() {
                        }

                        override fun onFail(errorInfo: String) {
                            LogUtils.d("onFail() called with: errorInfo = $errorInfo")
                            ThreadUtils.getInstance().runOnUIThread {
                                data.setState(StickerState.NORMAL_STATE)
                                mCurrentTitleData?.stickerAdapter?.notifyItemChanged(position)
                            }
                        }

                        override fun onFinish(path: String) {
                            post {
                                data.setPath(path)
                                data.setState(StickerState.DONE_STATE)
                                mCurrentTitleData?.stickerAdapter?.notifyItemChanged(position)
                                if (mCurrentContentPosition == position) {
                                        setGanStickerSelectedListener(data, mCurrentTitleData, true)
                                        mListener?.onItemClickSticker(position, mCurrentTitleData, data, true)
                                        chooseBg(data, true)
                                }
                            }
                        }

                        override fun onProgress(progress: Int) {
                        }

                        override fun onStart() {
                        }
                    })
                    return
                } else {
                    setOnClickDoneView(position, true)
                    refreshUI()
                    data?.let {
                            setGanStickerSelectedListener(data, mCurrentTitleData, true)
                            mListener?.onItemClickSticker(position, mCurrentTitleData, it, true)
                            chooseBg(it, true)
                    }
                }
            }

        })
    }

    private fun setListener() {
        mBinding.rvCloseSticker.setOnClickListener {
            for (titleItem in mTitleAdapter.data) {
                titleItem.stickerAdapter.setSelectedPosition(-1)
            }
            for (titleItem in mTitleAdapter.data) {
                titleItem.subMenuSelectedPosition = -1
            }
            mListener?.onClickClearSticker()
            mListener?.onGanStickerSelected(false)
        }

        mTitleAdapter.setOnItemClickListener { adapter, view, position ->
            val entity: StickerTitleEntity = mTitleAdapter.data[position]
            onClickTitleAdapter(entity, position)
        }
    }

    private fun setGanStickerSelectedListener(data: StickerItem, titleData: StickerTitleEntity?, selected: Boolean) {
        if (titleData == null || data.name == null) return
        // 是否是GAN贴纸
        val isGanSticker = titleData.type == EffectType.TYPE_STICKER_GAN
        if (titleData.type == EffectType.TYPE_STICKER_ADD) {
            if (!isGanSticker) return
            mListener?.onGanStickerSelected(selected)
        } else {
            if (isGanSticker && selected) {
                mListener?.onGanStickerSelected(true)
            } else {
                mListener?.onGanStickerSelected(false)
            }
        }
    }

    private fun chooseBg(data: StickerItem, selected: Boolean) {
        LogUtils.iTag(TAG, "chooseBg() called with: data = $data, selected = $selected")
        if (data.id == Constants.CHOOSE_BG_STICKER_ID ||
            data.id == Constants.CHOOSE_GREEN_ID ||
            data.id == Constants.CHOOSE_REED_ID ||
            data.id == Constants.CHOOSE_BLUE_ID ||
            data.id == Constants.CHOOSE_GREEN_VIDEO_ID) {//背景素材点击
        } else {// 选择的贴纸不是背景贴纸
            ContextHolder.setCurrentBg("")
        }
    }

    private fun setOnClickDoneView(position: Int, selected: Boolean?) {
        for (titleItem in mTitleAdapter.data) {
            // 通用物体追踪，不需要将其它tab置为-1，通用物体追踪可以其它贴纸共存
            if (mCurrentTitleData?.type!=EffectType.TYPE_STICKER_TRACK) {
                titleItem.subMenuSelectedPosition = -1;
            }
        }
        mCurrentTitleData?.subMenuSelectedPosition = position
        if (selected != null && !selected)
            mCurrentTitleData?.subMenuSelectedPosition = -1
    }

    private fun refreshUI() {
        for(titleItem in mTitleAdapter.data) {
            titleItem.stickerAdapter.setSelectedPosition(titleItem.subMenuSelectedPosition)
        }
    }

    fun setDataSync(stickerItem: StickerItem) {
        stickerItem.apply {
            for (titleItem in mTitleAdapter.data) {
                if (titleItem.type == EffectType.TYPE_STICKER_SYNC) {
                    titleItem.stickerAdapter.addHeadData(this)
                }
            }
        }
    }

    fun setData(contentData: EnumMap<EffectType, MutableList<StickerItem>>?) {
        for(index in mTitleAdapter.data.indices) {
            val titleItem = mTitleAdapter.data[index]
            if (index == 0) {
                mBinding.rvStickerIcons.adapter = titleItem.stickerAdapter
            }
        }

        contentData?.apply {
            for ((key, value) in contentData) {
                if (key != EffectType.TYPE_STICKER_TRACK && key != EffectType.TYPE_STICKER_ADD && key != EffectType.TYPE_STICKER_LOCAL && key != EffectType.TYPE_STICKER_SYNC) {
                    for (titleItem in mTitleAdapter.data) {
                        if (titleItem.type == key) {
                            // titleItem.stickerAdapter.refreshData(value)
                        }
                    }
                }
            }
        }
    }

    interface FinishCallback {
        fun finishExecute()
    }

    private fun setCustomData(item: StickerTitleEntity, stickerAdapter: StickerContentAdapter) {
        if (item.asset_path!=null) {
            val list = FileUtils.getStickerFiles(context, item.asset_path)
            Log.i("lugq", "setCustomDataSize=${list.size}")
            item.stickerAdapter.refreshData(list)
            mTitleAdapter.data
        }

        /*
        if (item.asset_path!=null) {
            val dis = Observable.create<ArrayList<StickerItem>> { observer->
            val list = FileUtils.getStickerFiles(context, item.asset_path)
                observer.onNext(list)
                observer.onComplete()
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    item.stickerAdapter.refreshData(list)
                    mTitleAdapter.data

                }
        }*/
    }

    fun init(titleData: ArrayList<StickerTitleEntity>, contentData: HashMap<EffectType, MutableList<StickerItem>>?, callback: FinishCallback) {
        mTitleAdapter.setNewInstance(titleData)
        contentData?.apply {
            // 当前是整妆的 设置子项 每个adapter都设置数据
            for (item in mTitleAdapter.data) {
                if (item.stickerAdapter == null) {
                    item.stickerAdapter = StickerContentAdapter()
                    setCustomData(item, item.stickerAdapter)

                    setAdapterListener(item.stickerAdapter)
                }

                val type = item.type
                val list: MutableList<StickerItem>? = contentData[type]
                list?.apply {
                    // 如果是<同步>则排序
                    if (type == EffectType.TYPE_STICKER_SYNC) {
                        for(i in list) {
                            i as StickerItem
                            i.orderTimeStamp = SpUtils.getParam(i.path + "time", 0L) as Long
                        }
                        list as MutableList<StickerItem>
                        list.sortByDescending {
                            it.orderTimeStamp
                        }
                    }
                    item.stickerAdapter?.refreshData(this)
                }
            }
        }

        // 默认是第一项
        mCurrentTitleData = mTitleAdapter.data[0]
        mBinding.rvStickerIcons.adapter = mCurrentTitleData?.stickerAdapter

        GlobalScope.launch(Dispatchers.Main) {
            val data = StyleDataNewUtils.getStyleMap(EffectType.stickerList)
            setData(data)
            refreshUI()
            callback.finishExecute()
        }
    }

    private fun initTitleRecyclerView() {
        mBinding.mTitleRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        mBinding.mTitleRecyclerView.addItemDecoration(SpaceItemDecoration(0))
        mTitleAdapter = StickerTitleAdapter()
        mBinding.mTitleRecyclerView.adapter = mTitleAdapter
        //mTitleAdapter.setNewInstance(LocalDataStore.getInstance().stickerOptionsListNew)
    }

    private fun initContentRecyclerView() {
        mBinding.rvStickerIcons.layoutManager = GridLayoutManager(context, 6)
        mBinding.rvStickerIcons.addItemDecoration(SpaceItemDecoration(0))
    }

    interface Listener {
        fun onItemClickSticker(position: Int, titleEntity: StickerTitleEntity?, contentEntity: LinkageEntity, selected: Boolean)
        fun onClickClearSticker()
        fun onSelectedObjectTrack()
        // gan 贴纸选中状态监听回调，用于横竖屏提示
        fun onGanStickerSelected(selected: Boolean)
    }
}