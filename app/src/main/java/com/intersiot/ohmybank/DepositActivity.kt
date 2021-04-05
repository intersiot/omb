package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.adapter.TransactAdapter
import com.intersiot.ohmybank.databinding.ActivityDepositBinding
import com.intersiot.ohmybank.model.TransactDTO
import com.intersiot.ohmybank.model.UserDTO
import java.text.SimpleDateFormat
import java.util.*

class DepositActivity : AppCompatActivity() {
    private val tag: String = "DepositActivity"
    // layout view
    private lateinit var binding: ActivityDepositBinding
    // firebase 인증
    private var firestore = FirebaseFirestore.getInstance()
    private var mAuth = FirebaseAuth.getInstance()

    private var transfer = TransactDTO()

    private var myAccount: String? = null
    private var cache = 0
    private var account: String? = null
    private lateinit var email: String
    private lateinit var time: String // 시간
    private lateinit var adater: TransactAdapter // 거래내역 어댑터
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepositBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 유저 아이디 가져오기
        var id = mAuth.currentUser?.email
        if (id != null) {
            firestore.collection("Users").document(id!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    var users = documentSnapshot.toObject<UserDTO>()!!
                    myAccount = users.account
                    cache = users.cache!!
                    binding.accountViewNumber.text = myAccount
                    binding.accountViewCache.text = cache.toString()
                    Log.d(tag, "계좌번호: ${myAccount}, 출금가능액: ${cache}로 변경됨")
                }
        }

        // 처음에는 안보이게 하기
        binding.inputAccountLayout.visibility = View.INVISIBLE  // 계좌번호 입력 안보이게 하기
        binding.btnDepositLayout.visibility = View.INVISIBLE    // btnDepositLayout 안보이게

        // 로그아웃 버튼 클릭시
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            Log.d(tag, "로그아웃 성공")
            mAuth.signOut()
            startActivity(intent)
        }
    } // end onCreate()

    // 확인 버튼 클릭 이벤트 처리
    fun onClickOkay(view: View) {
        if (binding.inputCache.text.isNotEmpty()) {
            binding.btnOkay.visibility = View.INVISIBLE             // 확인 버튼 숨기고
            binding.inputAccountLayout.visibility = View.VISIBLE    // 계좌번호 입력버튼 나타남
            binding.btnDepositLayout.visibility = View.VISIBLE      // 이체 버튼 레이아웃 나타남
        } else {
            Toast.makeText(this, "금액을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    // 내 통장에 입금하기 버튼 클릭시
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
                // 내 통장에 입금한 금액 데이터 전달하기
            } else { // 그 외는 입금한 금액만큼만
                myCache += cache
            }
            // 유저DB 갱신
            firestore.collection("Users").document(email).update("cache", myCache)
            finish()
        }
        Log.d(tag, "내 통장에 입금 성공")
    }

    // 다른 계좌로 이체하기 클릭시
    fun onDepositTrans(view: View) {
        var inputCache = binding.inputCache.text
        cache = Integer.parseInt(inputCache.toString())
        email = mAuth.currentUser?.email!!  // 유저 아이디 가져오기

        // 유저정보 받아오기
        firestore.collection("Users").document(email).get().addOnCompleteListener {
            var user = it.result?.toObject(UserDTO::class.java)!!
            var myCache = user.cache!!
            // 캐시가 20억이 넘을 경우 20억으로 고정: Int 형 값 넘어가는 오류 방지
            if (myCache - cache.toLong() > 2000000000) {
                myCache == 2000000000
                // 내 통장에 입금한 금액 데이터 전달하기
            } else { // 그 외는 입금한 금액만큼만
                myCache -= cache
            }
            // 유저DB 갱신
            firestore.collection("Users").document(email).update("cache", myCache)
        }
        Log.d(tag, "다른 계좌로 이체")
    }

    private fun moveHome() {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
} // end Activity