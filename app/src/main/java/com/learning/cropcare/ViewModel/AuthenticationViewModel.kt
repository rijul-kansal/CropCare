package com.learning.cropcare.ViewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import java.util.concurrent.TimeUnit


class AuthenticationViewModel : ViewModel() {
    private  var mAuth= FirebaseAuth.getInstance()
    private var signUpResult: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    private var signInResult: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    private var verifyTask: MutableLiveData<Task<Void>> = MutableLiveData()

    private var signInMobileNumberResult:MutableLiveData<Task<AuthResult>> = MutableLiveData()
    fun SignUp(name:String, activity: Activity, email:String, password:String)
    {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    signUpResult.value = task
                }
    }
    fun SignIn(activity: Activity, email: String, password: String)
    {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    signInResult.value = task
                }
    }

    fun sendVerificationCode()
    {
            mAuth.currentUser!!.sendEmailVerification()
                .addOnCompleteListener { task ->
                    verifyTask.value = task
                    if (task.isSuccessful) {
                        Log.d("rk", "email sent")
                    }
                }
    }
    fun updateProfileInAuth(name:String)
    {
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }

            mAuth.currentUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("rk", "User profile updated.")
                    }
                }
    }
    fun SignOut() {
        mAuth.signOut()
    }
    fun observerTaskResult(): LiveData<Task<AuthResult>> = signUpResult
    fun observerTaskResultLogin(): LiveData<Task<AuthResult>> = signInResult
    fun observerVerifiedEmail(): LiveData<Task<Void>> = verifyTask

    fun getUserId():String
    {
        if(mAuth.currentUser!=null)
        {
            return mAuth.currentUser!!.uid
        }
        return ""
    }
    fun signUpUsingMobileNumber(activity:Activity,phoneNumber:String,callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    {
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            Log.d("GFG" , "Auth started")
    }

    fun signInWithPhoneAuthCredential(activity:Activity,credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                signInMobileNumberResult.value=task
            }
    }

    fun observerSignInWithPhoneAuthCred(): LiveData<Task<AuthResult>> = signInMobileNumberResult
}
