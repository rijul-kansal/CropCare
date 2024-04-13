package com.learning.cropcare.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learning.cropcare.R
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.databinding.ActivityLanguageBinding

class LanguageActivity : AppCompatActivity() {
    lateinit var binding:ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityLanguageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.englishBtn.setOnClickListener {
            val sharedPreference =  getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("language","english")
            editor.putLong("l",100L)
            editor.commit()
            startActivity(Intent(this,WalkthroughScreen::class.java))
        }
        binding.hindiBtn.setOnClickListener {
            val sharedPreference =  getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("language","hindi")
            editor.putLong("l",100L)
            editor.commit()
            startActivity(Intent(this,WalkthroughScreen::class.java))
        }

    }
}