package com.podorozhniak.kotlinx.practice.extensions

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.podorozhniak.kotlinx.R

fun EditText.isPersonNameValid(layout: TextInputLayout): Boolean {
    val text = text.toString()
    return when {
        text.isEmpty() -> {
            layout.error = ""
            true
        }
        text.length < 3 || text.length > 33 -> {
            layout.error = resources.getString(R.string.bad_user_name)
            false
        }
        !text[0].isLetter() || !text[text.length - 1].isLetter() || !text.isNameValid() -> {
            layout.error = resources.getString(R.string.bad_user_name)
            false
        }
        else -> {
            layout.error = ""
            true
        }
    }
}

fun EditText.isPhoneValid(layout: TextInputLayout): Boolean {
    val text = text.toString().replace(" ", "")
    return when {
        text.isEmpty() -> {
            layout.error = ""
            true
        }
        !text.isPhoneNumberValid() -> {
            layout.error = resources.getString(R.string.bad_phone)
            false
        }
        else -> {
            layout.error = ""
            true
        }
    }
}

fun EditText.isEmailValid(layout: TextInputLayout): Boolean {
    val text = text.toString()
    return when {
        text.isEmpty() -> {
            layout.error = resources.getString(R.string.required)
            false
        }
        text.length < 6 || text.length > 65 -> {
            layout.error = resources.getString(R.string.bad_email)
            false
        }
        !text.isMailValid() -> {
            layout.error = resources.getString(R.string.bad_email)
            false
        }
        else -> {
            layout.error = ""
            true
        }
    }
}