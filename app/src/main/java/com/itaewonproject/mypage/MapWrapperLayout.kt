package com.itaewonproject.mypage

import android.content.Context
import android.view.MotionEvent
import android.widget.FrameLayout

class MapWrapperLayout(context: Context) : FrameLayout(context) {
    private var mOnDragListener: OnDragListener? = null

    interface OnDragListener {
        fun onDrag(motionEvent: MotionEvent)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mOnDragListener != null) {
            mOnDragListener!!.onDrag(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setOnDragListener(mOnDragListener: OnDragListener) {
        this.mOnDragListener = mOnDragListener
    }
}
