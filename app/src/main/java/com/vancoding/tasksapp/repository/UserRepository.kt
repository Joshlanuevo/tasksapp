package com.vancoding.tasksapp.repository

import androidx.lifecycle.LiveData
import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.bean.UserWithTasksBean
import com.vancoding.tasksapp.db.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: UserBean): Long {
        return userDao.insert(user);
    }

    suspend fun getUser(username: String, password: String): UserBean? {
        return userDao.getUser(username, password)
    }

    suspend fun updateUsername(newUsername: String, userId: Int) {
        userDao.updateUsername(newUsername, userId)
    }

    suspend fun changePassword(userId: Int, oldPassword: String, newPassword: String): Boolean {
        val user = userDao.getUserById(userId)
        return if (user != null && user.password == oldPassword) {
            userDao.updatePassword(userId.toString(), newPassword)
            true
        } else {
            false
        }
    }

    suspend fun updateNickname(newNickname: String, userId: Int) {
        userDao.updateNickname(newNickname, userId)
    }

    suspend fun getUserById(userId: Int): UserBean? {
        return userDao.getUserById(userId)
    }

    suspend fun checkUsernameExists(username: String): Boolean {
        return userDao.isUsernameExists(username)
    }
    suspend fun updateAvatar(userId: Int, avatarUrl: String) {
        userDao.updateAvatar(userId, avatarUrl)
    }

    fun getUsersWithTasks(): LiveData<List<UserWithTasksBean>> {
        return userDao.getUsersWithTasks()
    }
}