package com.intersiot.ohmybank.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.intersiot.ohmybank.LoginActivity
import com.intersiot.ohmybank.R
import com.intersiot.ohmybank.databinding.FragmentMybankBinding
import com.intersiot.ohmybank.model.UserDTO

class MybankFragment : Fragment() {
    // layout view
    private lateinit var binding: FragmentMybankBinding

    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestroe = FirebaseFirestore.getInstance()

    // 회원정보 가져오기 위한 초석
    var users = UserDTO()
    private var id = mAuth.currentUser?.email
    private var name: String? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mybank, container, false)

//        if (id != null) {
//            firestroe.collection("Users").document(id!!).get()
//                .addOnSuccessListener { documentSnapshot ->
////                    users = documentSnapshot.toObject<UserDTO>()!!
//                    name = users.name
//                    binding.userName.text = name
//                    Log.d(tag, "유저 이름 ${name}으로 자동 변경됨, ${users.id}")
//                }
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
}