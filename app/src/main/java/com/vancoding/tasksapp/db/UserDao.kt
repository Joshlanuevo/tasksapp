package com.vancoding.tasksapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.bean.UserWithTasksBean

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserBean): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): UserBean?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserBean?

    @Query("UPDATE users SET name = :newNickname WHERE id = :userId")
    suspend fun updateNickname(newNickname: String, userId: Int)

    @Transaction
    @Query("SELECT * FROM users")
    fun getUsersWithTasks(): List<UserWithTasksBean>
}