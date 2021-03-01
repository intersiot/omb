package com.intersiot.ohmybank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.intersiot.ohmybank.databinding.ActivityCreateAccountBinding
import java.util.*

class CreateAccountActivity : AppCompatActivity() {
    private val tag: String = "CreateAccountActivity"
    // layout view
    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    // 계좌 생성버튼 클릭시 이벤트
    fun onCreateAccount(view: View) {
        Log.d(tag, "계좌생성 선택됨")

        createAccount()
    }

    /**
     * random() 함수를 사용해서 계좌번호를 만든다.
     * 1. 중복이 되지 않도록 처리해야함.
     * 2. db에 저장되어야 함 -> firebase
     */
    fun createAccount(): String {
        var rand = Random()
        var numStr = "" // 난수가 저장될 변수
        var len = 11 // 생성할 난수의 길이
        var i = 0

        while (i < len) {
            //0~9 까지 난수 생성
            val ran = Integer.toString(rand.nextInt(10))
            if (numStr != null) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if (!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran
                } else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i -= 1
                }
            }
            i++
        }
        Log.d(tag, "생성된 계좌번호 ${numStr}")
        return numStr
    }
}