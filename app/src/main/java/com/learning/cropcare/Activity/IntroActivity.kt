package com.learning.cropcare.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learning.cropcare.R
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding:ActivityIntroBinding
    lateinit var languageChoosen:String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val sharedPreference =  getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE)
        languageChoosen= sharedPreference.getString("language","english").toString()

        if(languageChoosen=="hindi")
        {
            binding.textView.setText(resources.getString(R.string.intro_h))
        }
        binding.SignUpBtn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.SignINBtn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }
}