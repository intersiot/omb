package com.intersiot.ohmybank;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.adapter.TransactAdapter
import com.intersiot.ohmybank.databinding.ActivityTransactionBinding
import com.intersiot.ohmybank.model.TransactDTO
import com.intersiot.ohmybank.model.UserDTO

class TransactionActivity : AppCompatActivity() {
    // layout view
    private lateinit var binding: ActivityTransactionBinding
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    // 계좌 거래내역 리사이클러 어댑터
    private lateinit var adapter: TransactAdapter

    var tag = "TransactionActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 유저 정보 가져오기
        if (mAuth.currentUser?.email != null) {
            firestore.collection("Users").document(mAuth.currentUser?.email!!).get()
                    .addOnSuccessListener { documentSnapshot ->
                        var users = documentSnapshot.toObject<UserDTO>()!!
                        var account = users.account
                        var cache = users.cache
                        binding.accountView.text = account
                        binding.moneyView.text = cache.toString()
                        binding.moneyViewTrans.text = cache.toString()
                        Log.d(tag, "생성된 계좌번호: ${account}로 변경됨")
                        Log.d(tag, "보유금액: ${cache}로 변경됨")
                    }
        }

        // bottom navigation click event
        binding.bottomNavMybank.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            Log.d(tag, "내통장으로 이동")
            startActivity(intent)
        }

        binding.bottomNavNews.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            Log.d(tag, "금융뉴스로 이동")
            startActivity(intent)
        }

        // 로그아웃 버튼 클릭시
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            Log.d(tag, "로그아웃 성공")
            mAuth.signOut()
            startActivity(intent)
        }
        
        // 메인 액티비티로 이동
        binding.logo.setOnClickListener {
            moveHome()
        }

        setRecycler()
    } // end onCreate()

    // RecyclerView 설정
    fun setRecycler() {
        // 데이터 조회
        var query = firestore.collection("Transact")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .whereEqualTo("id", mAuth.currentUser?.email)
        var options = FirestoreRecyclerOptions.Builder<TransactDTO>()
            .setQuery(query, TransactDTO::class.java).build()
        adapter = TransactAdapter(options)
        // 리사이클러뷰 설정
        binding.transferRecyclerView.setHasFixedSize(true)
        var layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.transferRecyclerView.layoutManager = layoutManager
        binding.transferRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    } // end onStart()

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    } // end onStop()

    private fun moveHome() {
        Log.d(tag, "메인 액티비티로 이동")
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
} // end Activity
