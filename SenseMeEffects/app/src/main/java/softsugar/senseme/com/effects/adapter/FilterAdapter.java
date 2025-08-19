package softsugar.senseme.com.effects.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.lang.reflect.Type;
import java.util.List;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.entity.FilterMultiLanguage;
import softsugar.senseme.com.effects.utils.GlideUtils;
import softsugar.senseme.com.effects.utils.ReadAssetsJsonFileUtils;
import softsugar.senseme.com.effects.utils.ResUtil;
import softsugar.senseme.com.effects.view.FilterItem;

/**
 * 滤镜 Adapter.
 */
public class FilterAdapter extends BaseQuickAdapter<FilterItem, BaseViewHolder> {
    private final List<FilterMultiLanguage> multiList;

    public FilterAdapter() {
        super(R.layout.filter_item);
        String json = ReadAssetsJsonFileUtils.getJson("json_data/json_filter_multi_language.json");
        Type listType = GsonUtils.getListType(FilterMultiLanguage.class);
        multiList = GsonUtils.fromJson(json, listType);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FilterItem filterItem) {
        ImageView ivImg = baseViewHolder.getView(R.id.iv_filter_image);
        GlideUtils.INSTANCE.load(getContext(), filterItem.iconPath, ivImg);

        // 多语言切换根据json映射
        String str = getStr(filterItem.name);
        if (null != str) {
            int stringId = ResUtil.Companion.getStringId(getContext(), str);
            baseViewHolder.setText(R.id.filter_text, stringId);
        } else {
            baseViewHolder.setText(R.id.filter_text, filterItem.name);
        }

        if (filterItem.selected) {
            baseViewHolder.setTextColor(R.id.filter_text, getContext().getResources().getColor(R.color.color_ffffff));
        } else {
            baseViewHolder.setTextColor(R.id.filter_text, getContext().getResources().getColor(R.color.color_a7a7a7));
        }
        baseViewHolder.setVisible(R.id.iv_bg, filterItem.selected);
    }

    private String getStr(String name) {
        for (FilterMultiLanguage item : multiList) {
            if (name.equals(item.zip_name)) {
                return item.dis_name_res_id;
            }
        }
        return null;
    }
}
