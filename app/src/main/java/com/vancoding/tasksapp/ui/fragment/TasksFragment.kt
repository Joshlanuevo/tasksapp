package com.vancoding.tasksapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.adapter.TasksAdapter
import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.databinding.FragmentTasksBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.repository.TasksRepository
import com.vancoding.tasksapp.viewmodel.TasksViewModel
import com.vancoding.tasksapp.viewmodel.TasksViewModelFactory

class TasksFragment : BaseFragment(R.layout.fragment_tasks) {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasksAdapter: TasksAdapter
    private val tasksViewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(TasksRepository(UserDb.getDatabase(requireContext()).tasksDao))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        requestData()
        observeCallBack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initView() {
        binding.ivLogo.load(R.mipmap.ic_launcher)

        binding.ivAdd.setOnClickListener {
            showAddTaskDialog()
        }

        tasksAdapter = TasksAdapter { task ->
            showUpdateTaskDialog(task)
        }
        binding.listTasks.adapter = tasksAdapter
    }

    override fun requestData() {
        tasksViewModel.loadTasks()
    }

    override fun observeCallBack() {
        tasksViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let {
                tasksAdapter.submitList(it)
            }
        })
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_tasks, null)
        val etTaskTitle = dialogView.findViewById<EditText>(R.id.etTaskTitle)
        val etTaskDescription = dialogView.findViewById<EditText>(R.id.etTaskDescription)
        val btnAddTask = dialogView.findViewById<Button>(R.id.btnDone)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnAddTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val task = TasksBean(title = title, description = description)
                tasksViewModel.insert(task)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter both title and description", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun showUpdateTaskDialog(task: TasksBean) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_tasks, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
        val etTaskTitle = dialogView.findViewById<EditText>(R.id.etTaskTitle)
        val etTaskDescription = dialogView.findViewById<EditText>(R.id.etTaskDescription)
        val btnUpdateTask = dialogView.findViewById<Button>(R.id.btnDone)

        dialogTitle.text = "Update Task"
        etTaskTitle.setText(task.title)
        etTaskDescription.setText(task.description)
        btnUpdateTask.text = "Update"

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnUpdateTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val updatedTask = task.copy(title = title, description = description)
                tasksViewModel.update(updatedTask)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter both title and description", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
