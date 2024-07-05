package com.vancoding.tasksapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vancoding.tasksapp.bean.TasksBean

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: TasksBean): Long

    @Update
    suspend fun update(task: TasksBean): Int

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TasksBean>
}