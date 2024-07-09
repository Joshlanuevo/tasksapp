package com.vancoding.tasksapp.ui

import MineViewModel
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.vancoding.tasksapp.databinding.ActivityModifyUsernameBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.util.ToastUtils
import com.vancoding.tasksapp.viewmodel.MineViewModelFactory

class ModifyUsernameActivity : BaseActivity() {
    private lateinit var bindView: ActivityModifyUsernameBinding
    private lateinit var mViewModel: MineViewModel

    override fun initView() {
        bindView = ActivityModifyUsernameBinding.inflate(layoutInflater)
        setContentView(bindView.root)

        val userRepository = UserRepository(UserDb.getDatabase(applicationContext).userDao)
        mViewModel = ViewModelProvider(this, MineViewModelFactory(application, userRepository)).get(MineViewModel::class.java)

        val currentUsername = intent.getStringExtra("username")
        bindView.etUsername.setText(currentUsername)

        bindView.ivClear.setOnClickListener {
            bindView.etUsername.setText("")
        }

        bindView.toolbar.run {
            viewBack.setOnClickListener {
                finish()
            }
            tvTitle.text = "Edit Username"
        }

        bindView.btnSave.setOnClickListener {
            val newUsername = bindView.etUsername.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                val userId = PreferencesManager.getUserId(this)
                if (userId != null) {
                    mViewModel.updateUsername(newUsername, userId)
                    val intent = Intent().apply {
                        putExtra("username", newUsername)
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    ToastUtils.showToast(this, "User ID not found", it)
                }
            } else {
                ToastUtils.showToast(this, "Username cannot be empty", it)
            }
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {}
}
