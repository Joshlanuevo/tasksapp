package com.vancoding.tasksapp.ui

import MineViewModel
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.vancoding.tasksapp.databinding.ActivityModifyNameBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.extensions.showToast
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.util.ToastUtils
import com.vancoding.tasksapp.viewmodel.MineViewModelFactory

class ModifyNameActivity : BaseActivity() {
    private lateinit var bindView: ActivityModifyNameBinding;
    private lateinit var mViewModel: MineViewModel;

    override fun initView() {
        setupBinding()
        setupViewModel()
        onViewReady()
        setupInitialNickname()
        setupClearButton()
        setupToolbar()
    }

    override fun requestData() {}

    override fun observeCallBack() {}

    private fun setupBinding() {
        bindView = ActivityModifyNameBinding.inflate(layoutInflater)
        setContentView(bindView.root)
    }

    private fun setupViewModel() {
        val userRepository = UserRepository(UserDb.getDatabase(applicationContext).userDao)
        mViewModel = ViewModelProvider(this, MineViewModelFactory(application, userRepository)).get(MineViewModel::class.java)
    }

    private fun onViewReady() {
        with(bindView) {
            btnSave.setOnClickListener { onSaveNicknameClick() }
        }
    }

    private fun setupInitialNickname() {
        val currentNickname = intent.getStringExtra("nickname")
        bindView.etName.setText(currentNickname)
    }

    private fun setupClearButton() {
        bindView.ivClear.setOnClickListener {
            bindView.etName.setText("")
        }
    }

    private fun setupToolbar() {
        with(bindView.toolbar) {
            viewBack.setOnClickListener { finish() }
            tvTitle.text = "Edit"
        }
    }

    private fun onSaveNicknameClick() {
        val newNickname = bindView.etName.text.toString().trim()
        if (newNickname.isEmpty()) {
            showToast("Nickname cannot be empty")
            return
        }

        val userId = PreferencesManager.getUserId(this)
        if (userId != null) {
            updateNickname(newNickname, userId)
        } else {
            showToast("User ID not found")
        }
    }

    private fun updateNickname(newNickname: String, userId: Int) {
        mViewModel.updateNickname(newNickname, userId)
        setResult(Activity.RESULT_OK, Intent().apply { putExtra("nickname", newNickname) })
        finish()
    }
}