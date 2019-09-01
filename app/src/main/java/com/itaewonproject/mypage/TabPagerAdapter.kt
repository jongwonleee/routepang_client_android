package com.itaewonproject.mypage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    var fragments: ArrayList<Fragment>
    var titles:ArrayList<String>
    private var count=0

    init{
        fragments = ArrayList<Fragment>()
        titles = ArrayList<String>()
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }


    fun addPage(fragment: Fragment,title:String){
        fragments.add(fragment)
        titles.add(title)
        count++
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return count
    }

}