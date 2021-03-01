package com.intersiot.ohmybank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.intersiot.ohmybank.databinding.ActivityCreateAccountBinding
import com.intersiot.ohmybank.databinding.ActivityMainBinding
import java.util.*

class CreateAccountActivity : AppCompatActivity() {
    private val tag: String = "CreateAccountActivity"
    // layout view
    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    /**
     * random() 함수를 사용해서 계좌번호를 만든다.
     * 1. 중복이 되지 않도록 처리해야함.
     * 2. db에 저장되어야 함 -> firebase
     */
    fun onCreateAccount(view: View) {
        Log.d(tag, "계좌생성 선택됨")
    }

    fun createAccount() {
        
    }
}