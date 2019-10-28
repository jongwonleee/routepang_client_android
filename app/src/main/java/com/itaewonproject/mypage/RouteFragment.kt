package com.itaewonproject.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.itaewonproject.R
import com.itaewonproject.adapter.TabPagerAdapter
import com.itaewonproject.model.receiver.Route

class RouteFragment : Fragment() {

    // private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: TabPagerAdapter
    lateinit var route: Route
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        viewPager = view.findViewById(R.id.viewPager) as ViewPager
        adapter = TabPagerAdapter(childFragmentManager, 3)
        adapter.addPage(RouteListFragment(), "List")
        adapter.addPage(RouteEditFragment(), "Edit")
        adapter.addPage(RouteMapFragment(), "Map")
        viewPager.adapter = adapter
        // tabLayout.setupWithViewPager(viewPager)
        /*val fragmentTransaction = fragmentManager!!.beginTransaction()
        var fragment = RouteListFragment()
        fragmentTransaction.add(R.id.fragment, fragment) // add 대신 replace도 가능
        fragmentTransaction.commit()*/
    }

    fun toEditFragment(item: Route) {
        route = item
        viewPager.setCurrentItem(viewPager.currentItem + 1, true)
       /* var fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment,RouteEditFragment())
        fragmentTransaction.commit()
        Log.i("!!~~","!!!")*/
    }

    //FIXME enum 화시키기
    fun toFragment(next: Boolean) {
        if (next)viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        else viewPager.setCurrentItem(viewPager.currentItem - 1, true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route, container, false)
    }
}
