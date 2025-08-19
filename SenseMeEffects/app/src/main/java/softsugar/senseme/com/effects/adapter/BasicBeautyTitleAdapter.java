package softsugar.senseme.com.effects.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.utils.ResUtil;

// 基础美颜一级菜单
public class BasicBeautyTitleAdapter extends BaseQuickAdapter<BasicBeautyTitleItem, BaseViewHolder> {

    public BasicBeautyTitleAdapter() {
        super(R.layout.beauty_options_item, null);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, BasicBeautyTitleItem titleItem) {
        int nameStrId = ResUtil.Companion.getStringId(getContext(), titleItem.display_des_res_id);
        baseViewHolder.setText(R.id.iv_beauty_options, nameStrId);
        if (titleItem.selected) {
            baseViewHolder.setTextColor(R.id.iv_beauty_options, getContext().getResources().getColor(R.color.color_ffffff));
        } else {
            baseViewHolder.setTextColor(R.id.iv_beauty_options, getContext().getResources().getColor(R.color.color_80ffffff));
        }
        baseViewHolder.setVisible(R.id.iv_select_flag, titleItem.selected);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(ArrayList<BasicBeautyTitleItem> data) {
        this.setNewInstance(data);
        this.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedPosition(int position) {
        for (BasicBeautyTitleItem item : getData()) {
            item.selected = false;
        }
        getData().get(position).selected = true;
        notifyDataSetChanged();
    }
}
