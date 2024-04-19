package com.learning.cropcare.Activity

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    override fun onBackPressed() {
        if (isOnlyOneActivityInStack()) {
            showBackBtnDialog()
        } else {
            Log.d("rk", "More than one activity in the stack")
            super.onBackPressed()
        }
    }
    private fun isOnlyOneActivityInStack(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.appTasks
        for (task in tasks) {
            val taskInfo = task.taskInfo
            if (taskInfo.numActivities == 1) {
                return true
            }
        }
        return false
    }

    fun showBackBtnDialog()
    {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Want to exit the App")
            .setPositiveButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setNegativeButton("Exit") { dialog, which ->
                finish()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}