package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.databinding.ActivityDepositBinding
import com.intersiot.ohmybank.model.TransactDTO
import com.intersiot.ohmybank.model.UserDTO

class DepositActivity : AppCompatActivity() {
    private val tag: String = "DepositActivity"
    // layout view
    private lateinit var binding: ActivityDepositBinding
    // firebase 인증
    private var firestore = FirebaseFirestore.getInstance()
    private var mAuth = FirebaseAuth.getInstance()
    private var transactDTO = TransactDTO()
    private var payment = 0
    private var account: String ?= null
    private var myAccount: String ?= null
    private var myCache = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepositBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // DB에서 유저 데이터 가져오기
        var id = mAuth.currentUser?.email
        if (id != null) {
            firestore.collection("Users").document(id!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    var users = documentSnapshot.toObject<UserDTO>()!!
                    myAccount = users.account
                    myCache = users.cache!!
                    // 가져온 데이터로 레이아웃 내용 바꾸기
                    binding.accountViewNumber.text = myAccount
                    binding.accountViewCache.text = myCache.toString()
                    Log.d(tag, "계좌번호: ${myAccount}, 출금가능액: ${myCache}로 변경됨")
                }
        }

        // 이체 버튼 두개 처음에는 안보이게 하기
        binding.inputAccountLayout.visibility = View.INVISIBLE  // 계좌번호 입력 안보이게 하기
        binding.btnDepositLayout.visibility = View.INVISIBLE    // btnDepositLayout 안보이게

        // 로고 클릭시
        binding.logo.setOnClickListener {
            moveHome()
        }

        // 로그아웃 버튼 클릭시
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            Log.d(tag, "로그아웃 성공")
            mAuth.signOut()
            startActivity(intent)
        }
    } // end onCreate()

    // 확인 버튼 클릭 이벤트 처리
    fun onClickOkay(view: View) {
        if (binding.inputCache.text.isNotEmpty()) {
            binding.btnOkay.visibility = View.INVISIBLE             // 확인 버튼 숨기고
            binding.inputAccountLayout.visibility = View.VISIBLE    // 계좌번호 입력버튼 나타남
            binding.btnDepositLayout.visibility = View.VISIBLE      // 이체 버튼 레이아웃 나타남
        } else {
            Toast.makeText(this, "금액을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    // 내 통장 입금
    fun onDeposit(view: View) {
        var inputCache = binding.inputCache.text // 입력받은 cache 값
        payment = Integer.parseInt(inputCache.toString())
        var id = mAuth.currentUser?.email    // 유저 아이디 가져오기
        firestore.collection("Users").document(id!!).get().addOnSuccessListener { documentSnapshot ->
            var users = documentSnapshot.toObject<UserDTO>()!!
            myAccount = users.account
            myCache = users.cache!!

            if (myCache + payment.toLong() > 2000000000) {  // 캐시가 20억을 넘을 경우 20억으로 고정: Int 형 값 넘어가는 오류 방지
                myCache == 2000000000
            } else {    // 그 외는 입금한 금액만큼만
                myCache += payment
            }
            // settings
            var account = myAccount
            transactDTO.id = id
            transactDTO.account = account
            transactDTO.payment = payment
            transactDTO.cache = myCache
            transactDTO.timestamp = System.currentTimeMillis()
            // DB Users Update() : 수정
            mAuth.currentUser?.email?.let {
                firestore.collection("Users").document(it)
                        .update("cache", FieldValue.increment(payment.toLong()))    // increment(paymnet)면 증가한다는 의미
            }
            // DB Transact
            firestore.collection("Transact").document()
                    .set(transactDTO).addOnCompleteListener {
                        if (it.isComplete) {    // 내 통장에 입금 성공
                            var intent = Intent(applicationContext, ResultActivity::class.java)
                            // 데이터 전달
                            intent.putExtra("id", id)
                            intent.putExtra("account", account)
                            intent.putExtra("payment", payment)
                            startActivity(intent)
                            Log.d(tag, "$account 로 $payment 원 입금 성공! 현재 통장 잔액: $myCache")
                            Toast.makeText(this, "내 통장 입금 성공!", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    // 계좌 이체
    fun onDepositTrans(view: View) {
        if (binding.inputAccount.text.isNotEmpty()) {
            var inputCache = binding.inputCache.text // 입력받은 cache 값
            payment = Integer.parseInt(inputCache.toString())
            var id = mAuth.currentUser?.email    // 유저 아이디 가져오기
            firestore.collection("Users").document(id!!).get().addOnSuccessListener { documentSnapshot ->
                var users = documentSnapshot.toObject<UserDTO>()!!
                myCache = users.cache!!

                if (myCache - payment.toLong() < 0) {  // 캐시가 0보다 적을 경우 0으로 고정: Int 형 값 넘어가는 오류 방지
                    myCache == 0
                } else {    // 그 외는 입금한 금액만큼만
                    myCache -= payment
                }
                // settings
                account = binding.inputAccount.text.toString()
                transactDTO.id = id
                transactDTO.account = account
                transactDTO.payment = payment
                transactDTO.cache = myCache
                transactDTO.timestamp = System.currentTimeMillis()
                // DB Users Update() : 수정
                mAuth.currentUser?.email?.let {
                    firestore.collection("Users").document(it)
                            .update("cache", FieldValue.increment(-payment.toLong()))    // increment(paymnet)면 증가한다는 의미
                }
                // DB Transact
                firestore.collection("Transact").document()
                        .set(transactDTO).addOnCompleteListener {
                            if (it.isComplete) {    // 계좌이체 성공
                                var intent = Intent(applicationContext, ResultActivity::class.java)
                                // 데이터 전달
                                intent.putExtra("id", id)
                                intent.putExtra("account", account)
                                intent.putExtra("payment", payment)
                                startActivity(intent)
                                Log.d(tag, "$account 로 $payment 원 입금 성공! 현재 통장 잔액: $myCache")
                                Toast.makeText(this, "계좌이체 성공!", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        } else {
            Toast.makeText(this, "이체하고자 하는 계좌번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun moveHome() {
        Log.d(tag, "메인 액티비티로 이동")
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
} // end Activity