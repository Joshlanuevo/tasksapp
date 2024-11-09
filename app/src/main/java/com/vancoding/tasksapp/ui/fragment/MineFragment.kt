package com.vancoding.tasksapp.ui.fragment

import MineViewModel
import android.app.Activity
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
import com.vancoding.tasksapp.ui.ModifyNameActivity
import com.vancoding.tasksapp.ui.PersonalInfoActivity
import com.vancoding.tasksapp.ui.SetActivity
import com.vancoding.tasksapp.viewmodel.MineViewModelFactory

class MineFragment : BaseFragment(R.layout.fragment_mine) {
    private var _binding: FragmentMineBinding? = null;
    private lateinit var mViewModel: MineViewModel;
    private val binding get() = _binding!!;

    companion object {
        private const val PERSONAL_INFO_REQUEST_CODE = 1;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false);

        // Initialize UserRepository
        val userRepository = UserRepository(UserDb.getDatabase(requireContext()).userDao);

        // Initialize MineViewModel with UserRepository
        mViewModel = ViewModelProvider(this, MineViewModelFactory(requireActivity().application, userRepository)).get(MineViewModel::class.java);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeCallBack();
        mViewModel.getUserInfo();
    }

    override fun onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }

    override fun initView() {
        binding.tvSet.setOnClickListener{
            startActivity(Intent(activity, SetActivity::class.java));
        }

        binding.consInfo.setOnClickListener {
            val intent = Intent(activity, PersonalInfoActivity::class.java).apply {
                putExtra("nickname", mViewModel.userInfo.value?.nickname);
                putExtra("username", mViewModel.userInfo.value?.username);
            }
            startActivityForResult(intent, PERSONAL_INFO_REQUEST_CODE);
        }
    }

    override fun requestData() {
        // Fetch data...
    }

    override fun observeCallBack() {
        mViewModel.userInfo.observe(viewLifecycleOwner, Observer { userInfo ->
            binding.apply {
                tvNickName.text = userInfo.nickname;
                tvUsername.text = userInfo.username;
                layoutHeader.showUrl(userInfo.avatar);
            }
        })

        // Ensuring AvatarImageView updates when userInfo.avatar changes
        binding.layoutHeader.setRoundRadius(100000F) // Making the avatar circle
        mViewModel.userInfo.observe(viewLifecycleOwner, Observer { userInfo ->
            binding.layoutHeader.showUrl(userInfo.avatar)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERSONAL_INFO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mViewModel.getUserInfo();
        }
    }
}
