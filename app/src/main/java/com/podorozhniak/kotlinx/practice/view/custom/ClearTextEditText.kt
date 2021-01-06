package com.podorozhniak.kotlinx.practice.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.textfield.TextInputEditText

class ClearEditText : TextInputEditText {

    private var clickListener: DrawableClickListener? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) :
            super(context, attrs, defStyle) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val right = this.right
            val x = event.x
            val y = event.y
            if ((x > (right - this.bottom) && x < right) && (y > this.top && y < this.bottom)) {
                clickListener!!.onClick()
                event.action = MotionEvent.ACTION_CANCEL
            }
            return super.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    fun setClearClickListener(listener: DrawableClickListener) {
        this.clickListener = listener
    }

    interface DrawableClickListener {
        fun onClick()
    }
}