package com.itaewonproject.customviews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.widget.ImageView
import com.itaewonproject.R

class RoundedImageView: ImageView {
    private var imageRadius =0f
    private var isCircular = false


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int):super(context, attrs, defStyleAttr, defStyleRes){
        initView(context,attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        initView(context,attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs){
        initView(context,attrs)

    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)

    }
    private fun initView(context: Context?, attrs: AttributeSet?){
        val a = context!!.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)
        imageRadius = a.getDimension(R.styleable.RoundedImageView_imageRadius,0f)
        isCircular = a.getBoolean(R.styleable.RoundedImageView_isCircular,false)
        setRounds()
    }
    private fun setRounds(){
        var drawable:Drawable
        if(isCircular){
            drawable = ShapeDrawable(OvalShape())
        }else
        {
            drawable = GradientDrawable()
            drawable.setColor(Color.TRANSPARENT)
            drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadius = imageRadius
        }
        background = drawable
        clipToOutline = true
    }
}