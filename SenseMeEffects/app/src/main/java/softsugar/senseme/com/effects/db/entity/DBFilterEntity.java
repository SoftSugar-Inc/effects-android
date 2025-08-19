package softsugar.senseme.com.effects.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 存储风格妆信息
@Entity
public class DBFilterEntity {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String id = "";

    // 最后修改的时间
    @ColumnInfo(name = "time")
    public long time;

    // 选中的位置
    @ColumnInfo(name = "selectedPosition")
    public int selectedPosition;

    @ColumnInfo(name = "groupEnumName")
    public String groupEnumName;

    // SD卡的路径
    @ColumnInfo(name = "filterPath")
    public String filterPath;

    // 滤镜强度
    @ColumnInfo(name = "filterStrength")
    public float filterStrength;

    @Override
    public String toString() {
        return "DBFilterEntity{" +
                "id='" + id + '\'' +
                ", time=" + time +
                ", selectedPosition=" + selectedPosition +
                ", groupEnumName='" + groupEnumName + '\'' +
                ", filterPath='" + filterPath + '\'' +
                ", filterStrength=" + filterStrength +
                '}';
    }
}
