package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View

/**
 * 스플래쉬 이미지
 * 앱 시작하면 이미지 로딩되도록 함.
 */
class IntroActivity : AppCompatActivity() {

    var handler: Handler ?= null    // Handler: Runnable을 실행하는 클래스
    var runnable: Runnable? = null  // Runnable: 병렬 실행이 가능한 Thread를 만들어주는 클래스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        /**
         * 안드로이드 앱을 띄우는 Window의 속성을 변경하여
         * 시스템 UI를 숨기고 전체화면으로 표시하는 코드
         */
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    } // end onCreate()

    override fun onResume() {
        super.onResume()
        // Runnable이 실행되면 MainActivity로 이동하는 코드
        runnable = Runnable {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        // Handler를 생성하고 2000m(2초)후 runnalbe을 실행
        handler = Handler()
        handler?.run {
            postDelayed(runnable, 2500)
        }
    } // end onResume()

    override fun onPause() {
        super.onPause()
        // Activity Pause 상태일 때는 runnable도 중단하도록 함
        handler?.removeCallbacks(runnable)
    } // end onPause()
} // end Activity