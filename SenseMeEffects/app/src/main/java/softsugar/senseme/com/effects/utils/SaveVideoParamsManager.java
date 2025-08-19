package softsugar.senseme.com.effects.utils;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import softsugar.senseme.com.effects.entity.BasicEffectEntity;
import softsugar.senseme.com.effects.view.StickerItem;
import softsugar.senseme.com.effects.view.widget.EffectType;
import softsugar.senseme.com.effects.view.widget.TryOnSeekBarType;

public class SaveVideoParamsManager {

    // Try-On 中 美颜参数
    private List<BasicEffectEntity> params;

    // 试妆素材存储
    private final HashMap<EffectType, StickerItem> tryOnMap = new HashMap<>();

//    private final HashMap<EffectType, TryOnStrength> tryOnStrength = new HashMap<>();

    // 口红质地
    private int lipStickFinish;

    private static SaveVideoParamsManager instance;

    public static SaveVideoParamsManager getInstance() {
        if (instance == null) {
            synchronized (SaveVideoParamsManager.class) {
                if (instance == null) {
                    instance = new SaveVideoParamsManager();
                }
            }
        }
        return instance;
    }

    private SaveVideoParamsManager() {

    }

    public HashMap<EffectType, StickerItem> getTryOnMap() {
        return tryOnMap;
    }

    // 添加试妆params
    public void setTryOnMap(@NonNull EffectType type, StickerItem entity) {
        if (null == entity && tryOnMap.containsKey(type)) {
            tryOnMap.remove(type);
        } else {
            tryOnMap.put(type, entity);
        }
    }

    public void setTryOnStrength(EffectType type, @NonNull TryOnSeekBarType seekType, float progress) {
//        tryOnStrength.put(type, new TryOnStrength(seekType, progress));
    }

    public List<BasicEffectEntity> getParams() {
        return params;
    }

    public void setParams(List<BasicEffectEntity> params) {
        this.params = params;
    }

    public int getLipStickFinish() {
        return lipStickFinish;
    }

    public void setLipStickFinish(int lipStickFinish) {
        this.lipStickFinish = lipStickFinish;
    }

//    public HashMap<EffectType, TryOnStrength> getTryOnStrength() {
//        return tryOnStrength;
//    }
}
