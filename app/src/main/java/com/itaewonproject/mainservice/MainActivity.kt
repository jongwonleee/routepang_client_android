package com.itaewonproject.mainservice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.R
import android.widget.ImageView
import com.itaewonproject.mypage.*
import com.itaewonproject.search.LocationActivity

class MainActivity : AppCompatActivity() {


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var buttonSearch:ImageView
    private lateinit var buttonMessage:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabLayout = findViewById(R.id.tabLayout) as TabLayout
        viewPager = findViewById(R.id.viewPager) as ViewPager
        buttonSearch = findViewById(R.id.button_search) as ImageView
        buttonMessage=findViewById(R.id.button_message) as ImageView

        var adapter = TabPagerAdapter(supportFragmentManager, 3)
        adapter.addPage(RouteFragment(), "Route")
        adapter.addPage(WishlistFragment(), "Wish List")
        adapter.addPage(MyPageFragment(), "Mypage")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        buttonSearch.setOnClickListener({
            val intent = Intent(this,LocationActivity::class.java)
            startActivity(intent)
        })


    }
}

