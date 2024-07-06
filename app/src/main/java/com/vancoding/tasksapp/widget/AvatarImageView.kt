package com.vancoding.tasksapp.widget

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.vancoding.tasksapp.R

class AvatarImageView : FrameLayout {
    private lateinit var ivHeaderUrl: ImageView
    private lateinit var tvHeaderDefault: TextView
    private var roundRadius = 100F
    private var chatId = ""
    private var chatName = ""

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_header_new, this)
        ivHeaderUrl = findViewById(R.id.ivHeaderUrl)
        tvHeaderDefault = findViewById(R.id.tvHeaderDefault)
    }

    fun setChatId(chatId: String): AvatarImageView {
        this.chatId = chatId
        return this
    }

    fun setChatName(name: String): AvatarImageView {
        this.chatName = name
        return this
    }

    fun setRoundRadius(radius: Float): AvatarImageView {
        this.roundRadius = radius
        return this
    }

    fun setSquare() {
        roundRadius = 0F
    }

    fun showUrl(imageUrl: String?) {
        if (imageUrl?.startsWith("content://") == true) {
            // Handle content URI differently, for example:
            ivHeaderUrl.load(Uri.parse(imageUrl)) {
                transformations(RoundedCornersTransformation(roundRadius))
                placeholder(R.drawable.ic_mine_header)
                error(R.drawable.ic_mine_header)
            }
        } else {
            // Normal URL loading
            ivHeaderUrl.load(imageUrl) {
                transformations(RoundedCornersTransformation(roundRadius))
                placeholder(R.drawable.ic_mine_header)
                error(R.drawable.ic_mine_header)
            }
        }
    }


    fun showImageRes(resId: Int) {
        tvHeaderDefault.visibility = GONE
        ivHeaderUrl.setImageResource(resId)
    }

    fun showDefault() {
        try {
            if (!TextUtils.isEmpty(chatName)) {
                tvHeaderDefault.visibility = VISIBLE
                tvHeaderDefault.text = chatName.substring(0, 1).uppercase() ?: ""
            } else {
                tvHeaderDefault.visibility = VISIBLE
                tvHeaderDefault.text = ""
            }

            if (!TextUtils.isEmpty(chatId)) {
                val lastStr = chatId.substring(chatId.length - 1)
                ivHeaderUrl.setImageResource(getImgRes(lastStr))
            } else {
                // Handle default case when chatId is empty
                ivHeaderUrl.setImageResource(getImgRes("0")) // Default image resource
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tvHeaderDefault.text = ""
        }
    }

    private fun getImgRes(lastStr: String): Int {
        return when (lastStr) {
            "0" -> R.drawable.shape_img_1
            "1" -> R.drawable.shape_img_2
            "2" -> R.drawable.shape_img_3
            "3" -> R.drawable.shape_img_4
            "4" -> R.drawable.shape_img_5
            "5" -> R.drawable.shape_img_6
            "6" -> R.drawable.shape_img_7
            "7" -> R.drawable.shape_img_8
            "8" -> R.drawable.shape_img_9
            "9" -> R.drawable.shape_img_10
            else -> R.drawable.shape_img_1
        }
    }
}
