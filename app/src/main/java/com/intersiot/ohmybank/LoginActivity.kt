package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.intersiot.ohmybank.databinding.ActivityLoginBinding
import com.intersiot.ohmybank.model.UserDTO

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 1000   // 구글 로그인 확인 코드
    private var mAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private lateinit var googleSignInClient : GoogleSignInClient
            // lateinit: 초기화를 뒤로 미루는 키워드
    private lateinit var binding: ActivityLoginBinding
    var tag: String = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_login)
    } // end onCreate()

    // 이메일 로그인 버튼 클릭시 이벤트 처리
    fun emailLogin(view: View) {
        var id = binding.loginId.text.toString()
        var password = binding.loginPw.text.toString()

        /**
         * 예외처리
         * 1. 아이디 입력 안했을 때
         * 2. 비밀번호 입력 안했을 때
         */
        if (id.isEmpty() || password.isEmpty()) {
            Log.d(tag, "로그인 양식과 맞지 않음")
            Toast.makeText(this, "아이디 또는 패스워드를 입력해주세요.", 
                Toast.LENGTH_SHORT).show()
            return
        }
        
        mAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener { 
            if (it.isSuccessful) {  // 로그인 성공
                Log.d(tag, "로그인 성공")
                moveMain()
            } else {    // 로그인 실패
                binding.loginPw.text = null
                Log.d(tag, "로그인 실패")
                Toast.makeText(this, "아이디 또는 패스워드를 확인해주세요.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    } // end emailLogin()
    
    // 구글 로그인 버튼 클릭시 이벤트 처리
    fun googleLogin(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        Log.d(tag, "구글 로그인 버튼 클릭됨")
    } // end googleLogin()

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.d(tag, "구글 로그인 실패 ${e.printStackTrace()}")
                e.printStackTrace()
            }
        }
    } // end onActivityResult()

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {    // 구글 로그인 성공
                    firestore.collection("Users").document(mAuth.currentUser?.email!!)
                        .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                            if (documentSnapshot?.data == null) {   // 첫로그인(회원가입) 경우 아이디 생성
                                var email = mAuth.currentUser?.email!!
                                var user = UserDTO()
                                user.id = email
                                firestore.collection("Users")
                                    .document(email).set(user)
                            } else {    // 회원가입한 상태인 경우 액티비티 이동
                                moveMain()
                            }
                        }
                } else {    // 구글 로그인 실패
                    Log.d(tag, "구글 로그인 실패")
                    Toast.makeText(this, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    } // end firebaseAuthWithGoogle()

    // 회원 가입 버튼 클릭시
    fun createUser(view: View) {
        Log.d(tag, "회원가입 버튼 클릭됨")
        var intent = Intent(this, ConsentActivity::class.java)
        startActivity(intent)
    } // end createUser()

    // 회원찾기 버튼 클릭시
    fun findUser(view: View) {
        Log.d(tag, "회원찾기 버튼 클릭됨")
    } // end findUser()

    // 메인 액티비티로 이동
    private fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    } // end moveMain()
}