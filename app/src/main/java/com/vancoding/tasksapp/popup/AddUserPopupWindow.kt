package com.vancoding.tasksapp.popup

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.ui.LoginActivity
import com.vancoding.tasksapp.util.PreferencesManager
import com.xu.xpopupwindow.XPopupWindow

class AddUserPopupWindow(var ctx: Context) : XPopupWindow(ctx) {
    override fun getLayoutId(): Int {
        return R.layout.popup_add_user
    }

    override fun getLayoutParentNodeId(): Int {
        return androidx.constraintlayout.widget.R.id.parent
    }

    fun View.gone() {
        visibility = View.GONE
    }

    override fun initViews() {
        val tvAddUser = getPopupView().findViewById<TextView>(R.id.tvAddUser)

        tvAddUser.setOnClickListener {
            showConfirmationDialog()
        }
    }

    override fun initData() {}

    override fun startAnim(view: View): Animator? {
        return null
    }

    override fun exitAnim(view: View): Animator? {
        return null
    }

    override fun animStyle(): Int {
        return -1
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle("Add User")
            .setMessage("If you proceed to add a new user, you will be logged out. Do you want to continue?")
            .setPositiveButton("Proceed") { dialog, _ ->
                logoutUser()
                ctx.startActivity(Intent(ctx, LoginActivity::class.java))
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun logoutUser() {
        // Clear stored user credentials
        PreferencesManager.clearUserCredentials(ctx)
        PreferencesManager.clearUserId(ctx.applicationContext)
        val intent = Intent(ctx, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        ctx.startActivity(intent)
    }
}
