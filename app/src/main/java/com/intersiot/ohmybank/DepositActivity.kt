package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var reference = FirebaseDatabase.getInstance() // 리얼타임 데이터베이스
    // 유저 정보 가져오기 위한 초석
    private var users = UserDTO()
    private var id = mAuth.currentUser?.email
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
                // 내 통장에 입금한 금액 데이터 전달하기
                var transfer = TransactDTO.DepositAndWithdrawal()
                var deposit = transfer.deposit // 입금 금액
                var dateFormat = SimpleDateFormat("yyyy-MM-dd") // 거래 시간
                var mDate = Date()
                account = transfer.account // 입금한 계좌(여기선 내 계좌)
                transfer.timestamp = System.currentTimeMillis()
                // 입금 내역 리얼타임데이터베이스에 추가
                reference.reference.child("Transact").child(time)
                    .child("transfers").push()
                    .setValue(transfer).addOnCompleteListener {
//                        inputCache.text = null
                    }
            } else { // 그 외는 입금한 금액만큼만
                myCache += cache
            }

            // 유저DB 갱신
            firestore.collection("Users").document(email).update("cache", myCache)
            finish()
        }
        Log.d(tag, "내 통장에 입금 성공")
    }
    
    // 리사이클러뷰 설정
    fun setRecycler() {
        var query = reference.reference.child("Transact")
            .child(time).child("transfer")
            .orderByChild("timestamp")
        var options = FirebaseRecyclerOptions.Builder<TransactDTO.DepositAndWithdrawal>()
            .setQuery(query, TransactDTO.DepositAndWithdrawal::class.java).build()
//        adapter = TransactAdapter(options, this)
        
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