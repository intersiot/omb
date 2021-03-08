package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.intersiot.ohmybank.databinding.ActivityCreateAccountBinding
import com.intersiot.ohmybank.model.UserDTO
import java.util.*

class CreateAccountActivity : AppCompatActivity() {
    private val tag: String = "CreateAccountActivity"
    // layout view
    private lateinit var binding: ActivityCreateAccountBinding
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    } // end onCreate()

    // 계좌 생성버튼 클릭시 이벤트
    fun onCreateAccount(view: View) {
        Log.d(tag, "계좌생성 선택됨")
        createAccount()
    } // onCreateAccount()

    /**
     * random() 함수를 사용해서 계좌번호를 만든다.
     * db에 저장되어야 함 -> firebase
     */
    fun createAccount(): String {
        val random = Random()
        var len = 7 // 생성할 난수의 길이
        var numStr = "" // 난수가 저장될 변수
//        var i = 0

        if (numStr.isEmpty()) {
            for (i in 0..len) {
                // 0 ~ 9까지 난수 생성
                val ran = random.nextInt(10).toString()
                numStr += ran
            }
        }
        var account = "1002$numStr"
        Log.d(tag, "생성된 계좌번호: $account")

        // 유저 아이디 가져오기
        email = mAuth?.currentUser?.email!!
        // 유저정보 받아오기
        firestore.collection("Users").document(email).get().addOnCompleteListener {
            var user = it.result?.toObject(UserDTO::class.java)!!
            user.account = account
        }
        // 유저 데이터베이스 갱신
        firestore.collection("Users").document(email).update("account", account)
        finish()

        return numStr
    } // end createAccount()

} // end activity()