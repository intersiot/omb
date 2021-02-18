package com.intersiot.ohmybank.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intersiot.ohmybank.R
import kotlin.random.Random

class AllMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_menu, container, false)
    }

    // 계좌 이체 버튼
    fun accountTransfer(view: View) {
        
    }

    // 계좌 생성
    fun btnAccountCreate(view: View) {
        // 랜덤함수 사용해서 계좌 만들기 (중복제거)
    }

    // 로그아웃 버튼
    fun logoutBtn(view: View) {

    }
}