package softsugar.senseme.com.effects.view;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.blankj.utilcode.util.LogUtils;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import softsugar.senseme.com.effects.view.widget.LinkageEntity;

/**
 * Created by softsugar on 17-6-8.
 */

public class StickerItem implements LinkageEntity {
    public Boolean selected = false;
    public String name;
    public int id;
    public Bitmap icon;
    public String iconUrl;
    public String path;
    public String pkgUrl;
    public int lipFinishType;// 口红质地
    public StickerState state = StickerState.NORMAL_STATE;//0 未下载状态，也是默认状态，1，正在下载状态,2,下载完毕状态

    public long orderTimeStamp;

    public StickerItem(String iconUrl, StickerState state) {
        this.iconUrl = iconUrl;
        this.state = state;
    }

    public StickerItem(Boolean selected, String name, String iconUrl) {
        this.selected = selected;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public StickerItem(int lipFinishType, Boolean selected, String name, String iconUrl) {
        this.lipFinishType = lipFinishType;
        this.selected = selected;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public StickerItem(String name, Bitmap icon, String path) {
        this.name = name;
        this.icon = icon;
        this.path = path;
        if (TextUtils.isEmpty(this.path)) {
            state = StickerState.NORMAL_STATE;
        } else {
            state = StickerState.DONE_STATE;
        }
    }

    public StickerItem(){}

    public StickerItem(Bitmap bitmap) {
        this.icon = bitmap;
        state = StickerState.DONE_STATE;
    }

    public void recycle() {
        if (icon != null && !icon.isRecycled()) {
            icon.recycle();
            icon = null;
        }
    }

    @Override
    public String toString() {
        return "StickerItem{" +
                "name='" + name + '\'' +
                "id='" + id + '\'' +
                ", icon=" + icon +
                ", path='" + path + '\'' +
                ", state=" + state +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }

    @NotNull
    @Override
    public StickerState getState() {
        return state;
    }

    @Override
    public void setState(@NotNull StickerState state) {
        this.state = state;
    }

    @Override
    public void setPath(@NotNull String path) {
        this.path = path;
        if (TextUtils.isEmpty(this.path)) {
            state = StickerState.NORMAL_STATE;
        } else {
            state = StickerState.DONE_STATE;
        }
    }

    @NonNull
    @Override
    public String getPkgUrl() {
        return pkgUrl;
    }
}
