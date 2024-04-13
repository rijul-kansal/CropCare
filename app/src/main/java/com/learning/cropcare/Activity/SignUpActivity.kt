package com.learning.cropcare.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learning.cropcare.Utils.BaseActivity
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.ViewModel.AuthenticationViewModel
import com.learning.cropcare.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var email:String
    lateinit var name:String
    lateinit var password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try {
            // connecting view model
            viewModel= ViewModelProvider(this)[AuthenticationViewModel::class.java]

            charByCharDisplay(binding.generaltv.text.toString(),binding.generaltv)

            // redirect to login page
            binding.LoginPageRedirect.setOnClickListener {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }

            binding.mobileAuth.setOnClickListener {
                var intent=Intent(this,MobileNumberSignInActivity::class.java)
                intent.putExtra(Constants.SIGNUP_OR_SIGN_IN,"signUp")
                startActivity(intent)
                finish()
            }
            // sign up
            binding.SignUpBtn.setOnClickListener{
                email=binding.etEmail.text.toString()
                password=binding.etPassword.text.toString()
                name=binding.etName.text.toString()
                if(valid())
                {
                    try {
                        showProgressBar(this)
                        viewModel.SignUp(name,this@SignUpActivity, email, password)
                    }catch (e:Exception) {
                        Log.d("rk",e.message.toString())
                    }
                }

            }
            // result of sign up if successful the call sign in
            viewModel.observerTaskResult().observe(this, Observer { task->
                cancelProgressBar()
                if(task.isSuccessful)
                {
                    Toast(this,"Please verify your email")
                    viewModel.sendVerificationCode()
                }
                else
                {
                    Toast(this,task.exception!!.message.toString())
                }
            })

            viewModel.observerVerifiedEmail().observe(this, Observer{task->
                if(task.isSuccessful)
                {
                    var intent=Intent(this, SignInActivity::class.java)
                    intent.putExtra(Constants.EMAIL,email)
                    intent.putExtra(Constants.PASSWORD,password)
                    intent.putExtra(Constants.NAME,name)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast(this@SignUpActivity,"Email not sent "+task.exception!!.message.toString())
                }
            })
        }catch (e:Exception)
        {
            Log.e("rk",e.message.toString())
        }

    }
    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$")
        return emailRegex.matches(email)
    }
    fun valid():Boolean
    {
        if(email.length>0 && password.length>0 && name.length>0)
        {
            if(isEmailValid(email) && password.length>=6) {
                return true
            }
            else if(password.length<6)
            {
                Toast(this,"password length should be greater then 6")
            }
            else
                Toast(this,"Please Enter valid email")
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


}