package com.intersiot.ohmybank

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.databinding.ActivityMainBinding
import com.intersiot.ohmybank.model.UserDTO

class MainActivity : AppCompatActivity() {
    // layout view
    private lateinit var binding: ActivityMainBinding
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestroe = FirebaseFirestore.getInstance()
    // 회원정보 가져오기 위한 초석
    var users = UserDTO()
    private var id = mAuth.currentUser?.email
    private var name: String? = null
    private var account: String? = null
    private var cache: Int? = null

    var tag = "MainActivty"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    } // end onCreate()

    /**
     * onResume() 을 사용하면 액티비티간 이동시 새로고침된다.
     */
    override fun onResume() {
        super.onResume()

        if (id != null) {
            firestroe.collection("Users").document(id!!).get()
                    .addOnSuccessListener { documentSnapshot ->
                        users = documentSnapshot.toObject<UserDTO>()!!
                        name = users.name
                        account = users.account
                        cache = users.cache

                        binding.userName.text = name
                        // 계좌번호가 없을 때와 있을 때 이벤트 처리
                        if (account != null) {
                            binding.accountView.text = account
                        } else {
                            binding.accountView.text = "계좌를 생성해주세요."
                        }
                        binding.moneyView.text = cache.toString()
                        Log.d(tag, "유저 이름 ${name}으로 자동 변경됨, ${users.id}")
                        Log.d(tag, "생성된 계좌번호: $account")
                        Log.d(tag, "현재 보유중인 금액: $cache")
                    }
        }

        // 계좌 생성 버튼 클릭시
        binding.layoutCreateAccount.setOnClickListener {
//            Log.d(tag, "계좌생성 선택됨")
            /**
             * 랜덤함수 사용해서 계좌 만들기 (중복제거)
             * CreateAccountActivity로 넘어가서 주의사항 안내하고 생성
             * 완료 후 생성된 계좌번호 보여주고 -> 메인액티비티로 이동
             */
            val intent = Intent(this, CreateAccountActivity::class.java)
            Log.d(tag, "계좌생성 액티비티로 이동")
            startActivity(intent)
        }

        // 입출금 클릭시
        binding.layoutDeposit.setOnClickListener {
            val intent = Intent(this, DepositActivity::class.java)
            Log.d(tag, "내통장 입금 액티비티로 이동")
            startActivity(intent)
        }

        // 로그아웃 버튼 클릭시
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            Log.d(tag, "로그아웃 성공")
            mAuth.signOut()
            startActivity(intent)
        }

        // bottom navigation click event
        binding.bottomNavTrans.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            Log.d(tag, "거래내역으로 이동")
            startActivity(intent)
        }

        binding.bottomNavNews.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            Log.d(tag, "금융뉴스로 이동")
            startActivity(intent)
        }
    } // end onResume()

    // 마이페이지로 이동
    fun infoChange(view: View) {
        val intent = Intent(this, MyInfoActivity::class.java)
        Log.d(tag, "마이페이지로 이동")
        startActivity(intent)
    } // end infoChange()
} // end activity()