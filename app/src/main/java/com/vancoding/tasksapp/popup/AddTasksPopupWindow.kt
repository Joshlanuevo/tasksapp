package com.vancoding.tasksapp.popup

import android.animation.Animator
import android.content.Context
import android.view.View
import android.widget.TextView
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.dialog.AddTasksDialog
import com.xu.xpopupwindow.XPopupWindow

class AddTasksPopupWindow(var ctx: Context) : XPopupWindow(ctx) {

    override fun getLayoutId(): Int {
        return R.layout.popup_tasks_add;
    }

    override fun getLayoutParentNodeId(): Int {
        return androidx.constraintlayout.widget.R.id.parent;
    }

    fun View.gone() {
        visibility = View.GONE
    }

    override fun initViews() {
        val tvAddTasks = getPopupView().findViewById<TextView>(R.id.tvAddTasks)

        tvAddTasks.setOnClickListener {
            AddTasksDialog(ctx).showDialog();
        }
    }

    override fun initData() {
    }

    override fun startAnim(view: View): Animator? {
        return null
    }

    override fun exitAnim(view: View): Animator? {
        return null
    }

    override fun animStyle(): Int {
        return -1
    }
}
