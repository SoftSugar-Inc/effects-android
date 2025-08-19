package softsugar.senseme.com.effects.view;

import android.util.Log;

import java.util.List;

import softsugar.senseme.com.effects.adapter.FilterAdapter;
import softsugar.senseme.com.effects.view.widget.EffectType;

/**
 * 基础美颜一级菜单
 */
public class BeautyOptionsItem {
    public String name;
    public EffectType type;

    // 对应EffectType中的code
    public int code;
    public String uid;
    public String enum_des;
    public String enum_name;
    public String display_des_res_id;
    // 用于清零时候，根据这个找到对应的adapter，遍历Item清零
    public String[] third_adapter_enum;
    // 用于点清零时候清理掉磨皮中的
    public boolean has_base_skin_smooth;
    // 微整形分组
    public List<Groups> groups;
    // 拉取素材用
    public String group_id;

    // 当前子菜单选中的选项
    private String curr_sub_menu_select_uid;

    public String sub_selected_adapter_enum;

    public String[] assets_path_sub_menus;


    // 滤镜当前选中的位置
    public int indexSelectedFilter = -1;

    // 当前子项菜单选中位置
    public int subMenuSelectedPosition = -1;

    // 一级菜单选中
    public boolean selected;

    // 滤镜二级菜单Adapter
    public transient FilterAdapter filterAdapter = null;

    public float filterStrength;
    public Float backupFilterStrength;
    public Integer backupSelectedIndex;

    public String getCurr_sub_menu_select_uid() {
        return curr_sub_menu_select_uid;
    }

    public void setCurr_sub_menu_select_uid(String curr_sub_menu_select_uid) {
        this.curr_sub_menu_select_uid = curr_sub_menu_select_uid;
    }

    public static class Groups {
        public String name;
        public String name_res_id;
        public boolean selected;
        public String scroll_to_position_uid;
    }

    public BeautyOptionsItem(String name) {
        this.name = name;
    }

    public BeautyOptionsItem(EffectType type, String name) {
        this.name = name;
        this.type = type;
    }

}
