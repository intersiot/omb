package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    // 유저정보
    private var id = mAuth.currentUser?.email

    var tag = "MyInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 수정하기 클릭했을 때
        binding.btnChage.setOnClickListener {
            val intent = Intent(this, InfoChageActivity::class.java)
            Log.d(tag, "내 정보 수정페이지로 이동")
            startActivity(intent)
        }

        // 회원탈퇴 클릭했을 때
        binding.btnIdDelete.setOnClickListener {
            mAuth.currentUser!!.delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.e(tag, "${id} 삭제됨")
                    mAuth.signOut()
                } else {
                    Log.e(tag, "회원탈퇴 실패함")
                }
            }
        }
    } // end onCreate()

    override fun onResume() {
        super.onResume()

        // 유저정보 가져오기
        if (id != null) {
            firestroe.collection("Users").document(id!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    var users = documentSnapshot.toObject<UserDTO>()!!
                    var name = users.name
                    var nameEng = users.engname
                    var phone = users.phone
                    var homenumber = users.home
                    var address = users.address
                    var post = users.post
                    var tax = users.tax

                    binding.userNameKor.text = name
                    binding.userNameEng.text = nameEng
                    binding.userId.text = id
                    binding.userPhoneNumber.text = phone
                    binding.userHomeNumber.text = homenumber
                    binding.userAddress.text = address
                    binding.userChoiceOne.text = post
                    binding.userChoiceTwo.text = tax
                }
        }
    }
}