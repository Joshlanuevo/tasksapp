package com.vancoding.tasksapp.ui

import MineViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.databinding.ActivityPersonalInfoBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.util.ToastUtils
import com.vancoding.tasksapp.viewmodel.MineViewModelFactory

class PersonalInfoActivity : BaseActivity() {
    private lateinit var bindView: ActivityPersonalInfoBinding
    private lateinit var mViewModel: MineViewModel

    companion object {
        private const val MODIFY_NAME_REQUEST_CODE = 1
        private const val IMAGE_PICKER_REQUEST_CODE = 2
        private const val MODIFY_USERNAME_REQUEST_CODE = 3
    }

    @SuppressLint("ResourceType")
    override fun initView() {
        bindView = ActivityPersonalInfoBinding.inflate(layoutInflater)
        setContentView(bindView.root)

        val userRepository = UserRepository(UserDb.getDatabase(applicationContext).userDao)
        mViewModel = ViewModelProvider(this, MineViewModelFactory(application, userRepository)).get(MineViewModel::class.java)

        val nickname = intent.getStringExtra("nickname")
        val username = intent.getStringExtra("username")

        bindView.tvNickName.text = nickname
        bindView.tvUsername.text = username

        mViewModel.userInfo.observe(this, Observer { userInfo ->
            bindView.tvNickName.text = userInfo.nickname
            bindView.tvUsername.text = userInfo.username
            bindView.tvEditUsername.text = userInfo.username
            bindView.layoutHeader.showUrl(userInfo.avatar)
        })

        bindView.ivBack.setOnClickListener {
            finish()
        }

        bindView.layoutHeader.setOnClickListener {
            ImagePicker.with(this@PersonalInfoActivity)
                .galleryOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(IMAGE_PICKER_REQUEST_CODE)
        }

        bindView.layoutHeader.setRoundRadius(100000F)

        bindView.cons1.setOnClickListener {
            val intent = Intent(this, ModifyNameActivity::class.java)
            intent.putExtra("nickname", mViewModel.userInfo.value?.nickname)
            startActivityForResult(intent, MODIFY_NAME_REQUEST_CODE)
        }

        bindView.cvUserName.setOnClickListener {
            val intent = Intent(this, ModifyUsernameActivity::class.java)
            intent.putExtra("username", bindView.tvEditUsername.text.toString())
            startActivityForResult(intent, MODIFY_USERNAME_REQUEST_CODE)
        }

        bindView.layoutPwd.setOnClickListener {
            startActivity(Intent(this, ModifyPasswordActivity::class.java))
        }

        mViewModel.getUserInfo()
    }

    fun updateAvatar(newAvatarUrl: String) {
        mViewModel.updateAvatar(newAvatarUrl)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                updateAvatar(imageUri.toString())
                setResult(Activity.RESULT_OK, data)
            }
        } else if (requestCode == MODIFY_NAME_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val updateNickName = data?.getStringExtra("nickname")
            if (!updateNickName.isNullOrEmpty()) {
                mViewModel.getUserInfo()
                setResult(Activity.RESULT_OK, data)
            }
        } else if (requestCode == MODIFY_USERNAME_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val updatedUsername = data?.getStringExtra("username")
            if (!updatedUsername.isNullOrEmpty()) {
                bindView.tvEditUsername.text = updatedUsername
                ToastUtils.showToast(this, "Username updated successfully", bindView.root);
                mViewModel.getUserInfo()
                setResult(Activity.RESULT_OK, data)
            }
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {}
}
