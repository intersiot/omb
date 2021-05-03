package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.databinding.ActivityMyInfoBinding
import com.intersiot.ohmybank.model.UserDTO

/**
 * 내 정보 수정 페이지
 */
class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding // layout view
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestroe = FirebaseFirestore.getInstance()

    var tag = "MyInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 유저정보 가져오기
        var id = mAuth.currentUser?.email
        if (id != null) {
            firestroe.collection("Users").document(id!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    var users = documentSnapshot.toObject<UserDTO>()!!
                    var name = users.name
                    var phone = users.phone
                    var homenumber = users.homenumber
                    var address = users.address
                    var post = users.post
                    var tax = users.tax

                    binding.userNameKor.text = name
                    binding.userNameEng.text = name
                    binding.userId.text = id
                    binding.userPhoneNumber.text = phone
                    binding.userHomeNumber.text = homenumber
                    binding.userAddress.text = address
                    binding.userChoiceOne.text = post
                    binding.userChoiceTwo.text = tax
                }
        }

        // 수정하기 클릭했을 때
        binding.btnChage.setOnClickListener {
            val intent = Intent(this, InfoChageActivity::class.java)
            Log.d(tag, "내 정보 수정페이지로 이동")
            startActivity(intent)
        }
    } // end onCreate()
}