package com.learning.cropcare.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chaos.view.PinView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.learning.cropcare.R
import com.learning.cropcare.Utils.BaseActivity
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.ViewModel.AuthenticationViewModel
import com.learning.cropcare.databinding.ActivityMobileNumberSignInBinding

class MobileNumberSignInActivity : BaseActivity() {
    lateinit var viewModel: AuthenticationViewModel
    lateinit var binding:ActivityMobileNumberSignInBinding
    lateinit var storedVerificationId:String
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    var otpDialog:Dialog?=null
    var name:String?=null
    var countryCode:String?=null
    var mobileNumber:String?=null
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var signUpOrSignIn:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMobileNumberSignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        try{
            signUpOrSignIn=intent.getStringExtra(Constants.SIGNUP_OR_SIGN_IN).toString()

            if(signUpOrSignIn=="signIn")
            {
                binding.generaltv.text="Sign in to continue"
                binding.SignUpBtn.text="Sign In"
            }
            else
            {
                binding.generaltv.text="Sign up to continue"
                binding.SignUpBtn.text="Sign Up"
            }
            binding.LoginPageRedirect.setOnClickListener {
                startActivity(Intent(this,SignInActivity::class.java))
                finish()
            }
            binding.mobileAuth.setOnClickListener {
                startActivity(Intent(this,SignUpActivity::class.java))
                finish()

            }
            viewModel= ViewModelProvider(this)[AuthenticationViewModel::class.java]
            charByCharDisplay(binding.generaltv.text.toString(),binding.generaltv)
            callBackForAuth()
            binding.SignUpBtn.setOnClickListener {
                name=binding.etName.text.toString()
                mobileNumber=binding.etMobileNo.text.toString()
                countryCode=binding.countryCode.selectedCountryCode.toString()
                Log.d("rk",countryCode+mobileNumber)
                if(valid())
                {
                    showProgressBar(this)
                    viewModel.signUpUsingMobileNumber(this, "+"+countryCode+mobileNumber!!,callbacks)
                }
            }
            viewModel.observerSignInWithPhoneAuthCred().observe(this, Observer { task->
                cancelProgressBar()
                if(task.isSuccessful)
                {
                    otpDialog!!.dismiss()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish();
                }
                else
                {
                    Log.d("rk","failed")
                    Log.d("rk",task.exception!!.message.toString())
                }
             })

        }catch (e:Exception)
        {
            Log.d("rk",e.message.toString())
        }
    }

    fun callBackForAuth()
    {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
                Log.d("rk" , "onVerificationCompleted Success")
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("rk" , "onVerificationFailed  $e")

            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                cancelProgressBar()
                Log.d("rk","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                showOtpPopup()
            }
        }
    }
    fun showOtpPopup() {
        try {
            otpDialog= Dialog(this)
            val view: View = LayoutInflater.from(this).inflate(R.layout.popup_otp, null)
            val submitOtpButton = view.findViewById<TextView>(R.id.Enter_otp_btn)
            otpDialog!!.setContentView(view)
            otpDialog!!.setCanceledOnTouchOutside(false)
            val window = otpDialog!!.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setGravity(Gravity.BOTTOM)


            submitOtpButton.setOnClickListener {
                showProgressBar(this)
                val otp = view.findViewById<PinView>(R.id.pinview).text.toString()
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId, otp)
                viewModel.signInWithPhoneAuthCredential(this,credential)
            }
            otpDialog!!.show()
        } catch (e: Exception) {
            Log.d("rk", e.message.toString())
        }
    }
    fun valid():Boolean
    {
        if(mobileNumber!!.length>0 && name!!.length>0)
        {
            return true
        }
        else if(mobileNumber!!.length==0)
        {
            Toast(this,"Please Enter your mobile number")
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