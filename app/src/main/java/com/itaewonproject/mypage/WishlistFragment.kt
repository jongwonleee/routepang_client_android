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
import com.itaewonproject.model.receiver.Product
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.get.GetBasketConnector
import java.lang.NullPointerException

class WishlistFragment: Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    var list: ArrayList<Location> = arrayListOf()
    private lateinit var customer:Customer
    private val listFragment = WishlistListFragment()
    private val mapFragment = WishlistMapFragment()



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        try{
            if (isVisibleToUser && isResumed ) {
                val products = JsonParser().listJsonParsing(GetBasketConnector().get(customer.customerId), Product::class.java)
                list.clear()
                for( p in products){
                    list.add(p.location)
                }
            }
        }catch (e: NullPointerException){
            e.printStackTrace()
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.customer = arguments!!.getSerializable("customer")as Customer
        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        viewPager = view.findViewById(R.id.viewPager) as ViewPager

        val adapter = TabPagerAdapter(childFragmentManager, 2)
        adapter.addPage(listFragment, "리스트")
        adapter.addPage(mapFragment, "지도")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.clipToOutline=true
    }

    override fun onResume() {
        super.onResume()
        Log.i("is wishlist resumed?","yes!!!!")
        try{
            val products = (activity!!.application as Routepang).wishlist
            list.clear()
            for( p in products){
                list.add(p.location)
            }
        }catch (e: NullPointerException){
            e.printStackTrace()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }
}
