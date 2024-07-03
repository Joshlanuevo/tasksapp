package com.vancoding.tasksapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vancoding.tasksapp.bean.UserBean

@Database(entities = [UserBean::class], version = 1, exportSchema = false)
abstract class UserDb: RoomDatabase() {

    abstract val userDao: UserDao;

    companion object {
        @Volatile
        private var INSTANCE: UserDb? = null;

        fun getDatabase(context: Context): UserDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDb::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}