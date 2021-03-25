package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.databinding.ActivityDepositBinding
import com.intersiot.ohmybank.model.UserDTO

class DepositActivity : AppCompatActivity() {
    private val tag: String = "DepositActivity"
    // layout view
    private lateinit var binding: ActivityDepositBinding
    // firebase 인증
    private var firestore = FirebaseFirestore.getInstance()
    private var mAuth = FirebaseAuth.getInstance()
    // 유저 정보 가져오기 위한 초석
    var users = UserDTO()
    private var id = mAuth.currentUser?.email
//    var format = DecimalFormat("###,###,###,###")
    var cache = 0
    private lateinit var email: String
    private var account: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepositBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (id != null) {
            firestore.collection("Users").document(id!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    users = documentSnapshot.toObject<UserDTO>()!!
                    account = users.account
                    binding.accountView.text = account
                    Log.d(tag, "계좌번호: $account 로 변경됨")
                }
        }

        // 로그아웃 버튼 클릭시
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            Log.d(tag, "로그아웃 성공")
            mAuth.signOut()
            startActivity(intent)
        }

    } // end onCreate()

    fun onDeposit(view: View) {
        var inputCache = binding.inputCache.text
        cache = Integer.parseInt(inputCache.toString())

        // 유저 아이디 가져오기
        email = mAuth.currentUser?.email!!
        // 유저정보 받아오기
        firestore.collection("Users").document(email).get().addOnCompleteListener {
            var user = it.result?.toObject(UserDTO::class.java)!!
            var myCache = user.cache!!

            // 캐시가 20억이 넘을 경우 20억으로 고정: Int 형 값 넘어가는 오류 방지
            if (myCache + cache.toLong() > 2000000000) {
                myCache == 2000000000

            } else { // 그 외는 입금한 금액만큼만
                myCache += cache
            }

            // 유저DB 갱신
            firestore.collection("Users").document(email).update("cache", myCache)
            finish()
        }
        Log.d(tag, "내 통장에 입금 성공")
    }

    fun onDepositCancel(view: View) {
        Log.d(tag, "입금하기 취소")
        moveHome()
    }

    private fun moveHome() {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
} // end Activity