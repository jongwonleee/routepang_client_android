package com.itaewonproject

import android.graphics.Bitmap
import android.util.Log
import com.squareup.picasso.Transformation

class RatioTransformation(private val targetHeight: Int) : Transformation {
    private val KEY = "RatioTransformation"

    override fun transform(source: Bitmap): Bitmap {
        val aspectRatio = (source.width.toDouble() / source.height.toDouble())
        val targetWidth = (targetHeight * aspectRatio).toInt()
        val result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
        Log.i("!!", "$targetWidth , $targetHeight")
        if (result != source) source.recycle()
        return result
    }

    override fun key(): String {
        return KEY
    }
}
