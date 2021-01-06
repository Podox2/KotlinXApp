package com.podorozhniak.kotlinx.practice.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatImageView
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.practice.util.Screen
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_SIZE_DP = 2f
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = Screen.convertDpToPx(DEFAULT_BORDER_SIZE_DP)
    private val clipPath = Path()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)

            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)

            borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, borderWidth)

            a.recycle()
        }
    }


    @Dimension fun getBorderWidth(): Int = Screen.convertPxToDp(borderWidth).toInt()

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = Screen.convertDpToPx(dp.toFloat())
        invalidate()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = resources.getColor(colorId, context.theme)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        val x = width / DEFAULT_BORDER_SIZE_DP
        val y = height / DEFAULT_BORDER_SIZE_DP

        clipPath.addCircle(x, y, Math.min(x, y), Path.Direction.CW)
        canvas?.clipPath(clipPath)

        super.onDraw(canvas)

        paint.apply {
            style = Paint.Style.STROKE
            color = borderColor
            strokeWidth = borderWidth
        }

        canvas?.drawCircle(x, y, min(x, y), paint)
    }

}