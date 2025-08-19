package softsugar.senseme.com.effects.helper;

import com.blankj.utilcode.util.LogUtils;
import com.softsugar.stmobile.params.STEffectBeautyParams;
import com.softsugar.stmobile.params.STEffectBeautyType;

import softsugar.senseme.com.effects.display.BaseDisplay;
import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.EffectInfoDataHelper;
import softsugar.senseme.com.effects.view.widget.EffectType;

/**
 * 美白 and 磨皮相关逻辑
 * <p>
 * 调动setBeautyStrength之前需要做如下设置
 * <p>
 * 一、美白逻辑
 * 美白1：设置mode为0
 * 美白2：加载美白对应的素材，关闭皮肤分割
 * 美白3：设置mode为2，开启皮肤分割
 * 美白4：加载素材包，开启皮肤风格
 * <p>
 * 二、磨皮逻辑
 * 磨皮1：设置mode为1
 * 磨皮2：设置mode为2
 */
public class WhiteningUtils {

    public boolean firstWhiten2 = true;

    // 美白2已加载的美白素材
    private String loadedWhiten = "";

    // 设置美白2
    public void setWhitening2(BaseDisplay display, EffectType type) {
        EffectInfoDataHelper.getInstance().setWhiteningType(Constants.SP_WHITEN_2);
        String whitenPath = "";
        // 自然美白
        if (type == EffectType.TYPE_BASIC_2_NATURE) {
            whitenPath = "whiten_assets/whiten_gif.zip";
        }
        // 粉嫩美白
        else if (type == EffectType.TYPE_BASIC_2_PINK) {
            whitenPath = "whiten_assets/whiten_pink.zip";
        }
        // 美黑美白
        else if (type == EffectType.TYPE_BASIC_2_BLACK) {
            whitenPath = "whiten_assets/whiten_black.zip";
        }
        if (!loadedWhiten.equals(whitenPath)) {
            long time = System.currentTimeMillis();
            display.setWhitenFromAssetsFileSync(whitenPath);
            LogUtils.i("setWhitenFromAssetsFile:" + whitenPath);
            loadedWhiten = whitenPath;
        }
        display.setBeautyParam(STEffectBeautyParams.ENABLE_WHITEN_SKIN_MASK, 0);
    }

}
