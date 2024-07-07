package com.vancoding.tasksapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.bean.UserWithTasksBean
import com.vancoding.tasksapp.databinding.ItemUserWithTasksBinding

class UserWithTasksAdapter : RecyclerView.Adapter<UserWithTasksAdapter.UserWithTasksViewHolder>() {

    private var userList: List<UserWithTasksBean> = listOf()

    class UserWithTasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvNickname: TextView = view.findViewById(R.id.tvNickname)
        val tvTasks: TextView = view.findViewById(R.id.tvTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserWithTasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_with_tasks, parent, false)
        return UserWithTasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserWithTasksViewHolder, position: Int) {
        val userWithTasks = userList[position]
        holder.tvUsername.text = userWithTasks.user.username
        holder.tvNickname.text = userWithTasks.user.nickname

        // Load avatar with placeholder
        val avatarUrl = userWithTasks.user.avatarUrl
        holder.ivAvatar.load(avatarUrl) {
            placeholder(R.drawable.ic_mine_header) // Replace with your placeholder drawable
            error(R.drawable.ic_mine_header) // In case of an error, use the placeholder
            transformations(CircleCropTransformation())
        }

        // Populate tasks
        val tasksCount = userWithTasks.tasks.size
        holder.tvTasks.text = "Tasks: $tasksCount"
    }

    override fun getItemCount() = userList.size

    fun setUsers(users: List<UserWithTasksBean>) {
        userList = users
        notifyDataSetChanged()
    }
}

