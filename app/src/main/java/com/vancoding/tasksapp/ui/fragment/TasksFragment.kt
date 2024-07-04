package com.vancoding.tasksapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.adapter.TasksAdapter
import com.vancoding.tasksapp.databinding.FragmentTasksBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.popup.AddTasksPopupWindow
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
            val popupWindow = AddTasksPopupWindow(requireContext());
            popupWindow.showPopup(binding.ivAdd, 200, 4, Gravity.BOTTOM)
        }

        tasksAdapter = TasksAdapter()
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
}
