package com.intersiot.ohmybank.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fm: FragmentManager, val fragmentCount: Int)
    : FragmentStatePagerAdapter(fm) {
    var fragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int = fragmentList.size
    // 자바에서는 { return fragmentCount }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }
}