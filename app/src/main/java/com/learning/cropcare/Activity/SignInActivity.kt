package com.learning.cropcare.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.learning.cropcare.Utils.BaseActivity
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.ViewModel.AuthenticationViewModel
import com.learning.cropcare.ViewModel.FireStoreDataBaseViewModel
import com.learning.cropcare.ViewModel.StorageViewModel
import com.learning.cropcare.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {
    lateinit var binding: ActivitySignInBinding

    lateinit var viewModel: AuthenticationViewModel
    lateinit var viewModel1: FireStoreDataBaseViewModel
    lateinit var viewModel2: StorageViewModel
    lateinit var email:String
    lateinit var password:String
    lateinit var name:String
    lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        @SuppressLint("SuspiciousIndentation")
        binding= ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        charByCharDisplay(binding.signInGeneral.text.toString(),binding.signInGeneral)

        viewModel= ViewModelProvider(this)[AuthenticationViewModel::class.java]
        viewModel1= ViewModelProvider(this)[FireStoreDataBaseViewModel::class.java]
        try {
            binding.mobileAuth.setOnClickListener {
                var intent=Intent(this,MobileNumberSignInActivity::class.java)
                intent.putExtra(Constants.SIGNUP_OR_SIGN_IN,"signIn")
                startActivity(intent)
            }
            email=""
            password=""
            if(intent.hasExtra(Constants.PASSWORD))
                password=intent.getStringExtra(Constants.PASSWORD).toString()
            if(intent.hasExtra(Constants.EMAIL))
                email=intent.getStringExtra(Constants.EMAIL).toString()
            if(intent.hasExtra(Constants.NAME))
            {
                name=intent.getStringExtra(Constants.NAME).toString()
                val hash= hashMapOf("name" to name,"email" to email,"mobilenumber" to "","image" to "")
                viewModel1.addUserProfileData(this,hash)
                var arr : MutableMap<String,ArrayList<Map<String,String>>> = HashMap()
                val arrayList = ArrayList<Map<String, String>>()
                val innerMap = HashMap<String, String>()
                innerMap["innerKey1"] = "innerValue1"
                arrayList.add(innerMap)
                arr["array"] = arrayList
                viewModel1.addDataToHistoryintial(this,arr)
            }
            binding.etEmail.setText(email)
            binding.etPassword.setText(password)

            // current user Info
            user=FirebaseAuth.getInstance()


            // sign In
            binding.SignInBtn.setOnClickListener{
                email=binding.etEmail.text.toString()
                password=binding.etPassword.text.toString()
                if(valid())
                {
                    if(user.currentUser==null)
                    {
                        showProgressBar(this)
                        viewModel.SignIn(this@SignInActivity, email, password)
                    }
                    else {
                        if (user.currentUser!!.isEmailVerified) {
                            showProgressBar(this)
                            viewModel.SignIn(this@SignInActivity, email, password)
                        } else {
                            user.currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                                if (reloadTask.isSuccessful) {
                                    user.currentUser?.getIdToken(true)
                                        ?.addOnCompleteListener { tokenTask ->
                                            if (tokenTask.isSuccessful) {
                                                if (user.currentUser!!.isEmailVerified) {
                                                    showProgressBar(this)
                                                    viewModel.SignIn(
                                                        this@SignInActivity,
                                                        email,
                                                        password
                                                    )
                                                } else {
                                                    viewModel.sendVerificationCode()
                                                    Toast(this, "Please verify your email first")
                                                }
                                            } else {
                                                Log.e(
                                                    "rk",
                                                    "Error refreshing token: ${tokenTask.exception?.message}"
                                                )
                                            }
                                        }
                                } else {
                                    Log.e(
                                        "rk",
                                        "Error reloading user: ${reloadTask.exception?.message}"
                                    )
                                }
                            }
                        }
                    }
                }
            }


            // result of sign In if successful the call sign in
            viewModel.observerTaskResultLogin().observe(this, Observer { task->
                cancelProgressBar()
                if(task.isSuccessful)
                {
                    Toast(this,"user Login In successfully")
                    var i=Intent(this,MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)
                    finish()
                }
                else
                {
                    Toast(this,task.exception!!.message.toString())
                }
            })

        }catch (e:Exception)
        {
            Log.e("rk",e.message.toString())
        }
    }
    fun valid():Boolean
    {
        if(email.length>0 && password.length>0)
        {
            return true
        }
        else if(email.length==0)
        {
            Toast(this,"Please Enter your email")
        }
        else if(password.length==0)
        {
            Toast(this,"Please Enter your password")
        }
        else
        {
            Toast(this,"Please Enter your name")
        }
        return false
    }

    fun charByCharDisplay(textToDisplay:String,textView: TextView) {
        val handler = Handler(Looper.getMainLooper())
        var currentIndex=0
        handler.post(object : Runnable {
            override fun run() {
                if (currentIndex < textToDisplay.length) {
                    textView.text = textToDisplay.substring(0, currentIndex + 1)
                    currentIndex++
                    handler.postDelayed(this, 300)
                }
            }
        })
    }

    fun errorFn(message:String)
    {
        cancelProgressBar()
        Toast(this,message)
    }
}