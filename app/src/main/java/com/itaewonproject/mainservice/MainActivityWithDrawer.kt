package com.itaewonproject.mainservice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.R
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.widget.ImageView
import androidx.core.view.GravityCompat
import com.itaewonproject.adapter.TabPagerAdapter
import com.itaewonproject.linkshare.LinkShareActivity
import com.itaewonproject.message.MessageListActivity
import com.itaewonproject.mypage.*
import com.itaewonproject.setting.MyInfoActivity
import com.itaewonproject.setting.QuitActivity
import com.itaewonproject.setting.SettingActivity
import com.itaewonproject.search.LocationActivity

class MainActivityWithDrawer : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var buttonSearch:ImageView
    private lateinit var buttonMessage:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_with_drawer)
        tabLayout = findViewById(R.id.tabLayout) as TabLayout
        viewPager = findViewById(R.id.viewPager) as ViewPager
        drawerLayout=findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView=findViewById(R.id.nav_view) as NavigationView
        buttonSearch = findViewById(R.id.button_search) as ImageView
        buttonMessage=findViewById(R.id.button_message) as ImageView

        navigationView.setNavigationItemSelectedListener(this)

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

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.nav_search->{
                val intent = Intent(this,LocationActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_review->{
                val intent = Intent(this,LinkShareActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_message->{
                val intent = Intent(this, MessageListActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_setting->{
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_my_info->{
                val intent = Intent(this,MyInfoActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_quit->{
                val intent = Intent(this, QuitActivity::class.java)
                startActivity(intent)

            }

        }
        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END)
        }else
        {
            super.onBackPressed()
        }
    }

}

