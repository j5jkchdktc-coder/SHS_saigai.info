package jp.shsit.shsinfo2025.room.hinan

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HinanEntity::class], version = 2, exportSchema = false)
abstract class SHS_Database : RoomDatabase() {
    abstract fun HinanDao(): HinanDao

    companion object {
        // シングルトンで使えるようにする
        @Volatile
        private var instance: SHS_Database? = null

        // データベースインスタンスは、マルチスレッドで参照される可能性があるため synchronized でアクセス
        fun buildDatabase(context: Context): SHS_Database =
            instance ?: synchronized(this) {
                Room.databaseBuilder(context, SHS_Database::class.java, "shs_database.db")
                    //.createFromAsset("database/myapp.db")
                    .build()
            }
    }

    // 必要であればコメントアウト部分を使ってマイグレーションを有効化
    /*
    companion object {
        fun buildDatabase(context: Context): SHS_Database {
            return Room.databaseBuilder(
                context,
                SHS_Database::class.java, "shs_database.db"
            ).apply {
                // addMigrations(MIGRATION_4_5)
                // fallbackToDestructiveMigration()
            }.build()
        }
    }
    */
}
