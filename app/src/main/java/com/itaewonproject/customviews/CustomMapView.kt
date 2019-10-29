package com.itaewonproject.customviews

import android.content.Context
import com.google.android.gms.maps.MapView
import android.util.AttributeSet
import android.view.MotionEvent




class CustomMapView : MapView {
    var isInRoute=false
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context) : super(context) {}

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        if(isInRoute)parent.parent.parent.requestDisallowInterceptTouchEvent(true)
        else parent.parent.requestDisallowInterceptTouchEvent(true)

        return super.onInterceptTouchEvent(ev)
    }
}