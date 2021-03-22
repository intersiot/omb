package com.intersiot.ohmybank.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.intersiot.ohmybank.fragment.MyBankFragment
import com.intersiot.ohmybank.fragment.NewsFragment
import com.intersiot.ohmybank.fragment.TransactionFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> { MyBankFragment() }
            1 -> { TransactionFragment() }
            else -> { return NewsFragment() }
        }
    }

    override fun getCount(): Int {
        // 자바에서는 { return fragmentCount }
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "내통장"
            1 -> "거래내역"
            else -> { return "전체메뉴" }
        }
    }
}