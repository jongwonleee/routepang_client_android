package com.itaewonproject.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.TabPagerAdapter
import com.itaewonproject.customviews.NonSwipeViewPager
import com.itaewonproject.model.receiver.CustomerPage
import com.itaewonproject.rests.get.GetCustomerPageConnector
import com.itaewonproject.setting.SettingActivity

class MyPageFragment : Fragment() {
    // TODO: Rename and change types of parameters\

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: NonSwipeViewPager
    private lateinit var settingButton:ImageView
    private lateinit var textName:TextView
    private lateinit var profile:ImageView
    private lateinit var followingCount:TextView
    private lateinit var followerCount:TextView
    private lateinit var appBarLayout: AppBarLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appBarLayout = view.findViewById(R.id.appBar_layout) as AppBarLayout
        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        viewPager = view.findViewById(R.id.viewPager) as NonSwipeViewPager
        settingButton = view.findViewById(R.id.button_setting) as ImageView
        textName = view.findViewById(R.id.text_name) as TextView
        profile = view.findViewById(R.id.image_profile) as ImageView
        followerCount = view.findViewById(R.id.text_follower) as TextView
        followingCount = view.findViewById(R.id.text_following) as TextView

        settingButton.setOnClickListener({
            val intent = Intent(context,SettingActivity::class.java)
            startActivity(intent)
        })

        val ret = JsonParser().objectJsonParsing(GetCustomerPageConnector().get((activity!!.application as Routepang).customer.customerId).body!!,CustomerPage::class.java)
        textName.text = ret?.reference
        followingCount.text = ret?.followingCount.toString()
        followerCount.text= ret?.follwerCount.toString()
        val routeCount = ret?.routeCount
        val wishlistCount = ret?.productCount
        //val reviewCount = ret?.
        val adapter = TabPagerAdapter(childFragmentManager, 3)
        adapter.addPage(RouteFragment(), "루트\n$routeCount")
        adapter.addPage(WishlistFragment(), "위시리스트\n$wishlistCount")
        adapter.addPage(ReviewFragment(), "후기\n100")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        appBarLayout.setExpanded(true)
        viewPager.setAutoUnexpendingAppbar(appBarLayout)
    }

}
