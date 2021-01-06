package com.podorozhniak.kotlinx.practice.extensions

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView

fun TextView.underlinedText(textSpan: String) {
    val spannableString = SpannableString(textSpan)
    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
    text = spannableString
}

fun TextView.makeBoldText(textSpan: String) {
    val spannableString = SpannableString(textSpan)
    spannableString.setSpan(
        StyleSpan(Typeface.BOLD),
        0,
        text.length,
        0
    )
    text = spannableString
}