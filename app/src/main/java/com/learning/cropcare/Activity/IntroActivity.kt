package com.learning.cropcare.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learning.cropcare.R
import com.learning.cropcare.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.SignUpBtn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.SignINBtn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }
}