package com.vancoding.tasksapp.ui

import MineViewModel
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.vancoding.tasksapp.databinding.ActivityModifyNameBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.util.ToastUtils
import com.vancoding.tasksapp.viewmodel.MineViewModelFactory

class ModifyNameActivity : BaseActivity() {
    private lateinit var bindView: ActivityModifyNameBinding;
    private lateinit var mViewModel: MineViewModel;

    override fun initView() {
        bindView = ActivityModifyNameBinding.inflate(layoutInflater);
        setContentView(bindView.root);

        val userRepository = UserRepository(UserDb.getDatabase(applicationContext).userDao)
        mViewModel = ViewModelProvider(this, MineViewModelFactory(application, userRepository)).get(MineViewModel::class.java);

        val currentNickname = intent.getStringExtra("nickname")
        bindView.etName.setText(currentNickname);

        bindView.ivClear.setOnClickListener {
            bindView.etName.setText("");
        }

        bindView.toolbar.run {
            viewBack.setOnClickListener {
                finish()
            }
            tvTitle.text = "Edit"
        }

        bindView.btnSave.setOnClickListener {
            val newNickname = bindView.etName.text.toString().trim();
            if (newNickname.isNotEmpty()) {
                val userId = PreferencesManager.getUserId(this);
                if (userId != null) {
                    mViewModel.updateNickname(newNickname, userId);
                    val intent = Intent().apply {
                        putExtra("nickname", newNickname);
                    }
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showToast(this, "User ID not found", it);
                }
            } else {
                ToastUtils.showToast(this, "Nickname cannot be empty", it);
            }
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {}
}