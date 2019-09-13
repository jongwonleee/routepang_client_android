package com.itaewonproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView

class CustomTextView : TextView {

    private var stroke = false
    private var strokeWidth = 0.0f
    private var strokeColor: Int = 0

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {

        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        initView(context, attrs)
    }

    constructor(context: Context) : super(context) {}

    private fun initView(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
        stroke = a.getBoolean(R.styleable.CustomTextView_textStroke, false)
        strokeWidth = a.getFloat(R.styleable.CustomTextView_textStrokeWidth, 0.0f)
        strokeColor = a.getColor(R.styleable.CustomTextView_textStrokeColor, -0x1)
    }

    override fun onDraw(canvas: Canvas) {

        if (stroke) {
            val states = textColors
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            setTextColor(strokeColor)
            super.onDraw(canvas)

            paint.style = Paint.Style.FILL
            setTextColor(states)
        }

        super.onDraw(canvas)
    }
}


