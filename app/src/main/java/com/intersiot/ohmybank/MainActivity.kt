package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.intersiot.ohmybank.adapter.ViewPagerAdapter
import com.intersiot.ohmybank.databinding.ActivityMainBinding
import com.intersiot.ohmybank.fragment.AllMenuFragment
import com.intersiot.ohmybank.fragment.FinancialFragment
import com.intersiot.ohmybank.fragment.MybankFragment
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

    var tag = "MainActivty"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        configureBottomNavigation()
//        if (id != null) {
//            firestroe.collection("Users").document(id!!).get()
//                    .addOnSuccessListener { documentSnapshot ->
//                        users = documentSnapshot.toObject<UserDTO>()!!
//                        name = users.name
//                        binding.userName.text = name
//                        Log.d(tag, "유저 이름 ${name}으로 자동 변경됨, ${users.id}")
//                    }
//        }
    }

//    // 계좌이체 버튼 클릭
//    fun accountTransfer(view: View) {
//        Log.d(tag, "계좌이체 버튼 선택됨")
//    }
//
//    // 로그아웃
//    fun logoutBtn(view: View) {
//        Log.d(tag, "로그아웃 성공")
//        mAuth.signOut()
//        startActivity(Intent(this, LoginActivity::class.java))
//    }

    private fun configureBottomNavigation() {
        // 메인 액티비티 레이아웃의 뷰페이저와 어댑터 연결
        val adapter = ViewPagerAdapter(supportFragmentManager, 3)
        adapter.addFragment(MybankFragment())
        adapter.addFragment(FinancialFragment())
        adapter.addFragment(AllMenuFragment())
        binding.viewpager.adapter = adapter
        // 하단 네비게이션과 뷰페이저 연결
        binding.layoutNavigation.setupWithViewPager(binding.viewpager)


    }
}