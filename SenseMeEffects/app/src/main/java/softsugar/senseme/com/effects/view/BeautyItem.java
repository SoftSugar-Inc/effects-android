package softsugar.senseme.com.effects.view;

import androidx.annotation.NonNull;

import softsugar.senseme.com.effects.view.widget.EffectType;

public class BeautyItem implements Cloneable {

    public String enum_name;

    private float progress;
    private String text;
    public EffectType type;

    public int unselectedIconRes;
    public int selectedIconRes;

    public boolean start_center;
    // 是否是3D微整形
    public boolean is_3d_plastic;

    // 互斥使用，判断同样字符串去做互斥
    public String mutual;
    public String[] mutual_arr;

    public String uid;// 和头文件中对应起来
    public boolean current_use;// 做互斥使用，防止设置效果时候让后面的冲掉前面的
    // 磨皮美白用
    public boolean need_set_mode;
    public Integer mode;
    // 没有滑动条
    public boolean no_seekbar;
    // 美白2点击的时候跳转操作
    public String click_goto_enum_num;
    // 头文件中的beauty_type
    public int beauty_type;
    // 互斥用，Activity收到回调是currObj 需要arr互斥
    public int[] other_mutual_beauty_type;
    public Boolean need_open_whiten_skin_mask;
    public String beauty_asset_path;
    public float def_strength;
    public boolean skip_set_when_reset;
    public boolean skip_set_when_restart;
    public String[] assets_path_sub_menus;
    // 修改的时间戳
    public long timestamp;

    public BeautyItem() {

    }

    public BeautyItem(EffectType type, String text, int unselectedtIcon, int selectedIcon){
        this.type = type;
        this.text = text;
        this.unselectedIconRes = unselectedtIcon;
        this.selectedIconRes = selectedIcon;
        this.progress = type.getStrength();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public BeautyItem clone() throws CloneNotSupportedException {
        return (BeautyItem) super.clone();
    }

    @Override
    public String toString() {
        return "BeautyItem{" +
                "progress=" + progress +
                ", text='" + text + '\'' +
                ", unselectedtIconRes=" + unselectedIconRes +
                ", selectedtIconRes=" + selectedIconRes +
                ", start_center=" + start_center +
                ", mutual='" + mutual + '\'' +
                ", uid='" + uid + '\'' +
                ", current_use=" + current_use +
                ", need_set_mode=" + need_set_mode +
                ", smooth_mode=" + mode +
                ", no_seekbar=" + no_seekbar +
                ", click_goto_enum_num='" + click_goto_enum_num + '\'' +
                '}';
    }
}
