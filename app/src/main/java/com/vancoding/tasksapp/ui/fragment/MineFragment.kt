package com.vancoding.tasksapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.databinding.FragmentMineBinding
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.ui.SetActivity

class MineFragment: BaseFragment(R.layout.fragment_mine) {
    private  var _binding: FragmentMineBinding? = null;
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false);
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
        binding.tvSet.setOnClickListener{
            startActivity(Intent(activity, SetActivity::class.java))
        }
    }

    override fun requestData() {
        // Fetch data...
    }

    override fun observeCallBack() {
        // Observe LiveData...
    }
}