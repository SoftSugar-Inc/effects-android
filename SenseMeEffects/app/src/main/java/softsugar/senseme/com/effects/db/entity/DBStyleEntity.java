package softsugar.senseme.com.effects.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 存储风格妆信息
@Entity
public class DBStyleEntity {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String id = "";

    @ColumnInfo(name = "path")
    public String path;

    @ColumnInfo(name = "uid")
    public String uid;

    @ColumnInfo(name = "enumName")
    public String enumName;

    @ColumnInfo(name = "time")
    public long time;

    @ColumnInfo(name = "selectedPosition")
    public int selectedPosition;

    // 滤镜强度
    @ColumnInfo(name = "filterStrength")
    public float filterStrength;

    // 美妆强度
    @ColumnInfo(name = "makeupStrength")
    public float makeupStrength;

    @Override
    public String toString() {
        return "DBStyleEntity{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", enumName='" + enumName + '\'' +
                ", selectedPosition=" + selectedPosition +
                ", filterStrength=" + filterStrength +
                ", makeupStrength=" + makeupStrength +
                '}';
    }
}
