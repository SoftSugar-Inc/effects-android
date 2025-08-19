package softsugar.senseme.com.effects;

import softsugar.senseme.com.effects.adapter.StickerAdapter;
import softsugar.senseme.com.effects.adapter.StickerContentAdapter;
import softsugar.senseme.com.effects.view.widget.EffectType;

/**
 * 贴纸一级菜单
 */
public class StickerTitleEntity {

    public String uid;
    public String enum_name;
    public String enum_des;
    public String display_des_res_id;
    // 拉取素材用
    public String group_id;
    public EffectType type;

    // 一级菜单选中
    public boolean selected;

    public int subMenuSelectedPosition = -1;

    // 如果有值，会自动加载本地的文件夹贴纸数据
    /*
      {
        "enum_name": "TYPE_STICKER_SYNC",
        "enum_des": "测试",
        "asset_path": "local_sticker",
        "display_des_res_id": "str_test_tab"
      }
     */
    public String asset_path;

    public transient StickerContentAdapter stickerAdapter;
}
