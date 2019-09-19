package com.itaewonproject

import android.graphics.Bitmap
import android.util.Log
import com.squareup.picasso.Transformation

class RatioTransformation(val targetHeight: Int) : Transformation {
    private val KEY = "RatioTransformation"

    override fun transform(source: Bitmap): Bitmap {
        var aspectRatio = (source.width.toDouble() / source.height.toDouble())
        var targetWidth = (targetHeight * aspectRatio).toInt()
        var result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
        Log.i("!!", "$targetWidth , $targetHeight")
        if (result != source) source.recycle()
        return result
    }

    override fun key(): String {
        return KEY
    }
}
