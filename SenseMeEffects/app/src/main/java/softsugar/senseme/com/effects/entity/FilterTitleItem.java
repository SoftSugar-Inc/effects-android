package softsugar.senseme.com.effects.entity;

import softsugar.senseme.com.effects.adapter.FilterAdapter;
import softsugar.senseme.com.effects.view.widget.EffectType;

/**
 * 基础美颜一级菜单
 */
public class FilterTitleItem {
    public String name;
    public EffectType type;
    public String uid;
    public String enum_des;
    public String enum_name;
    public String display_des_res_id;

    // 滤镜当前选中的位置
    public int sub_menu_selected_position = -1;

    // 一级菜单选中
    public boolean selected;

    // 滤镜二级菜单Adapter
    public transient FilterAdapter filterAdapter = null;

    public float filterStrength;
    public Float backupFilterStrength;
    public Integer backupSelectedIndex;
}
