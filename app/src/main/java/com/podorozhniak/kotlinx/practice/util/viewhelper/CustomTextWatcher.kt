package com.podorozhniak.kotlinx.practice.util.viewhelper

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomTextWatcher(
    private val editText: TextInputEditText,
    private val inputLayout: TextInputLayout,
    private val doAfterTextChanged: (TextInputEditText, TextInputLayout) -> Unit
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        doAfterTextChanged(editText, inputLayout)
    }
}