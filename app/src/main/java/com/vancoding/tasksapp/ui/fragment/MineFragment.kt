package com.vancoding.tasksapp.ui.fragment

import MineViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.databinding.FragmentMineBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.ui.SetActivity
import com.vancoding.tasksapp.viewmodel.MineViewModelFactory

class MineFragment : BaseFragment(R.layout.fragment_mine) {
    private var _binding: FragmentMineBinding? = null
    private lateinit var mViewModel: MineViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false)

        // Initialize UserRepository
        val userRepository = UserRepository(UserDb.getDatabase(requireContext()).userDao)

        // Initialize MineViewModel with UserRepository
        mViewModel = ViewModelProvider(this, MineViewModelFactory(requireActivity().application, userRepository)).get(MineViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeCallBack()
        mViewModel.getUserInfo()
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
        mViewModel.userInfo.observe(viewLifecycleOwner, Observer { userInfo ->
            binding.tvNickName.text = userInfo.nickname
            binding.tvUsername.text = userInfo.username
        })
    }
}
