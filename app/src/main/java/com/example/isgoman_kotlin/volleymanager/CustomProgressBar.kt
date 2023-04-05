package com.example.isgoman_kotlin.volleymanager

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout

/*
class CustomProgressBar(private val context: Context, resourceIdOfImage: Int) :
    Dialog(context, R.style.TransparentProgressDialog) {
    private val iv: ImageView

    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
        iv = ImageView(context)
        iv.setImageResource(resourceIdOfImage)
        layout.addView(iv, params)
        addContentView(layout, params)
    }

    override fun show() {
        if (!(context as Activity).isFinishing) {
            super.show()
            val anim = RotateAnimation(
                0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            anim.setInterpolator(context, R.interpolator.linear)
            anim.repeatCount = Animation.INFINITE
            anim.duration = 1000
            iv.animation = anim
            iv.startAnimation(anim)
        }
    }
}*/
