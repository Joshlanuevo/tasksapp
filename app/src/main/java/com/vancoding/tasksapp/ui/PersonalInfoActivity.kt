package com.vancoding.tasksapp.ui

import MineViewModel
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vancoding.tasksapp.databinding.ActivityPersonalInfoBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.viewmodel.MineViewModelFactory

class PersonalInfoActivity: BaseActivity() {
    private lateinit var bindView: ActivityPersonalInfoBinding;
    private lateinit var mViewModel: MineViewModel;

    companion object {
        private const val MODIFY_NAME_REQUEST_CODE = 1;
    }

    override fun initView() {
        bindView = ActivityPersonalInfoBinding.inflate(layoutInflater);
        setContentView(bindView.root);

        val userRepository = UserRepository(UserDb.getDatabase(applicationContext).userDao)
        mViewModel = ViewModelProvider(this, MineViewModelFactory(application, userRepository)).get(MineViewModel::class.java)

        val nickname = intent.getStringExtra("nickname");
        val username = intent.getStringExtra("username");

        bindView.tvNickName.text = nickname;
        bindView.tvUsername.text = username;

        mViewModel.userInfo.observe(this, Observer { userInfo ->
            bindView.tvNickName.text = userInfo.nickname;
        })

        bindView.ivBack.setOnClickListener {
            finish();
        }

        bindView.cons1.setOnClickListener {
            val intent = Intent(this, ModifyNameActivity::class.java);
            intent.putExtra("nickname", mViewModel.userInfo.value?.nickname);
            startActivityForResult(intent, MODIFY_NAME_REQUEST_CODE);
        }

        mViewModel.getUserInfo();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MODIFY_NAME_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val updateNickName = data?.getStringExtra("nickname");
            if (!updateNickName.isNullOrEmpty()) {
                mViewModel.getUserInfo();
                setResult(Activity.RESULT_OK, data);
            }
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {}
}