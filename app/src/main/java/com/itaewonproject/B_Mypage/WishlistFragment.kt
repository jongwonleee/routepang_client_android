package com.itaewonproject.B_Mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.R


class WishlistFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        viewPager = view.findViewById(R.id.viewPager) as ViewPager
        var adapter = TabPagerAdapter(childFragmentManager,2)
        adapter.addPage(WishlistListFragment(),"List")
        adapter.addPage(WishlistMapFragment(),"Map")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }
}
