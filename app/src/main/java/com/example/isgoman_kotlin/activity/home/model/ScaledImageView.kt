package com.example.isgoman_kotlin.activity.home.model

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class ScaledImageView(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatImageView(
    context!!, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d = drawable
        if (d != null) {
            val width: Int
            val height: Int
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
                height = MeasureSpec.getSize(heightMeasureSpec)
                width =
                    Math.ceil((height * d.intrinsicWidth.toFloat() / d.intrinsicHeight).toDouble())
                        .toInt()
            } else {
                width = MeasureSpec.getSize(widthMeasureSpec)
                height =
                    Math.ceil((width * d.intrinsicHeight.toFloat() / d.intrinsicWidth).toDouble())
                        .toInt()
            }
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}