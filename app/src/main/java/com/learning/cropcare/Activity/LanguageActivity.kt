package com.learning.cropcare.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learning.cropcare.R
import com.learning.cropcare.databinding.ActivityLanguageBinding

class LanguageActivity : AppCompatActivity() {
    lateinit var binding:ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityLanguageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.englishBtn.setOnClickListener {
            startActivity(Intent(this,WalkthroughScreen::class.java))
        }
        binding.hindiBtn.setOnClickListener {
            startActivity(Intent(this,WalkthroughScreen::class.java))
        }

    }
}