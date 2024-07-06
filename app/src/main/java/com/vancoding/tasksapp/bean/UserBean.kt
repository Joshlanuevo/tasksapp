package com.vancoding.tasksapp.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserBean(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "name")
    val nickname: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null
)
