package softsugar.senseme.com.effects.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DBBeautyEntity {
    // 自增的主键,如果用string，就填写false
    // 和头文件中名字对应
    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String id = "";

    @ColumnInfo(name = "is_3d_plastic")
    public boolean is_3d_plastic;

    @ColumnInfo(name = "beauty_asset_path")
    public String beauty_asset_path;

    @ColumnInfo(name = "mode")
    public Integer mode;

    // 当前在用 用于磨皮1 磨皮2 磨皮3防止效果被冲掉
    @ColumnInfo(name = "current_use")
    public boolean current_use;

    // 磨皮中身体强度
    @ColumnInfo(name = "smooth_body_strength")
    public float smooth_body_strength;

    // 默认强度
    @ColumnInfo(name = "strength")
    public float strength;

    // 恢复效果时候防止后面的将前面的冲掉[app启动时候用]
    @ColumnInfo(name = "skip_set_when_restart")
    public boolean skip_set_when_restart;

    @ColumnInfo(name = "beauty_type")
    public int beauty_type;

    @ColumnInfo(name = "name")
    public String name = "";

    @ColumnInfo(name = "need_set_mode")
    public boolean need_set_mode;

    @ColumnInfo(name = "smooth_mode")
    public int smooth_mode;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @Override
    public String toString() {
        return "DB{" +
                "id='" + id + '\'' +
                ", current_use=" + current_use +
                ", skip_set_when_restart=" + skip_set_when_restart +
                ", smooth_body_strength=" + smooth_body_strength +
                ", strength=" + strength +
                ", beauty_asset_path=" + beauty_asset_path +
                ", timestamp=" + timestamp +
                ", beauty_type=" + beauty_type +
                ", name='" + name + '\'' +
                '}';
    }
}
