package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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

    var tag = "MainActivty"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (id != null) {
            firestroe.collection("Users").document(id!!).get()
                    .addOnSuccessListener { documentSnapshot ->
                        users = documentSnapshot.toObject<UserDTO>()!!
                        name = users.name
                        binding.userName.text = name
                        Log.d(tag, "유저 이름 ${name}으로 자동 변경됨, ${users.id}")
                    }
        }
    }

    // 로그아웃
    fun logoutBtn(view: View) {
        Log.d(tag, "로그아웃 성공")
        mAuth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}