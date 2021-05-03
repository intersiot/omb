package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.intersiot.ohmybank.databinding.ActivityInfoChageBinding
import com.intersiot.ohmybank.databinding.ActivityMyInfoBinding

class InfoChageActivity : AppCompatActivity() {
    // layout
    private lateinit var binding: ActivityInfoChageBinding
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestroe = FirebaseFirestore.getInstance()

    private val SEARCH_ADDRESS_ACTIVITY = 1000

    var tag = "InfoChageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoChageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 주소 변경
        binding.inputAddress.setOnClickListener {
            Log.d(tag, "주소 변경 선택됨")
            var intent = Intent(this, DaumWebViewActivity::class.java)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }

        // 수정하기
        binding.btnInfoChage.setOnClickListener {
            val intent = Intent(this, MyInfoActivity::class.java)
            Log.d(tag, "내 정보 페이지로 이동")
            startActivity(intent)
        }
    }
}