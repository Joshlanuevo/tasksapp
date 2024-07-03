package com.vancoding.tasksapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vancoding.tasksapp.bean.UserBean

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserBean): Long;

    @Query("SELECT * FROM users WHERE  username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): UserBean?
}