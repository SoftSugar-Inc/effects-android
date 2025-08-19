package softsugar.senseme.com.effects.entity;

public class BeautyItemNew {

    // 唯一标识
    public String uid;

    // 中文
    public String display_des_res_id;
    // seek_bar从中间开始滑动
    public boolean start_center;
    public boolean no_seekbar;
    public boolean is_3d_plastic;

    // 赋值给EffectType中字段
    public String enum_name;
    public float def_strength;
    public String enum_des;//
    public int st_beauty_type; // 对应.h中字段映射
    // 磨皮美白用
    public boolean need_set_mode;
    public String[] mutual_arr;
    public Integer mode;
    // 【磨皮2】【磨皮3】【磨皮4】三种磨皮功能内左下角新增【脸部/全身】切换开关：
    public boolean face_body_btn_show;

    // 美白用，是否开启皮肤分割【美白1和美白2不需要皮肤分割，美白3美白4需要皮肤分割】
    public boolean need_skin_mask;

    // 做互斥用 比如磨皮互斥 传入"mutual_smooth"，内部遍历所有相同一组的进行互斥操作
    public String mutual;
    public boolean current_use;// 恢复效果时候防止后面的冲掉前面的
    public int[] other_mutual_beauty_type;

    // 常规状态图片
    public String select_icon_nor_res;
    // 美白开启皮肤分割
    public Boolean need_open_whiten_skin_mask;

    // 按下图片
    public String select_icon_press_res;
    public String click_goto_enum_num;
    // 美白素材包
    public String beauty_asset_path;
    public boolean skip_set_when_reset;

}
