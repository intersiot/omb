package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.intersiot.ohmybank.databinding.ActivityDepositBinding
import com.intersiot.ohmybank.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    // layout view
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }   // end onCreate()

    fun onClickOkay(view: View) {
        moveHome()
    }

    private fun moveHome() {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}