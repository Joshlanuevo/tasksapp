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
import androidx.lifecycle.lifecycleScope
import coil.load
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.adapter.TasksAdapter
import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.databinding.FragmentTasksBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.repository.TasksRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.viewmodel.TasksViewModel
import com.vancoding.tasksapp.viewmodel.TasksViewModelFactory
import kotlinx.coroutines.launch

class TasksFragment : BaseFragment(R.layout.fragment_tasks) {
    private var _binding: FragmentTasksBinding? = null;
    private val binding get() = _binding!!;
    private lateinit var tasksAdapter: TasksAdapter;
    private val tasksViewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(TasksRepository(UserDb.getDatabase(requireContext().applicationContext).tasksDao))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        requestData();
        observeCallBack();

        binding.refreshHome.setOnRefreshListener { requestData() }
    }

    override fun onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }

    override fun initView() {
        binding.ivLogo.load(R.mipmap.ic_launcher)

        binding.ivAdd.setOnClickListener {
            showAddTaskDialog();
        }

        tasksAdapter = TasksAdapter(
            onEditClick = { task ->
                showUpdateTaskDialog(task);
            },
            onDeleteClick = { task ->
                showDeleteTaskDialog(task);
            }
        )
        binding.listTasks.adapter = tasksAdapter;
    }

    override fun requestData() {
        val currentUserId = PreferencesManager.getUserId(requireContext())
        lifecycleScope.launch { // Use lifecycleScope.launch to launch coroutine
            if (currentUserId != null) {
                tasksViewModel.getTasksForUser(currentUserId)
            }
        }
    }


    override fun observeCallBack() {
        tasksViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let {
                tasksAdapter.submitList(it);
                binding.refreshHome.isRefreshing = false // Stop the refreshing animation
            }
        })
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_tasks, null);
        val etTaskTitle = dialogView.findViewById<EditText>(R.id.etTaskTitle);
        val etTaskDescription = dialogView.findViewById<EditText>(R.id.etTaskDescription);
        val btnAddTask = dialogView.findViewById<Button>(R.id.btnDone);

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnAddTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()
            val currentUserId = PreferencesManager.getUserId(requireContext())

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val task =
                    currentUserId?.let { it1 -> TasksBean(userId = it1, title = title, description = description) }
                if (task != null) {
                    tasksViewModel.insert(task, requireContext())
                }
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter both title and description", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun showUpdateTaskDialog(task: TasksBean) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_tasks, null);
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialog_title);
        val etTaskTitle = dialogView.findViewById<EditText>(R.id.etTaskTitle);
        val etTaskDescription = dialogView.findViewById<EditText>(R.id.etTaskDescription);
        val btnUpdateTask = dialogView.findViewById<Button>(R.id.btnDone);

        dialogTitle.text = "Update Task";
        etTaskTitle.setText(task.title);
        etTaskDescription.setText(task.description);
        btnUpdateTask.text = "Update";

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnUpdateTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim();
            val description = etTaskDescription.text.toString().trim();

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val updatedTask = task.copy(title = title, description = description);
                tasksViewModel.update(updatedTask, requireContext());
                dialog.dismiss();
            } else {
                Toast.makeText(requireContext(), "Please enter both title and description", Toast.LENGTH_SHORT).show();
            }
        }

        dialog.show()
    }

    private fun showDeleteTaskDialog(task: TasksBean) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete the task? If you delete it, you will never be able to recover it.")
            .setPositiveButton("Delete") { _, _ ->
                tasksViewModel.delete(task, requireContext());
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show();
    }
}
