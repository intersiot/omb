package com.intersiot.ohmybank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.intersiot.ohmybank.adapter.ViewPagerAdapter
import com.intersiot.ohmybank.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // layout view
    private lateinit var binding: ActivityMainBinding

    var tag = "MainActivty"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragmentAdapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewpager.adapter = fragmentAdapter
//        tabBar.addTab(tabBar.newTab().setText("내통장"))
//        tabBar.addTab(tabBar.newTab().setText("금융상품"))
//        tabBar.addTab(tabBar.newTab().setText("전체메뉴"))
        binding.layoutNavigation.setupWithViewPager(binding.viewpager)


    }

}