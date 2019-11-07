package com.itaewonproject.mainservice

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.R
import android.widget.ImageView
import android.widget.TextView
import com.itaewonproject.adapter.TabPagerAdapter
import com.itaewonproject.message.MessageListActivity
import com.itaewonproject.mypage.*
import com.itaewonproject.search.LocationActivity

class MainActivity : AppCompatActivity(),TabLayout.OnTabSelectedListener {
    override fun onTabReselected(p0: TabLayout.Tab?) {
        setTabView(p0!!.position,true)
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
        setTabView(p0!!.position,false)
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        setTabView(p0!!.position,true)

    }


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var buttonSearch:ImageView
    private lateinit var buttonMessage:ImageView

    val titles = listOf("마이 페이지","뉴스피드", "인기 루트")
    val iconsOff = listOf(R.drawable.ic_ico_user_off,R.drawable.ic_ico_newsfeed_off,R.drawable.ic_ico_pin_off)
    val iconsOn = listOf(R.drawable.ic_ico_user_on,R.drawable.ic_ico_newsfeed_on,R.drawable.ic_ico_pin_on)
/*
        /////원래
    val titles = listOf("뉴스피드", "인기 루트", "마이 페이지")
    val iconsOff = listOf(R.drawable.ic_ico_newsfeed_off,R.drawable.ic_ico_pin_off,R.drawable.ic_ico_user_off)
    val iconsOn = listOf(R.drawable.ic_ico_newsfeed_on,R.drawable.ic_ico_pin_on,R.drawable.ic_ico_user_on)
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabLayout = findViewById(R.id.tabLayout) as TabLayout
        viewPager = findViewById(R.id.viewPager) as ViewPager
        buttonSearch = findViewById(R.id.button_search) as ImageView
        buttonMessage=findViewById(R.id.button_message) as ImageView

        var adapter = TabPagerAdapter(supportFragmentManager, 3)
       /* adapter.addPage(NewsfeedFragment(), "")
        adapter.addPage(FavRouteFragment(), "")*/
        adapter.addPage(MyPageFragment(), "")
        adapter.addPage(ComingSoonFragment(),"")
        adapter.addPage(ComingSoonFragment(),"")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(this)

        setTabView(0,true)
        setTabView(1,false)
        setTabView(2,false)

        buttonSearch.setOnClickListener({
            val intent = Intent(this,LocationActivity::class.java)
            startActivity(intent)
        })

        buttonMessage.setOnClickListener({
            val intent = Intent(this, MessageListActivity::class.java)
            startActivity(intent)
        })


    }


    private fun setTabView(pos:Int,selected:Boolean){
        Log.i("changing Tab","$pos, $selected ${if(selected)iconsOn[pos] else iconsOff[pos]}")
        val view = layoutInflater.inflate(R.layout.tab_view_main,null)
        val title = view.findViewById(R.id.title) as TextView
        val image = view.findViewById(R.id.icon) as ImageView
        title.text=titles[pos]
        title.setTextColor(Color.parseColor(if(selected)"#fd62b0" else "#8e8e93"))
        image.setImageResource(if(selected)iconsOn[pos] else iconsOff[pos])
        tabLayout.getTabAt(pos)?.customView=null
        tabLayout.getTabAt(pos)?.setCustomView(view)
        tabLayout.refreshDrawableState()
    }
}

private fun TabLayout.addOnTabSelectedListener(onPageChangeListener: ViewPager.OnPageChangeListener) {

}

