package softsugar.senseme.com.effects.display;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.view.BeautyItem;
import softsugar.senseme.com.effects.view.widget.LinkageEntity;

public interface BaseDisplayI {

    /**
     * 设置滤镜
     */
    void setFilter(LinkageEntity contentEntity, float strength);

    /**
     * 清空滤镜效果
     */
    void clearFilter();

    /**
     * 清空所有贴纸or通用物体追踪
     */
    void clearAllSticker();

    /* *********** 贴纸相关 end ************/

    /* *********** 基础美颜相关 start ************/
    /**
     * 重置基础美颜
     */
    void resetBaseEffect(@NotNull BasicBeautyTitleItem currentTitleData, @NotNull ArrayList<BeautyItem> dataList);

    /**
     * 设置基础美颜强度
     */
    void setBasicBeauty(BasicBeautyTitleItem titleData, @NotNull BeautyItem contentEntity, float progress);

}
