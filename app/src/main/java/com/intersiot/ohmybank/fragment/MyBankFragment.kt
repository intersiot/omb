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
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.CreateAccountActivity
import com.intersiot.ohmybank.DepositActivity
import com.intersiot.ohmybank.LoginActivity
import com.intersiot.ohmybank.databinding.FragmentMyBankBinding
import com.intersiot.ohmybank.model.UserDTO

class MyBankFragment : Fragment() {
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestroe = FirebaseFirestore.getInstance()

    // 회원정보 가져오기 위한 초석
    var users = UserDTO()
    private var id = mAuth.currentUser?.email
    private var name: String? = null
    private var account: String? = null
    private var cache: Int? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMyBankBinding.inflate(inflater, container, false)

        if (id != null) {
            firestroe.collection("Users").document(id!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    users = documentSnapshot.toObject<UserDTO>()!!
                    name = users.name
                    account = users.account
                    cache = users.cache
                    binding.userName.text = name
                    binding.accountView.text = account
                    binding.accoutName.text = "생성된 계좌번호:"
                    binding.moneyView.text = cache.toString()
                    Log.d(tag, "유저 이름 ${name}으로 자동 변경됨, ${users.id}")
                    Log.d(tag, "생성된 계좌번호: $account")
                    Log.d(tag, "현재 보유중인 금액: $cache")
                }
        }

        // 계좌 이체 버튼 클릭시
        binding.btnTransfer.setOnClickListener {
            Log.d(tag, "계좌이체 버튼 선택됨")
        }

        // 계좌 생성 버튼 클릭시
        binding.layoutCreateAccount.setOnClickListener {
//            Log.d(tag, "계좌생성 선택됨")
            /**
             * 랜덤함수 사용해서 계좌 만들기 (중복제거)
             * CreateAccountActivity로 넘어가서 주의사항 안내하고 생성
             * 완료 후 생성된 계좌번호 보여주고 -> 메인액티비티로 이동
              */
            val intent = Intent(context, CreateAccountActivity::class.java)
            Log.d(tag, "계좌생성 액티비티로 이동")
            startActivity(intent)
        }

        // 내 통장 입금 클릭시
        binding.layoutDeposit.setOnClickListener {
            val intent = Intent(context, DepositActivity::class.java)
            Log.d(tag, "내통장 입금 액티비티로 이동")
            startActivity(intent)
        }

        // 로그아웃 버튼 클릭시
        binding.btnLogout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java);
            Log.d(tag, "로그아웃 성공")
            mAuth.signOut()
            startActivity(intent)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}