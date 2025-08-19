package softsugar.senseme.com.effects.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.StickerTitleEntity;
import softsugar.senseme.com.effects.utils.ResUtil;
import softsugar.senseme.com.effects.view.BeautyOptionsItem;

// 贴纸一级菜单
public class StickerTitleAdapter extends BaseQuickAdapter<StickerTitleEntity, BaseViewHolder> {

    public StickerTitleAdapter() {
        super(R.layout.beauty_options_item, null);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, StickerTitleEntity titleItem) {
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
    public void setSelectedPosition(int position) {
        for(StickerTitleEntity item : getData()) {
            item.selected = false;
        }
        this.getData().get(position).selected = true;
        this.notifyDataSetChanged();
    }

}
