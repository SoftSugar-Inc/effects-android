package softsugar.senseme.com.effects.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import softsugar.senseme.com.effects.adapter.FilterAdapter
import softsugar.senseme.com.effects.adapter.FilterTitleAdapter
import softsugar.senseme.com.effects.databinding.ViewMakeupBinding
import softsugar.senseme.com.effects.db.DBManager
import softsugar.senseme.com.effects.entity.FilterTitleItem
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper
import softsugar.senseme.com.effects.utils.RippleUtils
import softsugar.senseme.com.effects.view.FilterItem
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 滤镜View
 */
class FilterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "FilterView"
        const val DEF_STRENGTH = 0.80f
    }

    private lateinit var mBinding: ViewMakeupBinding

    lateinit var mIbShowOriginal: ImageButton
    private lateinit var mTitleAdapter: FilterTitleAdapter
    private var mCurrentTitleData: FilterTitleItem? = null
    private var mListener: Listener? = null
    private val dbManager = DBManager()

    fun setHighLight(param1: EffectType?, selectedIndexMap: EnumMap<EffectType, Int>?, strengthsMap: EnumMap<EffectType, Float>?) {
        param1?.let {
            getTitleEntityByEnum(param1)?.let { it1 ->
                for (index in mTitleAdapter.data.indices) {
                    val beautyOptionsItem = mTitleAdapter.data[index]
                    beautyOptionsItem.selected = false
                    if (beautyOptionsItem.type == param1){
                        beautyOptionsItem.selected = true
                        break
                    }
                }

                mTitleAdapter.data[getIndexByEnum(param1)].selected = true
                onClickContentAdapter(it1, getIndexByEnum(param1))
            }
        }

        // 查找指定的item
        if (selectedIndexMap != null) {
            for ((type, selectedIndex) in selectedIndexMap) {
                if (selectedIndex >= 0) {
                    mTitleAdapter.data.apply {
                        for (titleItem in this) {
                            if (titleItem.type.name == type.name) {
                                // 一级菜单中 index 用于控制seekbar 显示 隐藏
                                titleItem.sub_menu_selected_position = selectedIndex

                                // 控制二级菜单item选中
                                titleItem?.filterAdapter?.data?.get(selectedIndex)?.selected = true
                                // 添加滤镜->添加风格妆->退出APP重新进入->再去掉风格妆->展示滤镜UI->要显示数据库存储的数据
                                setBackupInfo(titleItem!!.uid, selectedIndex, strengthsMap?.get(titleItem.type)!!)
                            }
                        }
                    }
                }
            }
        }

        strengthsMap?.let {
            val titleList = mTitleAdapter.data
            for (titleItem in titleList) {
                if (strengthsMap.containsKey(titleItem.type)) {
                    titleItem.filterStrength = strengthsMap[titleItem.type]!!
                }
            }
        }

        selectedIndexMap?.let {
            if (!needSetDefStyle(selectedIndexMap)) {
                setDefStrength()
            }
        }

        refreshUI(mCurrentTitleData)
    }

    private fun needSetDefStyle(contentSelectedIndexMap: EnumMap<EffectType, Int>): Boolean {
        var flag = false
        val effectTypes: Set<EffectType> = contentSelectedIndexMap.keys
        for (type in effectTypes) {
            val integer = contentSelectedIndexMap[type]
            if (integer != null && integer >= 0) flag = true
        }
        return flag
    }

    private fun getIndexByEnum(type: EffectType?): Int {
        var index: Int = -1
        type?.apply {
            for (i in mTitleAdapter.data.indices) {
                val beautyOptionsItem = mTitleAdapter.data[i]
                if (beautyOptionsItem.type == type){
                    index = i
                    break
                }
            }
        }
        return index
    }

    private fun getTitleEntityByEnum(type: EffectType?): FilterTitleItem? {
        var item: FilterTitleItem? = null
        type?.apply {
            for (i in mTitleAdapter.data.indices) {
                val beautyOptionsItem = mTitleAdapter.data[i]
                if (beautyOptionsItem.type == type && beautyOptionsItem.enum_name.equals(type.name)) item = beautyOptionsItem
            }
        }
        return item
    }

    init {
        val startTime = System.currentTimeMillis()
        initView(context)
        initData()

        setListener()
        LogUtils.iTag(TAG, "init total cost time:" + (System.currentTimeMillis() - startTime))
    }

    fun getSelectedContentItemMap(): EnumMap<EffectType, FilterItem> {
        val mData: EnumMap<EffectType, FilterItem> = EnumMap(EffectType::class.java)
        val titleList = mTitleAdapter.data
        for (titleItem in titleList) {
            if (titleItem.sub_menu_selected_position >= 0) {
                val filterItem = titleItem.filterAdapter?.data?.get(titleItem.sub_menu_selected_position)
                if (filterItem != null) {
                    mData[titleItem.type] = filterItem as FilterItem?
                }
            }
        }
        return mData
    }

    fun getSelectedStrengthMap(): EnumMap<EffectType, Float> {
        val map = EnumMap<EffectType, Float>(EffectType::class.java)
        mCurrentTitleData?.let {
            map[mCurrentTitleData?.type] = mCurrentTitleData?.filterStrength
        }
        return map
    }

    private fun initData() {
        setDefStrength()
        setDefContentSelectedIndex()
    }

    private fun setDefStrength() {
        for (titleItem in mTitleAdapter.data) {
            titleItem.filterStrength = DEF_STRENGTH
        }
    }

    // 点击事件
    private fun setContentClick(filterAdapter:FilterAdapter) {
        filterAdapter.setOnItemClickListener(object:OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val filterItem = filterAdapter.data[position] as FilterItem

                // 如果已选中，不执行
                if (filterItem.selected) {
                    return
                }

                // 单选，所有置为 false ，当前为true
                val titleList = mTitleAdapter.data
                for (titleItem in titleList) {
                    val contentList = titleItem.filterAdapter.data
                    for (contentItem in contentList) {
                        contentItem.selected = false
                    }
                }

                filterItem.selected = true

                val index: Int = mCurrentTitleData!!.sub_menu_selected_position
                // 如果本分组下没有选中的滤镜，则点击时候将进度调节至默认值80
                if (index < 0) {
                    setDefStrength()
                }

                // 都重置再赋值
                for (titleItem in mTitleAdapter.data) {
                    titleItem.sub_menu_selected_position = -1
                    EffectInfoDataHelper.getInstance().setContentSelectedIndex(titleItem.type, -1)
                }
                mCurrentTitleData?.sub_menu_selected_position = position

                refreshUI(mCurrentTitleData)

                EffectInfoDataHelper.getInstance().setContentSelectedIndex(mCurrentTitleData?.type, position)
                for (titleItem in titleList) {
                    EffectInfoDataHelper.getInstance().setStrength(titleItem.type, mCurrentTitleData!!.filterStrength)
                }

                EffectInfoDataHelper.getInstance().styleHigh = false
                mListener?.onItemClickFilter(position, mCurrentTitleData, filterItem, filterItem.selected, mCurrentTitleData!!.filterStrength, adapter)
                setBackupInfo(mCurrentTitleData!!.uid, position, mCurrentTitleData!!.filterStrength)
                dbManager.insertFilter(mCurrentTitleData?.enum_name, position, filterItem.model, mCurrentTitleData!!.filterStrength)
                EffectInfoDataHelper.getInstance().filterHigh = true

                mCurrentTitleData?.filterAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun setBackupInfo(uid: String, backUpSelectedIndex: Int, backupStrength: Float) {
        for(titleItem in mTitleAdapter.data) {
            titleItem.backupSelectedIndex = null
            titleItem.backupFilterStrength = null
            if (titleItem.uid.equals(uid)) {
                titleItem.backupSelectedIndex = backUpSelectedIndex
                titleItem.backupFilterStrength = backupStrength
            }
        }
    }

    // 恢复手动调节的UI，overlap场景使用
    @SuppressLint("NotifyDataSetChanged")
    fun restoreUI() {
        for(titleItem in mTitleAdapter.data) {
            if (titleItem.backupSelectedIndex != null) {
                titleItem.sub_menu_selected_position = titleItem.backupSelectedIndex
                titleItem.filterStrength = titleItem.backupFilterStrength
                titleItem.filterAdapter.data[titleItem.backupSelectedIndex].selected = true

                titleItem.filterAdapter.notifyDataSetChanged()
            }
        }
        refreshUI(mCurrentTitleData)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListener() {
        mBinding.sbStrength.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                if (fromUser) {
                    mCurrentTitleData!!.filterStrength = progress
                    mCurrentTitleData?.let { mListener?.onProgressChangedFilter(it, seekBar, progress, true) }

                    setBackupInfo(mCurrentTitleData!!.uid, mCurrentTitleData!!.sub_menu_selected_position, mCurrentTitleData!!.filterStrength)
                    dbManager.updateFilterStrength(progress)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val pro = mCurrentTitleData!!.filterStrength
                for (item in mTitleAdapter.data) {
                    pro.let { EffectInfoDataHelper.getInstance().setStrength(item.type, it) }
                }
            }

        })

        mTitleAdapter.setOnItemClickListener(object:OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                mCurrentTitleData = adapter.data[position] as FilterTitleItem?

                if (mCurrentTitleData?.selected == true) {
                    return
                }
                for (index in adapter.data.indices) {
                    if (index != position) {
                        val entity = adapter.data[index] as FilterTitleItem
                        entity.selected = false
                    }
                }
                mTitleAdapter.data[position].selected = true

                mCurrentTitleData.apply {
                    mBinding.mContentRecycler.adapter = mCurrentTitleData?.filterAdapter
                }
                mTitleAdapter.notifyDataSetChanged()

                refreshUI(mCurrentTitleData)
            }
        })

        mBinding.dtClear.setOnClickListener {
            setDefContentSelectedIndex()
            setDefStrength()
            refreshUI(mCurrentTitleData)
            EffectInfoDataHelper.getInstance().filterHigh = false
            mListener?.onClickClearFilter()

            val titleList = mTitleAdapter.data
            for (titleItem in titleList) {
                EffectInfoDataHelper.getInstance().setContentSelectedIndex(titleItem.type, -1)
                EffectInfoDataHelper.getInstance().setStrength(titleItem.type, DEF_STRENGTH)
            }

            for (titleItem in titleList) {
                val contentList = titleItem.filterAdapter.data
                for (contentItem in contentList) {
                    contentItem.selected = false
                }
            }
            dbManager.clearFilter()
            mCurrentTitleData?.filterAdapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onClickContentAdapter(
        entity: FilterTitleItem,
        position: Int
    ) {
        mCurrentTitleData = entity

        if (position>=0) {
            mTitleAdapter.data[position].selected = true
            mTitleAdapter.notifyDataSetChanged()

            entity.apply {
                mBinding.mContentRecycler.adapter = mTitleAdapter.data[position].filterAdapter
            }
        }

        refreshUI(mCurrentTitleData)
    }

    private fun setDefContentSelectedIndex() {
        val titleList = mTitleAdapter.data
        if (titleList.size>0) {
            for (titleItem in titleList) {
                titleItem.sub_menu_selected_position = -1

                for (filterItem in titleItem.filterAdapter.data) {
                    filterItem.selected = false
                }
            }
        }
    }

    private fun refreshUI(currentTitleData: FilterTitleItem?) {
        mBinding.sbStrength.visibility = INVISIBLE
        val contentSelectedIndex = currentTitleData?.sub_menu_selected_position
        if (null != contentSelectedIndex && contentSelectedIndex >= 0) {
            mBinding.sbStrength.visibility = VISIBLE
        } else {
            mBinding.sbStrength.visibility = INVISIBLE
        }

        currentTitleData?.filterStrength?.let { mBinding.sbStrength.setValue(it) }
    }

    private fun initView(context: Context) {
        mBinding = ViewMakeupBinding.inflate(LayoutInflater.from(context), this, true)
        setOpenVar()
        RippleUtils.setForeground(context, mBinding.dtClear)
        initTitleRecyclerView()
        initContentRecyclerView()
    }

    private fun setOpenVar() {
        mIbShowOriginal = mBinding.ibShowOriginal
    }

    private fun initTitleRecyclerView() {
        val ms = LinearLayoutManager(context)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        mBinding.mTitleRecycleView.layoutManager = ms

        mTitleAdapter = FilterTitleAdapter()
        mBinding.mTitleRecycleView.adapter = mTitleAdapter
    }

    private fun initContentRecyclerView() {
        val ms = LinearLayoutManager(context)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        mBinding.mContentRecycler.layoutManager = ms
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearContentSelected() {
        setDefContentSelectedIndex()
        setDefStrength()

        refreshUI(mCurrentTitleData)
        EffectInfoDataHelper.getInstance().filterHigh = false
        mCurrentTitleData?.filterAdapter?.notifyDataSetChanged()
    }

    fun init(titleData: ArrayList<FilterTitleItem>, contentData: HashMap<EffectType, ArrayList<FilterItem>>?) {
        mTitleAdapter.setNewInstance(titleData)

        contentData?.apply {
            for (item in titleData) {
                val type = item.type
                val list: ArrayList<FilterItem>? = contentData[type]

                if (item.filterAdapter == null) {
                    item.filterAdapter = FilterAdapter()
                    val adapter = item.filterAdapter
                    setContentClick(adapter)
                    adapter.setNewInstance(list)
                }
            }
        }
        if (titleData.size>0) {
            mCurrentTitleData = titleData[0]
        }
        mBinding.mContentRecycler.adapter = mCurrentTitleData?.filterAdapter
        refreshUI(mCurrentTitleData)
    }

    interface Listener {
        // 滤镜清零
        fun onClickClearFilter()
        // 选中滤镜
        fun onItemClickFilter(position: Int, titleEntity: FilterTitleItem?, contentEntity: LinkageEntity, selected: Boolean, strength: Float, adapter: RecyclerView.Adapter<*>)
        // 调节强度
        fun onProgressChangedFilter(titleEntity: FilterTitleItem, seekBar: SeekBar?, progress: Float, fromUser: Boolean)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

}