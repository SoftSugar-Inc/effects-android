package softsugar.senseme.com.effects.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import softsugar.senseme.com.effects.db.entity.DBBeautyEntity;
import softsugar.senseme.com.effects.db.entity.DBFilterEntity;
import softsugar.senseme.com.effects.db.entity.DBStyleEntity;

@Dao
public interface EffectsDao {

    // 注意：不能使用ArrayList，必须使用List
    @Query("SELECT * FROM dbbeautyentity")
    List<DBBeautyEntity> getAllBeautyItem();

    @Query("SELECT * FROM dbfilterentity")
    DBFilterEntity getFilter();

    @Query("DELETE FROM dbfilterentity")
    void clearFilter();

    // 表示插入的时候，如果有该数据的情况下会直接替换（对象中要包含主键，和要修改的值即可）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBeauty(DBBeautyEntity beauty);

    // 表示插入的时候，如果有该数据的情况下会直接替换（对象中要包含主键，和要修改的值即可）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFilter(DBFilterEntity filter);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStyle(DBStyleEntity style);


    @Query("SELECT * FROM dbstyleentity")
    DBStyleEntity getStyle();
}
