package com.intersiot.ohmybank

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.databinding.ActivityInfoChageBinding
import com.intersiot.ohmybank.databinding.ActivityMyInfoBinding
import com.intersiot.ohmybank.model.UserDTO
import org.jetbrains.anko.email

class InfoChageActivity : AppCompatActivity() {
    // layout
    private lateinit var binding: ActivityInfoChageBinding
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    // webview address
    private val SEARCH_ADDRESS_ACTIVITY = 1000
    // user
    private var users = UserDTO()
    private var id = mAuth.currentUser?.email

    private lateinit var post: String
    private lateinit var tax: String

    var tag = "InfoChageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoChageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        editTextConstraints() // EditText 제한조건
        // 이메일은 아이디이므로 고정해야 해서.
        if (id != null) {
            firestore.collection("Users").document(mAuth.currentUser?.email!!).get()
                    .addOnSuccessListener { documentSnapshot ->
                        users = documentSnapshot.toObject<UserDTO>()!!
                        var email = users.id
                        var address = users.address
                        binding.outputEmail.text = email
                        binding.textviewAddress.text = address
                    }
        }
        // 주소 변경
        binding.btnChangeAddress.setOnClickListener {
            Log.d(tag, "주소 변경 선택됨")
            var intent = Intent(this, DaumWebViewActivity::class.java)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }

        Log.e(tag, "테스트3")
        // 수정하기
        binding.btnInfoChage.setOnClickListener {
            // 셋팅
            var address = binding.textviewAddress.text.toString()
            var nameEng = binding.inputNameEng.text.toString()
            var phone = binding.inputPhoneNumber.text.toString()
            var homeNumber = binding.inputHomeNumber.text.toString()
            Log.e(tag, "테스트4")
            firestore.collection("Users").document(id!!).get()
                    .addOnSuccessListener { documentSnapshot ->
                        users = documentSnapshot.toObject<UserDTO>()!!
                        Log.e(tag, "테스트5")
                        // settings
                        users.engname = nameEng
                        users.phone = phone
                        users.home = homeNumber
                        users.address = address
                        Log.e(tag, "테스트55")
                        // 라디오버튼 두개 처리
                        if (binding.checkedHome.isChecked) {
                            post = binding.checkedHome.text.toString()
                            users.post = post
                            Log.d(tag, "우편: 집")
                        } else if (binding.checkedJob.isChecked) {
                            post = binding.checkedJob.text.toString()
                            users.post = post
                            Log.d(tag, "우편: 직장")
                        } else if (binding.checkedNo.isChecked) {
                            post = binding.checkedNo.text.toString()
                            users.post = post
                            Log.d(tag, "우편: 안받음")
                        }
                        Log.d(tag, "테스트6")
                        if (binding.taxYes.isChecked) {
                            tax = binding.taxYes.text.toString()
                            users.tax = tax
                            Log.d(tag, "납세: 한국")
                        } else if (binding.taxNo.isChecked) {
                            tax = binding.taxNo.text.toString()
                            users.tax = tax
                            Log.d(tag, "납세: 한국 아님")
                        }
                        Log.e(tag, "테스트7")
                        // DB 업데이트 : 수정
                        firestore.collection("Users").document(id!!).update(
                                "engname", nameEng,
                                "phone", phone,
                                "home", homeNumber,
                                "address", address,
                                "post", post,
                                "tax", tax
                        )
                        Log.e(tag, "테스트8")
                        val intent = Intent(this, MyInfoActivity::class.java)
                        Log.d(tag, "내 정보 페이지로 이동")
                        startActivity(intent)
                    }
        }
    } // end onCreate()

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == SEARCH_ADDRESS_ACTIVITY && resultCode === Activity.RESULT_OK) {
            // 다음 API에서 받아온 주소 정보를 채워줌
            val data = intent?.extras!!.getString("data")
            if (data != null)
                binding.textviewAddress.text = Editable.Factory.getInstance().newEditable(data)
        }

    }

    // EditText 제한 조건
    private fun editTextConstraints() {
        // 특수문자 제한 : 입력시 빈칸 리턴
        binding.inputNameEng.filters = arrayOf(InputFilter {source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        })
        binding.inputPhoneNumber.filters = arrayOf(InputFilter {source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        })
        binding.inputHomeNumber.filters = arrayOf(InputFilter {source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        })

        // 전화번호 길이 체크
        binding.inputPhoneNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.inputPhoneNumber.length() !in 10..11) {
                    binding.inputPhoneNumber.error = "전화번호를 확인해 주세요"
                }
            }
        })
        binding.inputHomeNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.inputHomeNumber.length() !in 10..11) {
                    binding.inputHomeNumber.error = "전화번호를 확인해 주세요"
                }
            }
        })
    }

}