package com.podorozhniak.kotlinx.practice.extensions

import com.google.android.material.textfield.TextInputLayout
import com.podorozhniak.kotlinx.practice.util.customview.ClearTextEditText


fun ClearTextEditText.clearText(layout: TextInputLayout) {
    setClearClickListener(object : ClearTextEditText.DrawableClickListener {
        override fun onClick() {
            if (layout.error?.isNotBlank() == true) {
                layout.error = ""
                setText("")
            }
        }
    })
}