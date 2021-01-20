package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.intersiot.ohmybank.databinding.ActivityJoinBinding
import com.intersiot.ohmybank.model.UserDTO
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {
    // layout view
    private lateinit var binding: ActivityJoinBinding
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    var checkId = false
    var checkPw = false

    var tag = "JoinActivty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.e("JoinActivity", "${intent.getBooleanExtra("promotion", false)}")

        editTextConstraints()   // EditText 제한조건 사용
    } // end onCreate()

    fun onJoinClick(view: View) {
        var userName = binding.userName.text.toString()
        var userHp = binding.userPhone.text.toString()
        var email = binding.userId.text.toString()
        var password = binding.userPassword.text.toString()
        var password_ck = binding.userPwChecked.text.toString()
        var promotionCk = intent.getBooleanExtra("promotion", false)

        // 예외처리
        // 1. 내용을 입력하지 않았을 경우
        if (userName.isEmpty() || userHp.isEmpty() || email.isEmpty()
            || password.isEmpty() || password_ck.isEmpty()) {
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. 양식과 맞지 않음
        if (!checkId || !checkPw) {
            Toast.makeText(this, "양식을 확인해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 비밀번호와 비밀번호 체크 값이 같을 경우
        if (password == password_ck) {
            // 이메일 회원가입
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                if (it.isSuccessful) { // 성공
                    var user = UserDTO()
                    user.id = email
                    user.uid = mAuth?.uid
                    user.name = userName
                    user.phone = userHp
                    user.promotion = promotionCk
                    // cloud firestore에 유저 등록
                    firestore.collection("Users").document(email)
                            .set(user).addOnCompleteListener {
                        if (it.isComplete) {
                            Log.e("JoinActivity", "회원가입 성공 ${email}")
                            startActivity(Intent(this, LoginActivity::class.java))
                            mAuth.signOut() // 사용자 로그아웃
                            finish()
                        }
                    }
                } else { // 실패
                    Toast.makeText(this, "가입오류: {${it.exception}", Toast.LENGTH_SHORT).show()
                    Log.e("JoinActivity", "가입오류. ${it.exception}")
                }
            }
        } else { // 비밀번호와 비밀번호 채크가 일치하지 않을 경우
            Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
    } // end onJoinClick()

    // 입력 제한 조건
    private fun editTextConstraints() {
        binding.userId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val id = binding.userId.text.toString()
                // 이메일 형식인지 체크
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
                    binding.userId.error = "이메일 형식의 아이디를 입력해주세요."
                    checkId = false
                } else
                    checkId = true
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        binding.userPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val pattern = "\\w+"
                val pw = binding.userPassword.text.toString()
                // 특수문자가 들어가는지 체크
                if (Pattern.matches(pattern, pw))
                    binding.userPassword.error = "영문 또는 숫자와 특수문자 필수\n"
                // 비밀번호의 길이 체크
                if (pw.length !in 6..20)
                    binding.userPassword.error = "비밀번호는 6~20로 생성해주세요"
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        binding.userPwChecked.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // 비밀번호와 비밀번호 확인의 값이 동일한지 체크
                if (binding.userPassword.text.toString() != binding.userPwChecked.text.toString()) {
                    binding.userPwChecked.error = "비밀번호가 일치하지 않습니다."
                    checkPw = false
                } else
                    checkPw = true
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    } // end editTextConstraints()

}