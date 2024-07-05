package com.vancoding.tasksapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.databinding.ItemTaskBinding

class TasksAdapter(private val onEditClick: (TasksBean) -> Unit) : ListAdapter<TasksBean, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, onEditClick);
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position);
        holder.bind(task);
    }

    class TaskViewHolder(private val binding: ItemTaskBinding, private val onEditClick: (TasksBean) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TasksBean) {
            binding.tvTaskTitle.text = task.title;
            binding.tvTaskDescription.text = task.description;
            binding.ivEditTask.setOnClickListener { onEditClick(task) }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<TasksBean>() {
        override fun areItemsTheSame(oldItem: TasksBean, newItem: TasksBean): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: TasksBean, newItem: TasksBean): Boolean {
           return oldItem == newItem;
        }
    }
}