package com.itaewonproject.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.JsonParser
import com.itaewonproject.player.BasketConnector
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.player.LocationConnector
import com.itaewonproject.search.LocationActivity


class WishlistFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var buttonSearch: LinearLayout
    lateinit var list:ArrayList<Location>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = JsonParser().listJsonParsing(BasketConnector().get((1).toLong()),Location::class.java)
        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        viewPager = view.findViewById(R.id.viewPager) as ViewPager
        buttonSearch = view.findViewById(R.id.button_search) as LinearLayout

        buttonSearch.setOnClickListener({
            var intent = Intent(activity,LocationActivity::class.java)
            startActivity(intent)
        })
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
