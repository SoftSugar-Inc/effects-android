package softsugar.senseme.com.effects.entity;


import softsugar.senseme.com.effects.view.widget.EffectType;

public class MakeUpTitleItem {
    public EffectType type;
    public String enum_des;
    public String enum_name;
    public String group_id;
    public int code;
    // 和头文件一一对应
    public int beauty_type;
    public boolean selected;
    public String display_des_res_id;

    // 默认0.8f
    public Float makeUpStrength = 0.8f;

    // 子项菜单选中的索引
    public int subMenusSelectedIndex = 0;

    // for overlap
    public int backupSelectedIndex;
    public Float backupStrength;
}
