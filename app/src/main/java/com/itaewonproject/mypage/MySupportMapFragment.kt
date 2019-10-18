package com.itaewonproject.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.SupportMapFragment

class MySupportMapFragment : SupportMapFragment() {
    var mOriginalContentView: View? = null
    lateinit var mMapWrapperLayout: MapWrapperLayout

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {

        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState)
        mMapWrapperLayout = MapWrapperLayout(activity!!)
        mMapWrapperLayout.addView(mOriginalContentView)
        return mMapWrapperLayout
    }

    override fun getView(): View? {
        return mOriginalContentView
    }

    fun setOnDragListener(onDragListener: MapWrapperLayout.OnDragListener) {
        mMapWrapperLayout.setOnDragListener(onDragListener)
    }
}
