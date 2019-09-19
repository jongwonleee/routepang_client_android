package com.itaewonproject.mypage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

data class Page(val title: String, val fragment: Fragment)


class TabPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    var pages: ArrayList<Page> = arrayListOf()

    init {
    }

    override fun getItem(position: Int): Fragment {
        return pages[position].fragment
    }

    fun addPage(fragment: Fragment, title: String) {
        pages.add(Page(title, fragment))
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pages[position].title
    }

    override fun getCount(): Int {
        return pages.size
    }
}
