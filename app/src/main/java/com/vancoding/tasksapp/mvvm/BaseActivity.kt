package com.vancoding.tasksapp.mvvm

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.util.ToastUtils

open abstract class BaseActivity : AppCompatActivity() {
    var isShowNoNetDialog = false;
    var isLogin = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        if (isTranslucentStatus()) {
            setTranslucentStatus();
        }

        initView();
        requestData();
        observeCallBack();
    }

    /**
     * Set the status bar to be transparent for an immersive experience
     */
    open fun setTranslucentStatus() {
        // Starting from Android 5.x, the color needs to be set to transparent; otherwise, the navigation bar will be the default light gray color
        val window = window;
        val decorView = window.decorView;
        // Starting from Android 5.x, the color needs to be set to transparent; otherwise, the navigation bar will be the default light gray color
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.statusBarColor = Color.TRANSPARENT;
    }

    abstract fun initView();

    abstract fun requestData();

    abstract fun observeCallBack();

    open fun isTranslucentStatus(): Boolean {
        return true;
    }
}