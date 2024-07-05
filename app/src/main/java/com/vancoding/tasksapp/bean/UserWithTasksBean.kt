package com.vancoding.tasksapp.bean

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithTasksBean(
    @Embedded val user: UserBean,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val tasks: List<TasksBean>
)
