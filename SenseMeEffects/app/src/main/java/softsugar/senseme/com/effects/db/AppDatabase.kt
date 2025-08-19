package softsugar.senseme.com.effects.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import softsugar.senseme.com.effects.db.entity.DBBeautyEntity
import softsugar.senseme.com.effects.db.entity.DBFilterEntity
import softsugar.senseme.com.effects.db.entity.DBStyleEntity

@Database(
    entities = [DBBeautyEntity::class, DBStyleEntity::class, DBFilterEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // 每次创建AppDatabase实力都会产生打的开销，所以这里是单例
    companion object {
        private const val DT_NAME = "my_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this)
            {
                INSTANCE ?: (
                        Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DT_NAME
                        ).allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                        ).also {
                        INSTANCE = it
                    }
            }
    }

    abstract fun beautyDao(): EffectsDao
}