package com.learning.cropcare.Utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.learning.cropcare.R

open class BaseActivity : AppCompatActivity() {
    var dialog:Dialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
    fun Toast(constext: Context, message:String)
    {
        android.widget.Toast.makeText(constext,message, android.widget.Toast.LENGTH_LONG).show()
    }

    fun showProgressBar(context: Context) {
        dialog = Dialog(context)
        dialog!!.setContentView(R.layout.progress_bar)
        dialog!!.show()
    }

    fun cancelProgressBar()
    {
        if(dialog!=null)
        {
            dialog!!.dismiss()
            dialog=null
        }
    }
}