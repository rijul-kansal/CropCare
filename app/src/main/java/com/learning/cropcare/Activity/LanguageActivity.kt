package com.learning.cropcare.Activity

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.databinding.ActivityLanguageBinding

class LanguageActivity : AppCompatActivity() {
    lateinit var binding:ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityLanguageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var startactivityornot=intent.getStringExtra(Constants.START_LANGUAGE_CHOSEN_OR_NOT).toString()
        binding.englishBtn.setOnClickListener {
            val sharedPreference =  getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("language","english")
            editor.putLong("l",100L)
            editor.commit()
            if(startactivityornot=="no")
            {
                finish()
            }
            else
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