package softsugar.senseme.com.effects.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import softsugar.senseme.com.effects.adapter.BaseBottomGroupAdapter
import softsugar.senseme.com.effects.adapter.BasicBeautyTitleAdapter
import softsugar.senseme.com.effects.adapter.BeautyItemAdapter
import softsugar.senseme.com.effects.databinding.ViewBasicEffectBinding
import softsugar.senseme.com.effects.db.DBManager
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper
import softsugar.senseme.com.effects.utils.LocalDataStore
import softsugar.senseme.com.effects.utils.RippleUtils
import softsugar.senseme.com.effects.utils.RxEventBus
import softsugar.senseme.com.effects.utils.STUtils
import softsugar.senseme.com.effects.view.BeautyItem
import java.util.EnumMap

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 7/1/21 7:36 PM
 */
class BasicEffectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    companion object {
        const val TAG = "BasicEffectView"
    }

    private val dbManager = DBManager()

    private var bottomAdapter: BaseBottomGroupAdapter

    private lateinit var mTitleAdapter: BasicBeautyTitleAdapter

    private var mCurrentTitleData: BasicBeautyTitleItem? = null
    private var mCurrentContentData: BeautyItem? = null
    private var mCurrentContentAdapter: BeautyItemAdapter? = null

    // 加风格移除风格妆时候再恢复 base beauty ui
    public var backupList: ArrayList<BeautyItem> ?= null

    private lateinit var mBinding: ViewBasicEffectBinding

    private val mContentAdaptersMap: EnumMap<EffectType, BeautyItemAdapter> = EnumMap<EffectType, BeautyItemAdapter>(EffectType::class.java)

    fun backUpList() {
        backupList = ArrayList()
        for ((type, adapter) in mContentAdaptersMap) {
            val list:ArrayList<BeautyItem> = adapter.data
            for(item in list) {
                backupList!!.add(item.clone())
            }
        }
    }

    fun updateBackListStrength(enumName: String, strength: Float) {
        if (backupList!=null) {
            for (item in backupList!!) {
                if (item.enum_name.equals(enumName)) {
                    item.progress = strength
                }
            }
        }
    }

    init {
        initView(context)
        // 基础美颜
        val titleList = LocalDataStore.getInstance().beautyOptionsList
        for (title in titleList) {
            if (title.assets_path_sub_menus != null) {
                for ((index, value) in title.third_adapter_enum.withIndex()) {
                    val assetPath = title.assets_path_sub_menus[index]
                    val adapter = BeautyItemAdapter(context, LocalDataStore.getInstance().getBeautyList(assetPath, 0))
                    mContentAdaptersMap[EffectType.getTypeByName(value)] = adapter
                }
            }
        }

        backUpList()
        initData()

        setListener()

        mCurrentTitleData = mTitleAdapter.data[0]
        mBinding.mContentRecycler.adapter = mContentAdaptersMap[EffectType.TYPE_BASE]
        mCurrentContentAdapter = mContentAdaptersMap[mCurrentTitleData?.type]
        mCurrentContentData = mCurrentContentAdapter?.data?.get(0)

        mBinding.rlBottomGroup.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottomAdapter = BaseBottomGroupAdapter()
        mBinding.rlBottomGroup.adapter = bottomAdapter
    }

    // 数据库恢复数据更新UI用.
    @SuppressLint("NotifyDataSetChanged")
    fun setHighLightNew2(beautyItem: BeautyItem?) {
        updateBackListStrength(beautyItem!!.enum_name, beautyItem.progress)
        if (beautyItem != null) {
            for ((type, adapter) in mContentAdaptersMap) {
                for (entity in adapter.data) {
                    if (beautyItem.beauty_type == entity.beauty_type && entity.enum_name.equals(beautyItem.enum_name)) {
                        entity.progress = beautyItem.progress
                        entity.current_use = true
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
        refreshUI(mCurrentContentData)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun overlapBefore(beautyItem: BeautyItem?) {
        if (beautyItem != null) {
            for ((type, adapter) in mContentAdaptersMap) {
                for (entity in adapter.data) {
                    if (beautyItem.beauty_type == entity.beauty_type && entity.enum_name.equals(beautyItem.enum_name)) {
                        entity.progress = beautyItem.progress
                        entity.current_use = true
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
        refreshUI(mCurrentContentData)
    }

    // overlap用
    fun setHighLightNew(beautyItem: BeautyItem?) {
        if (beautyItem != null) {
            for ((type, adapter) in mContentAdaptersMap) {
                for (entity in adapter.data) {
                    if (beautyItem.beauty_type == entity.beauty_type /*&& (entity.mode == beautyItem.mode)*/) {
                        if (entity.mode == beautyItem.mode) {
                            entity.progress = beautyItem.progress
                            entity.current_use = true
                        } else {// 互斥逻辑
                            entity.progress = 0f
                            entity.current_use = false
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
        refreshUI(mCurrentContentData)
    }

    // beautyList 数据库中的数据
    fun setHighLight(
        param1: EffectType?,
        param2: EnumMap<EffectType, Int>?,
        dBbeautyList: ArrayList<BeautyItem>?
    ) {
        param1?.let {
            getTitleEntityByEnum(param1)?.let { it1 ->
                mTitleAdapter.setSelectedPosition(getIndexByEnum(param1))
                onClickTitleAdapter(it1, getIndexByEnum(param1))
            }
        }

        // 设置二级菜单子项高亮
        param2?.let {
            refreshUI(mCurrentContentData)
        }

        refreshUI(mCurrentContentData)
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

    private fun getTitleEntityByEnum(type: EffectType?): BasicBeautyTitleItem? {
        var item: BasicBeautyTitleItem? = null
        type?.apply {
            for (i in mTitleAdapter.data.indices) {
                val beautyOptionsItem = mTitleAdapter.data[i]
                if (beautyOptionsItem.type == type) item = beautyOptionsItem
            }
        }
        return item
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListener() {
        mBinding.dtBasicReset.setOnClickListener {
            // 数据重置
            var typeArr = mCurrentTitleData?.third_adapter_enum

            for (type in typeArr!!) {
                var enumType: EffectType = EffectType.getTypeByName(type)!!
                var contentsList: ArrayList<BeautyItem>? = mContentAdaptersMap[enumType]?.data

                if (contentsList!=null) {
                    for(item in contentsList) {
                        item.progress = item.def_strength
                        dbManager.insertBaseBeauty(item)
                    }
                }
                mCurrentTitleData?.let { it1 -> mListener?.onClickResetBasicEffect(it1, contentsList!!) }
            }
        }

        // 清零
        mBinding.dtBasicClear.setOnClickListener {
            var typeArr = mCurrentTitleData?.third_adapter_enum

            mCurrentTitleData?.curr_sub_menu_select_uid = null

            for (type in typeArr!!) {
                var enumType: EffectType = EffectType.getTypeByName(type)!!
                var contentsList: ArrayList<BeautyItem>? = mContentAdaptersMap[enumType]?.data
                if (contentsList != null) {
                    for (item in contentsList) {
                        item.progress = 0f
                        updateBackListStrength(item.type.name, 0f)
                        EffectInfoDataHelper.getInstance().setStrength(item.type, 0f)
                        mListener?.onProgressChangedBasicEffect(mCurrentTitleData, item, 0f, false)

                        dbManager.insertBaseBeauty(item)
                    }
                }
                mContentAdaptersMap[enumType]?.setSelectedPosition(-1)
                mContentAdaptersMap[enumType]?.notifyDataSetChanged()
            }
            mBinding.sbStrength.visibility = View.INVISIBLE
            refreshUI(mCurrentContentData)
        }

        mTitleAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val entity = mTitleAdapter.data[position]
                if (entity.selected) return

                onClickTitleAdapter(entity, position)

                // fix bug : Android，微整形/3D微整形，切换导航-眼睛，选择调整，再切换回微整形/3D微整形，功能列表从第一个展示，导航栏还显示眼睛
                if (mCurrentTitleData?.groups!=null) {
                    for((index, groupItem) in bottomAdapter.data.withIndex()) {
                        groupItem.selected = index == 0
                    }
                    bottomAdapter.notifyDataSetChanged()
                }
            }
        })

        // contentAdapter 点击
        for ((_, value) in mContentAdaptersMap) {
            value.setListener { position, item ->
                mCurrentContentData = item
                mCurrentTitleData?.curr_sub_menu_select_uid = mCurrentContentData?.uid
                if (mCurrentContentData?.click_goto_enum_num != null) {
                    mBinding.mContentRecycler.adapter = mContentAdaptersMap[EffectType.getTypeByName(mCurrentContentData?.click_goto_enum_num)]

                    // 因为不记录切换的tab点返回后，重置位置
                    if (mCurrentTitleData?.groups!=null) {
                        for((index, groupItem) in bottomAdapter.data.withIndex()) {
                            groupItem.selected = index == 0
                        }
                        bottomAdapter.notifyDataSetChanged()
                    }

                    mCurrentTitleData?.sub_selected_adapter_enum = mCurrentContentData?.click_goto_enum_num
                }
                refreshUI(mCurrentContentData)
            }
        }

        mBinding.sbStrength.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                if (fromUser) {
                    setMutualNew(mCurrentTitleData)
                    mCurrentContentData?.progress = progress
                    mCurrentContentData?.skip_set_when_restart = false
                    mContentAdaptersMap[mCurrentTitleData?.type]?.notifyDataSetChanged()
                    mCurrentContentData?.let {
                        mListener?.onProgressChangedBasicEffect(mCurrentTitleData, it, progress, true)
                    }
                    if (progress == 0f || progress == 1f || progress == -1f) {
                        RxEventBus.vibrator.onNext(true)
                    }

                    updateBackListStrength(mCurrentContentData!!.enum_name, progress)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                EffectInfoDataHelper.getInstance().setStrength(mCurrentContentData?.type, mCurrentContentData!!.progress)
                updateBackListStrength(mCurrentContentData!!.enum_name, mCurrentContentData!!.progress)
                dbManager.insertBaseBeauty(mCurrentContentData)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setMutualNew(currentTitleData: BasicBeautyTitleItem?) {
        // 最新互斥
        val thirdAdapterEnum = currentTitleData?.third_adapter_enum
        if (thirdAdapterEnum != null) {
            for (item in thirdAdapterEnum) {
                val enumType = EffectType.getTypeByName(item)
                val beautyItems = mContentAdaptersMap[enumType]?.data
                for (beautyItem in beautyItems!!) {

                    val mutualArr = mCurrentContentData?.mutual_arr
                    if (mutualArr!=null) {
                        for (needMutualItem in mutualArr) {
                            if (mutualArr.contains(beautyItem.uid) && beautyItem.progress>0) {
                                EffectInfoDataHelper.getInstance().setStrength(beautyItem.type, 0f)
                                beautyItem.progress = 0f
                                beautyItem.current_use = false
                                beautyItem.skip_set_when_restart = true
                                dbManager.insertBaseBeauty(beautyItem)

                                updateBackListStrength(beautyItem!!.enum_name, 0f)

                                mListener?.onProgressChangedBasicEffect(null, beautyItem, 0f, true)
                            }
                        }
                    }
                    mContentAdaptersMap[enumType]?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun onClickTitleAdapter(
        entity: BasicBeautyTitleItem,
        position: Int,
    ) {
        mCurrentTitleData = entity

        if (mCurrentTitleData?.sub_selected_adapter_enum != null) {
            mCurrentContentAdapter =
                mContentAdaptersMap[EffectType.getTypeByName(mCurrentTitleData?.sub_selected_adapter_enum)]
        } else {
            mCurrentContentAdapter = mContentAdaptersMap[mCurrentTitleData?.type]
        }

        mCurrentContentData = null
        for (enumStr in mCurrentTitleData?.third_adapter_enum!!) {
            val datas = mContentAdaptersMap[EffectType.getTypeByName(enumStr)]?.data
            if (datas != null) {
                for (data in datas) {
                    if (mCurrentTitleData?.curr_sub_menu_select_uid != null && mCurrentTitleData?.curr_sub_menu_select_uid.equals(
                            data.uid
                        )
                    ) {
                        mCurrentContentData = data
                    }
                }
            }
        }

        mTitleAdapter.setSelectedPosition(position)
        entity.apply {
            if (mCurrentTitleData?.sub_selected_adapter_enum != null) {
                val contentAdapter =
                    mContentAdaptersMap[EffectType.getTypeByName(mCurrentTitleData?.sub_selected_adapter_enum)]
                mBinding.mContentRecycler.adapter = contentAdapter
            } else {
                val contentAdapter = mContentAdaptersMap[mCurrentTitleData?.type]
                mBinding.mContentRecycler.adapter = contentAdapter
            }
        }

        // 控制seekBar显示或隐藏
        refreshUI(mCurrentContentData)

        if (mCurrentTitleData?.groups != null && mCurrentTitleData?.groups?.size!! > 0) {
            mBinding.rlBottomGroup.visibility = View.VISIBLE
            bottomAdapter.setNewInstance(mCurrentTitleData?.groups!!)
            bottomAdapter?.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                    val datas = adapter.data
                    for (item in datas) {
                        (item as BasicBeautyTitleItem.Groups).selected = false
                    }
                    val item: BasicBeautyTitleItem.Groups = datas.get(position) as BasicBeautyTitleItem.Groups
                    item.selected = true

                    var needPosition = -1;
                    for ((index, value) in mCurrentContentAdapter?.data!!.withIndex()) {
                        if (value.uid != null && value.uid.equals(item.scroll_to_position_uid)) {
                            needPosition = index
                            break
                        }
                    }
                    if (needPosition != -1) {
                        scrollToPosition(mBinding.mContentRecycler, needPosition)
                    }
                    bottomAdapter.notifyDataSetChanged()
                }
            })
        } else {
            mBinding.rlBottomGroup.visibility = View.INVISIBLE
        }
    }


    private fun refreshUI(currentContentData: BeautyItem?) {
        if (currentContentData?.start_center == true) {
            mBinding.sbStrength.setShowType(TextThumbSeekBar.Type.START_CENTER)
        } else {
            mBinding.sbStrength.setShowType(TextThumbSeekBar.Type.START_LEFT)
        }
        currentContentData?.progress?.let { mBinding.sbStrength.setValue(it) }

        if (currentContentData == null || currentContentData!!.no_seekbar) {
            mBinding.sbStrength.visibility = View.INVISIBLE
        } else {
            mBinding.sbStrength.visibility = View.VISIBLE
        }

        if (mCurrentTitleData?.curr_sub_menu_select_uid == null) {
            mBinding.sbStrength.visibility = View.INVISIBLE
        }

        // 微整形-高阶瘦脸，点击下面的展示bar切换脸型/鼻子/嘴巴/眼睛/眉毛，不会跳转
        // 针对以上bug做隐藏处理
        if (mCurrentTitleData?.groups != null && mCurrentTitleData?.groups!!.size > 0 && null!=mCurrentTitleData?.sub_selected_adapter_enum) {
            if (mCurrentTitleData?.sub_selected_adapter_enum.equals(mCurrentTitleData?.third_adapter_enum?.get(0)!!)) {
                mBinding.rlBottomGroup.visibility = View.VISIBLE
            } else {
                mBinding.rlBottomGroup.visibility = View.INVISIBLE
            }
        }
    }

    private fun initData() {
        mTitleAdapter.refreshData(LocalDataStore.getInstance().beautyOptionsList)
    }

    lateinit var mIbShowOriginal: ImageButton

    private fun setOpenVar() {
        mIbShowOriginal = mBinding.ibShowOriginal
    }

    private fun scrollToPosition(recyclerView: RecyclerView, position: Int) {
        if (position != -1) {
            recyclerView.scrollToPosition(position)
            val mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            mLayoutManager.scrollToPositionWithOffset(position, 0)
        }
    }

    private fun initView(context: Context) {
        mBinding = ViewBasicEffectBinding.inflate(LayoutInflater.from(context), this, true)
        RippleUtils.setForeground(context, mBinding.dtBasicClear, mBinding.dtBasicReset)
        setOpenVar()
        initTitleRecyclerView()
        initContentRecyclerView()
    }

    private fun initTitleRecyclerView() {
        val ms = LinearLayoutManager(context)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        mBinding.mTitleRecycleView.layoutManager = ms

        // 标题的listView设置
        mTitleAdapter = BasicBeautyTitleAdapter()
        mBinding.mTitleRecycleView.adapter = mTitleAdapter
    }

    private fun initContentRecyclerView() {
        val ms = LinearLayoutManager(context)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        mBinding.mContentRecycler.layoutManager = ms
        mBinding.mContentRecycler.addItemDecoration(BeautyItemDecoration(STUtils.dip2px(context, 15f)))
    }

    internal class BeautyItemDecoration(private val space: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = space
            outRect.right = space
        }
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onProgressChangedBasicEffect(titleData: BasicBeautyTitleItem?, contentEntity: BeautyItem, progress: Float, fromUser: Boolean, )

        fun onClickResetBasicEffect(currentTitleData: BasicBeautyTitleItem, dataList: ArrayList<BeautyItem>)
    }

}