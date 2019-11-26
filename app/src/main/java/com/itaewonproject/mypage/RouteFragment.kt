package com.itaewonproject.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.TabPagerAdapter
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.receiver.Product
import com.itaewonproject.model.receiver.Route
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.get.GetRouteConnector

class RouteFragment: Fragment() {

    // private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: TabPagerAdapter
    lateinit var route: Route
    lateinit var customer:Customer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // tabLayout = view.findViewById(R.id.tabLayout) as TabLayout

        viewPager = view.findViewById(R.id.viewPager) as ViewPager
        val routeListFragment =  RouteListFragment()
        val routeEditFragment = RouteEditFragment()
        val routeMapFragment = RouteMapFragment()
        routeListFragment.arguments = arguments
        routeEditFragment.arguments = arguments
        routeMapFragment.arguments =arguments
        adapter = TabPagerAdapter(childFragmentManager, 3)
        adapter.addPage(routeListFragment, "List")
        adapter.addPage(routeEditFragment, "Edit")
        adapter.addPage(routeMapFragment, "Map")
        viewPager.adapter = adapter
    }

    fun toEditFragment(item: Route) {
        route = item
        route.products = JsonParser().listJsonParsing(GetRouteConnector().get(route.routeId),Product::class.java)
        viewPager.setCurrentItem(1, true)
    }

    fun toListFragment() {
        viewPager.setCurrentItem(0, true)
    }

    //FIXME enum 화시키기
    fun toFragment(next: Boolean) {
        if (next)viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        else viewPager.setCurrentItem(viewPager.currentItem - 1, true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.customer = arguments!!.getSerializable("customer") as Customer
        return inflater.inflate(R.layout.fragment_route, container, false)
    }

}
