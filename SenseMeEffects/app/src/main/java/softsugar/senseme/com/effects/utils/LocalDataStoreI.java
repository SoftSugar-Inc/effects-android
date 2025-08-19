package softsugar.senseme.com.effects.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import softsugar.senseme.com.effects.StickerTitleEntity;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.entity.BasicEffectEntity;
import softsugar.senseme.com.effects.entity.FilterTitleItem;
import softsugar.senseme.com.effects.entity.MakeUpTitleItem;

import softsugar.senseme.com.effects.view.BeautyItem;
import softsugar.senseme.com.effects.view.BeautyOptionsItem;
import softsugar.senseme.com.effects.view.FilterItem;
import softsugar.senseme.com.effects.view.StickerItem;
import softsugar.senseme.com.effects.view.widget.EffectType;

public interface LocalDataStoreI {
    // 滤镜标题
    ArrayList<FilterTitleItem> getFilterOptionsList();
    // 美颜标题 一级菜单
    ArrayList<BasicBeautyTitleItem> getBeautyOptionsList();
    ArrayList<BeautyItem> getAllBaseBeautyItems();

    List<BasicEffectEntity> getTryOnBoyDefParams();

    List<BasicEffectEntity> getTryOnGirlDefParams();

    ArrayList<MakeUpTitleItem> getMakeupTitleItemList();

    List<StickerItem> getTryOnLipList();

    // 唇线样式
    List<StickerItem> getTryOnLipLineStyleList();

    // 美妆
    EnumMap<EffectType, Float> getMakeupStrengthMap();

    EnumMap<EffectType, Integer> getMakeupContentSelectedIndexMap();

    EnumMap<EffectType, Integer> getStyleContentSelectedIndexMap();

    EnumMap<EffectType, Float> getBasicStrengthMap();

    HashMap<EffectType, List<?>> getMakeupListsNew();

    // 贴纸标题（new）
    ArrayList<StickerTitleEntity> getStickerOptionsListNew();



    HashMap<EffectType, List<StickerItem>> getStickerContentList();

    ArrayList<BeautyOptionsItem> getMakeupOptionsList();

    HashMap<EffectType, ArrayList<FilterItem>> getFilterContentList();

}
