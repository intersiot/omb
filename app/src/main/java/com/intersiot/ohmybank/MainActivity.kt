package com.intersiot.ohmybank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.intersiot.ohmybank.databinding.ActivityLoginBinding
import com.intersiot.ohmybank.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_main)
    }
}