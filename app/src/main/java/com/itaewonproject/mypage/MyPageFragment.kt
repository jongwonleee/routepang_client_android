package com.itaewonproject.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.R
import com.itaewonproject.adapter.TabPagerAdapter
import com.itaewonproject.setting.SettingActivity

class MyPageFragment : Fragment() {
    // TODO: Rename and change types of parameters\

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var settingButton:ImageView
    private lateinit var textName:TextView
    private lateinit var profile:ImageView
    private lateinit var followingCount:TextView
    private lateinit var followerCount:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        viewPager = view.findViewById(R.id.viewPager) as ViewPager
        settingButton = view.findViewById(R.id.button_setting) as ImageView
        textName = view.findViewById(R.id.text_name) as TextView
        profile = view.findViewById(R.id.image_profile) as ImageView
        followerCount = view.findViewById(R.id.text_follower) as TextView
        followingCount = view.findViewById(R.id.text_following) as TextView

        settingButton.setOnClickListener({
            Log.i("!!!","button clicked")
            val intent = Intent(context,SettingActivity::class.java)
            startActivity(intent)
        })

        val adapter = TabPagerAdapter(childFragmentManager, 3)
        adapter.addPage(RouteFragment(), "루트\n200")
        adapter.addPage(WishlistFragment(), "위시리스트\n150")
        adapter.addPage(ReviewFragment(), "후기\n100")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }


}
