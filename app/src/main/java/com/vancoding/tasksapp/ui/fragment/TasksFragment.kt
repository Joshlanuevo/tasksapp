package com.vancoding.tasksapp.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.databinding.FragmentTasksBinding
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.popup.AddTasksPopupWindow

class TasksFragment : BaseFragment(R.layout.fragment_tasks) {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun requestData() {
        // Fetch data...
    }

    override fun observeCallBack() {
        // Observe LiveData...
    }
}
