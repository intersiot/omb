package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.intersiot.ohmybank.databinding.ActivityConsentBinding
import com.intersiot.ohmybank.databinding.ActivityMainBinding

class ConsentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsentBinding

    var tag = "ConsetActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    // 동의 취소 버튼 클릭시
    fun onCancel(view: View) {
        Log.d(tag, "동의 취소 버튼 클릭됨")
        startActivity(Intent(this, LoginActivity::class.java))
    }

    // 동의버튼 클릭시 이벤트 처리
    fun onConsent(view: View) {
        var promotion: Boolean = false

        /**
         * 예외처리
         * 1. 체크박스 동의여부
         */
        if (!binding.firstCheck.isChecked || !binding.secondCheck.isChecked) {
            Log.d(tag, "이용약관 또는 개인정보 수집에 동의해주세요")
            Toast.makeText(this, "이용약관, 개인정보 수집에 동의해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            if (binding.thirdCheck.isChecked) {
                promotion = true
                intent = Intent(applicationContext, JoinActivity::class.java)
                intent.putExtra("promotion", promotion)
                Log.d(tag, "프로모션 동의 체크됨")
                startActivity(intent)
            } else {
                Log.d(tag, "프로모션 동의 체크안됨")
                startActivity(Intent(this, JoinActivity::class.java))
            }
        }
    }
}