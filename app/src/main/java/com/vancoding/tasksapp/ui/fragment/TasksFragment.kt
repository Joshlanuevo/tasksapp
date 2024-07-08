package com.vancoding.tasksapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.adapter.TasksAdapter
import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.databinding.FragmentTasksBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.repository.TasksRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.util.ToastUtils
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

    private var originalTasksList: List<TasksBean> = emptyList()

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

        binding.tvSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter the list based on search query
                tasksAdapter.submitList(filterTasks(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }
        })

        binding.tvSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                // Perform search or filter tasks when "Enter" is pressed
                val query = binding.tvSearch.text.toString().trim()
                tasksAdapter.submitList(filterTasks(query))
                // Hide the keyboard
                hideKeyboard()
                true // Consume the action
            } else {
                false // Continue with default behavior
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }

    override fun initView() {
        binding.ivLogo.load(R.mipmap.ic_launcher) {
            transformations(CircleCropTransformation())
        }

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
                originalTasksList = it

                if (it.isEmpty()) {
                    binding.tvNoTasks.visibility = View.VISIBLE;
                    binding.listTasks.visibility = View.GONE;
                } else {
                    binding.tvNoTasks.visibility = View.GONE;
                    binding.listTasks.visibility = View.VISIBLE;
                }
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
                ToastUtils.showToast(requireContext(), "Please enter both title and description", it);
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
                ToastUtils.showToast(requireContext(), "Please enter both title and description", it);
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

    private fun filterTasks(query: String): List<TasksBean> {
        return originalTasksList.filter {
            it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.tvSearch.windowToken, 0)
    }
}
