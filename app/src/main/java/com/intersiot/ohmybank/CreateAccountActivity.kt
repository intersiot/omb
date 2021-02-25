package com.intersiot.ohmybank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class CreateAccountActivity : AppCompatActivity() {
    private val tag: String = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
    }

    /**
     * random() 함수를 사용해서 계좌번호를 만든다.
     * 1. 중복이 되지 않도록 처리해야함.
     * 2. db에 저장되어야 함 -> firebase
     */
    fun onCreateAccount() {
        Log.d(tag, "계좌생성 선택됨")
    }
}