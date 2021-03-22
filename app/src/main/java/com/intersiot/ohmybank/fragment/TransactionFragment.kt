package com.intersiot.ohmybank.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.intersiot.ohmybank.R
import com.intersiot.ohmybank.databinding.FragmentTransactionBinding
import com.intersiot.ohmybank.model.UserDTO
import java.nio.file.attribute.UserDefinedFileAttributeView

class TransactionFragment : Fragment() {
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()
    private var firestroe = FirebaseFirestore.getInstance()
    // 유저 정보 가져오기
    private var users = UserDTO()
    private var id = mAuth.currentUser?.email
    private var account: String? = null
    private var cache: Int? = null
    // 리사이클러뷰
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentTransactionBinding.inflate(inflater, container, false)

        var moneyView = binding.moneyView.text
        cache = Integer.parseInt(moneyView.toString())

        if (id != null) {
            firestroe.collection("Users").document(id!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    users = documentSnapshot.toObject<UserDTO>()!!
                    account = users.account
                    cache = users.cache
                    binding.accountView.text = account
                    binding.moneyView.text = cache.toString()
                    Log.d(tag, "생성된 계좌번호: ${account}로 변경됨")
                    Log.d(tag, "보유금액: ${cache}로 변경됨")
                }
        }

        return binding.root
    }

    /**
     * 리사이클러뷰를 이용해서 거래내역이 업데이트 되도록 해야 함.
     */
}