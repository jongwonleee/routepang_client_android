package com.itaewonproject.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.TabPagerAdapter
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.rests.get.GetBasketConnector

class WishlistFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    lateinit var list: ArrayList<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ret = GetBasketConnector().get((activity!!.application as Routepang).customer.customerId)
        Log.i("wishlist!!!",ret.body)
        list = JsonParser().listJsonParsing(ret, Location::class.java)
        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        viewPager = view.findViewById(R.id.viewPager) as ViewPager

        var adapter = TabPagerAdapter(childFragmentManager, 2)
        adapter.addPage(WishlistListFragment(), "리스트")
        adapter.addPage(WishlistMapFragment(), "지도　")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.clipToOutline=true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }
}
