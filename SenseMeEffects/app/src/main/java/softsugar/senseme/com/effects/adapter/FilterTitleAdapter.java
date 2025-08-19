package softsugar.senseme.com.effects.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.entity.FilterTitleItem;
import softsugar.senseme.com.effects.utils.ResUtil;
import softsugar.senseme.com.effects.view.BeautyOptionsItem;

// 滤镜一级菜单
public class FilterTitleAdapter extends BaseQuickAdapter<FilterTitleItem, BaseViewHolder> {

    public FilterTitleAdapter() {
        super(R.layout.beauty_options_item, null);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FilterTitleItem titleItem) {
        int nameStrId = ResUtil.Companion.getStringId(getContext(), titleItem.display_des_res_id);
        baseViewHolder.setText(R.id.iv_beauty_options, nameStrId);
        if (titleItem.selected) {
            baseViewHolder.setTextColor(R.id.iv_beauty_options, getContext().getResources().getColor(R.color.color_ffffff));
        } else {
            baseViewHolder.setTextColor(R.id.iv_beauty_options, getContext().getResources().getColor(R.color.color_80ffffff));
        }
        baseViewHolder.setVisible(R.id.iv_select_flag, titleItem.selected);
    }
}
