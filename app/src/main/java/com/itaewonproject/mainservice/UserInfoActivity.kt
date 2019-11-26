package com.itaewonproject.mainservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.TabPagerAdapter
import com.itaewonproject.customviews.NonSwipeViewPager
import com.itaewonproject.model.receiver.CustomerPage
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.model.sender.Follow
import com.itaewonproject.mypage.ReviewFragment
import com.itaewonproject.mypage.RouteFragment
import com.itaewonproject.mypage.WishlistFragment
import com.itaewonproject.rests.get.GetCustomerPageConnector
import com.itaewonproject.rests.post.PostFollowConnector

class UserInfoActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: NonSwipeViewPager
    private lateinit var textName: TextView
    private lateinit var profile: ImageView
    private lateinit var followingCount: TextView
    private lateinit var followerCount: TextView
    private lateinit var adapter: TabPagerAdapter
    private lateinit var buttonFollow: TextView

    private lateinit var customer:Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        customer = intent.getSerializableExtra("customer") as Customer
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        textName = findViewById(R.id.text_name)
        profile = findViewById(R.id.image_profile)
        followerCount = findViewById(R.id.text_follower)
        followingCount = findViewById(R.id.text_following)
        buttonFollow = findViewById(R.id.button_follow)

        adapter = TabPagerAdapter(supportFragmentManager, 3)

        val bundle = Bundle(1)
        bundle.putSerializable("customer",customer)
        val routeFragment = RouteFragment()
        val wishlistFragment = WishlistFragment()
        val reviewFragment = ReviewFragment()
        routeFragment.arguments = bundle
        wishlistFragment.arguments =bundle
        reviewFragment.arguments=bundle

        adapter.addPage(routeFragment, "루트")
        adapter.addPage(wishlistFragment, "위시리스트")
        adapter.addPage(reviewFragment, "후기")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.setSwipePagingEnabled(true)

        tabLayout.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            return@setOnTouchListener true
        }
        buttonFollow.setOnClickListener({
            val follow = Follow()
            follow.target = customer
            follow.follower = (application as Routepang).customer
            val ret = PostFollowConnector().post(follow)
            if(ret.responceCode==200){
                Toast.makeText(this,"팔로우 완료",Toast.LENGTH_SHORT).show()
            }else
            {
                Toast.makeText(this,"다시 시도해주세요",Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val ret = JsonParser().objectJsonParsing(
            GetCustomerPageConnector().get(customer.customerId).body!!, CustomerPage::class.java)
        textName.text = ret?.reference
        followingCount.text = ret?.followingCount.toString()
        followerCount.text= ret?.follwerCount.toString()
        val routeCount = ret?.routeCount
        val wishlistCount = ret?.productCount
        val reviewCount = ret?.articleCount
        adapter.setPageString(0, "루트\n$routeCount")
        adapter.setPageString(1,  "위시리스트\n$wishlistCount")
        adapter.setPageString(2,  "후기\n$reviewCount")
    }
}
