package com.podorozhniak.kotlinx.practice.extensions

import com.google.android.material.textfield.TextInputLayout
import com.podorozhniak.kotlinx.practice.view.custom.ClearEditText


fun ClearEditText.clearText(layout: TextInputLayout) {
    setClearClickListener(object : ClearEditText.DrawableClickListener {
        override fun onClick() {
            if (layout.error?.isNotBlank() == true) {
                layout.error = ""
                setText("")
            }
        }
    })
}