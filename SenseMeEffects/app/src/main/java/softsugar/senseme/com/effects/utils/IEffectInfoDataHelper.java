package softsugar.senseme.com.effects.utils;

import softsugar.senseme.com.effects.view.widget.EffectType;

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/22/21 3:54 PM
 */
public interface IEffectInfoDataHelper {

    // 设置风格妆时候走这个
    int getContentSelectedIndex(EffectType type);

    // 设置基础美颜时间戳
    void setBasicEffectStamp(EffectType type);
    // 获取基础美颜时间戳
    long getBasicEffectStamp(EffectType type);

    void setStrength(EffectType type, float strength);

    void clear();
}
