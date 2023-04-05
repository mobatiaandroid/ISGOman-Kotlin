package com.example.isgoman_kotlin.manager

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.example.isgoman_kotlin.constants.JsonTagConstants
import com.example.isgoman_kotlin.constants.JsonTagConstants.FONT_NORMAL

class CustomTextViewNormal: androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {
        val type = Typeface.createFromAsset(context.assets, FONT_NORMAL)
        this.typeface = type

    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val type = Typeface.createFromAsset(context.assets, FONT_NORMAL)
        this.typeface = type
    }
    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attributeSet, defStyleAttr) {
        val type = Typeface.createFromAsset(context.assets, FONT_NORMAL)
        this.typeface = type
    }
}