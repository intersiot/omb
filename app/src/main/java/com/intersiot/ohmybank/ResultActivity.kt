package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.intersiot.ohmybank.databinding.ActivityDepositBinding
import com.intersiot.ohmybank.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    // layout view
    private lateinit var binding: ActivityResultBinding
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestroe = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // intent로 전달된 데이터 받기
        var intent = getIntent()
        var account = intent.getStringExtra("account")
        var payment = intent.getIntExtra("payment", 0)

        // 받은 데이터로 셋팅
        var viewAccount = binding.accountViewNumber
        var viewInputCache = binding.inputCacheView
//        viewAccount.text = account.toString()
//        viewInputCache.text = payment.toString()

        // 로고 클릭시
        binding.logo.setOnClickListener {
            moveHome()
        }

    }   // end onCreate()

    fun onClickOkay(view: View) {
        var intent = Intent(this, TransactionActivity::class.java)
        startActivity(intent)
    }

    private fun moveHome() {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}