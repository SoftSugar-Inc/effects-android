package softsugar.senseme.com.effects.view.widget

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.utils.DateUtils
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.utils.GlideUtils

class ChooseBgAdapter : BaseQuickAdapter<LocalMedia, BaseViewHolder>(R.layout.item_choose_bg, null),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: LocalMedia) {
        val iv_pic = holder.getView<ShapeableImageView>(R.id.iv_pic)
        if (item.chooseModel == SelectMimeType.ofVideo()) {//
            val time = DateUtils.formatDurationTime(item.duration)
            holder.setText(R.id.tvTime, "${time}s")
            holder.setVisible(R.id.tvTime, true)
        } else {
            holder.setVisible(R.id.tvTime, false)
        }
        GlideUtils.load2(context, item.realPath, iv_pic)
        if (item.isChecked) {
            iv_pic.setStrokeColorResource(R.color.color_dd90fa)
        } else {
            iv_pic.setStrokeColorResource(android.R.color.transparent)
        }
    }

    fun firstRefresh(data: MutableList<LocalMedia>?) {
        setNewInstance(data)
    }

    fun loadMoreData(data: List<LocalMedia>?) {
        if (data != null) {
            addData(data)
        }
    }
}