package softsugar.senseme.com.effects.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.utils.GlideUtils;
import softsugar.senseme.com.effects.utils.SpUtils;
import softsugar.senseme.com.effects.view.StickerItem;

public class StickerContentAdapter extends BaseQuickAdapter<StickerItem, BaseViewHolder> {

    public StickerContentAdapter() {
        super(R.layout.sticker_item, null);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, StickerItem entity) {
        switch (entity.state) {
            case NORMAL_STATE:
                baseViewHolder.setVisible(R.id.pb_loading, false);
                baseViewHolder.setVisible(R.id.normalState, true);
                break;
            case LOADING_STATE:
                baseViewHolder.setVisible(R.id.normalState, false);
                baseViewHolder.setVisible(R.id.pb_loading, true);
                break;
            case DONE_STATE:
                baseViewHolder.setVisible(R.id.normalState, false);
                baseViewHolder.setVisible(R.id.pb_loading, false);
                break;
        }
        ImageView icon = baseViewHolder.getView(R.id.icon);
        GlideUtils.INSTANCE.load1(entity.iconUrl, icon);
        baseViewHolder.itemView.setSelected(entity.selected);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedPosition(int position) {
        for(StickerItem item : this.getData()) {
            item.selected = false;
        }
        if (position >= 0) {
            this.getData().get(position).selected = true;
        }
        this.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<StickerItem> data) {
        this.setNewInstance(data);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addHeadData(StickerItem item) {
        if (!hasItem(item)) {
            this.getData().add(0, item);
            SpUtils.setParam(item.path + "time", System.currentTimeMillis());
            SpUtils.setParam(item.path, item.iconUrl);
            notifyDataSetChanged();
        }
    }

    private boolean hasItem(StickerItem item) {
        for (StickerItem i : getData()) {
            if (i.path.contains(item.name))
                return true;
        }
        return false;
    }
}
