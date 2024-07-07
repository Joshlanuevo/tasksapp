package com.vancoding.tasksapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.adapter.UserWithTasksAdapter
import com.vancoding.tasksapp.databinding.FragmentDiscoverBinding
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.viewmodel.DiscoverViewModel

class DiscoverFragment: BaseFragment(R.layout.fragment_discover) {
    private lateinit var mViewModel: DiscoverViewModel;
    private lateinit var mAapter: UserWithTasksAdapter;

    private var _binding: FragmentDiscoverBinding? = null;
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        return binding.root;
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

        mAapter = UserWithTasksAdapter();
        binding.listUsers.adapter = mAapter;
        binding.listUsers.layoutManager = GridLayoutManager(requireContext(), 2)

        mViewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java);

        mViewModel.usersWithTasks.observe(viewLifecycleOwner) { userWithTasks ->
            userWithTasks?.let {
                mAapter.setUsers(it);
            }
        }

        mViewModel.getUserWithTasks()
    }

    override fun requestData() {
        // Fetch data...
    }

    override fun observeCallBack() {
        // Observe LiveData...
    }
}