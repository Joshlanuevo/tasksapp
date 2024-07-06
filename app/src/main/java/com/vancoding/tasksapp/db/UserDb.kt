package com.vancoding.tasksapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.bean.UserBean

@Database(entities = [UserBean::class, TasksBean::class], version = 3, exportSchema = false)
abstract class UserDb : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val tasksDao: TasksDao

    companion object {
        @Volatile
        private var INSTANCE: UserDb? = null

        fun getDatabase(context: Context): UserDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDb::class.java,
                    "user_database"
                )
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `tasks` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `user_id` INTEGER NOT NULL,
                        `title` TEXT NOT NULL,
                        `description` TEXT,
                        FOREIGN KEY(`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
                    )
                """)
                database.execSQL("""
                    ALTER TABLE users ADD COLUMN avatar_url TEXT
                """)
            }
        }
    }
}
