package com.learning.cropcare.ViewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.learning.cropcare.Activity.MobileNumberSignInActivity
import com.learning.cropcare.Activity.SignInActivity
import com.learning.cropcare.Activity.SignUpActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class AuthenticationViewModel : ViewModel() {
    private  var mAuth= FirebaseAuth.getInstance()
    private var signUpResult: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    private var signInResult: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    private var verifyTask: MutableLiveData<Task<Void>> = MutableLiveData()
    private var signInMobileNumberResult:MutableLiveData<Task<AuthResult>> = MutableLiveData()



    fun SignUp(name:String, activity: SignUpActivity, email:String, password:String)
    {
        if(checkForInternet1(activity))
        {
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            signUpResult.value = task
                        }
                }
            }catch (e:Exception)
            {
                Log.d("rk",e.message.toString())
            }
        }
        else
        {
            activity.errorFn("Please check your internet connection ")
        }
    }
    fun SignIn(activity: SignInActivity, email: String, password: String)
    {
        if(checkForInternet1(activity))
        {
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            signInResult.value = task
                        }
                }
            }catch (e:Exception)
            {
                Log.d("rk",e.message.toString())
            }
        }
        else
        {
            activity.errorFn("Please check your internet connection ")
        }

    }
    fun sendVerificationCode()
    {
            viewModelScope.launch(Dispatchers.IO) {
                mAuth.currentUser!!.sendEmailVerification()
                    .addOnCompleteListener { task ->
                        verifyTask.value = task
                        if (task.isSuccessful) {
                            Log.d("rk", "email sent")
                        }
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
    fun signUpUsingMobileNumber(activity:MobileNumberSignInActivity,phoneNumber:String,callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    {
        if(checkForInternet1(activity))
        {
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    val options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(callbacks)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                    Log.d("GFG" , "Auth started")
                }
            }catch (e:Exception)
            {
                Log.d("rk",e.message.toString())
            }
        }
        else
        {
            activity.errorFn("Please check your internet connection ")
        }

    }
    fun signInWithPhoneAuthCredential(activity:MobileNumberSignInActivity,credential: PhoneAuthCredential) {
        if(checkForInternet1(activity))
        {
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(activity) { task ->
                            signInMobileNumberResult.value=task
                        }
                }
            }catch (e:Exception)
            {
                Log.d("rk",e.message.toString())
            }
        }
        else
        {
            activity.errorFn("Please check your internet connection ")
        }

    }
    fun observerSignInWithPhoneAuthCred(): LiveData<Task<AuthResult>> = signInMobileNumberResult
    private fun checkForInternet1(context: Context): Boolean
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
