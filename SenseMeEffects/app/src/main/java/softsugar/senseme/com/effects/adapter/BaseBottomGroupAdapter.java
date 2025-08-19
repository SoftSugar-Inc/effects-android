package softsugar.senseme.com.effects.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.utils.ResUtil;

public class BaseBottomGroupAdapter extends BaseQuickAdapter<BasicBeautyTitleItem.Groups, BaseViewHolder> {

    public BaseBottomGroupAdapter() {
        super(R.layout.item_base_bottom_group);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, BasicBeautyTitleItem.Groups baseBottomGroupEntity) {
        int nameStrId = ResUtil.Companion.getStringId(getContext(), baseBottomGroupEntity.name_res_id);
        baseViewHolder.setText(R.id.tv_group_item, nameStrId);
        TextView tv_group_item = baseViewHolder.getView(R.id.tv_group_item);
        tv_group_item.setSelected(baseBottomGroupEntity.selected);
        if (baseBottomGroupEntity.selected) {
            tv_group_item.setBackgroundResource(R.drawable.shape_default_avatar);
        } else {
            tv_group_item.setBackground(null);
        }
    }
}
