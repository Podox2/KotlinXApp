package com.podorozhniak.kotlinx.practice.util.viewhelper

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomTextFormatter(
    private val editText: TextInputEditText,
    private val inputLayout: TextInputLayout,
    private val pattern: String,
    private val doAfterTextChanged: (TextInputEditText, TextInputLayout) -> Unit
) :
    TextWatcher {

    companion object {
        const val PHONE_PATTERN = "+## ## ### #### ###"
        const val CARD_PATTERN = "#### #### #### ####"
        const val DATE_PATTERN = "##/##"
    }

    init {
        val maxLength = pattern.length
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = StringBuilder(s)
        if (count > 0 && !isValid(value.toString())) {
            for (i in value.indices) {
                val c = pattern[i]
                if (c != '#' && c != value[i]) {
                    value.insert(i, c)
                }
            }
            editText.setText(value)
            editText.setSelection(editText.text?.length ?: 0)
        }
    }

    override fun afterTextChanged(s: Editable) {
        doAfterTextChanged(editText, inputLayout)
    }

    private fun isValid(value: String): Boolean {
        for (i in value.indices) {
            val c = pattern[i]
            if (c == '#') continue
            if (c != value[i]) {
                return false
            }
        }
        return true
    }
}
