package com.example.movies.ratio

import android.content.Context
import android.util.AttributeSet

class AspectRatioImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    var ratio : Float = 1f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var width = measuredWidth
        var height = measuredHeight

        if (width == 0 && height == 0) {
            return
        }

        if (width == 0) {
            height = (width * ratio).toInt()
        }

        if (height == 0) {
            width = (height / ratio).toInt()
        }

        setMeasuredDimension(width, height)
    }
}