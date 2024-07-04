package com.vancoding.tasksapp.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.adapter.TasksAdapter
import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.repository.TasksRepository
import com.vancoding.tasksapp.viewmodel.TasksViewModel
import com.vancoding.tasksapp.viewmodel.TasksViewModelFactory

class AddTasksDialog(private val context: Context) {

    private val tasksViewModel: TasksViewModel;

    init {
        val activity = context as FragmentActivity
        val tasksdao = UserDb.getDatabase(context).tasksDao
        val repository = TasksRepository(tasksdao)
        val factory = TasksViewModelFactory(repository)
        tasksViewModel = ViewModelProvider(activity, factory).get(TasksViewModel::class.java)
    }

    fun showDialog() {
        val dialog = Dialog(context);
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_tasks, null);
        dialog.setContentView(view);

        // Set the dialog width to 90% of the screen width
        val layoutParams = WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.window?.attributes);
        layoutParams.width = (context.resources.displayMetrics.widthPixels * 0.9).toInt();
        dialog.window?.attributes = layoutParams;

        val etTaskTitle = view.findViewById<EditText>(R.id.etTaskTitle);
        val etTaskDescription = view.findViewById<EditText>(R.id.etTaskDescription);
        val btnDone = view.findViewById<Button>(R.id.btnDone);

        btnDone.setOnClickListener {
            val taskTitle = etTaskTitle.text.toString().trim();
            val taskDescription = etTaskDescription.text.toString().trim();

            if (taskTitle.isEmpty()) {
                etTaskTitle.error = "Task title is required";
            } else {
                val task = TasksBean(title = taskTitle, description = taskDescription);
                tasksViewModel.insert(task);
                Toast.makeText(context, "Task Added: $taskTitle - $taskDescription", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
        dialog.show();
    }
}