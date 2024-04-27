package com.learning.cropcare.ViewModel

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.learning.cropcare.Activity.MobileNumberSignInActivity
import com.learning.cropcare.Activity.SignInActivity
import com.learning.cropcare.Fragment.CropPrediction
import com.learning.cropcare.Fragment.CropYieldPrediction
import com.learning.cropcare.Fragment.FertilizerRecommendation
import com.learning.cropcare.Fragment.History
import com.learning.cropcare.Fragment.PestDetection
import com.learning.cropcare.Fragment.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FireStoreDataBaseViewModel : ViewModel() {
    val db = Firebase.firestore

    var addUserProfileDataResult:MutableLiveData<String> = MutableLiveData()
    fun addUserProfileData(context: Context, data: HashMap<String, String>)
    {
        if(checkForInternet1(context))
        {
            db.collection("users").document(AuthenticationViewModel().getUserId())
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                     addUserProfileDataResult.value="Successfully added data"
                }
                .addOnFailureListener {
                        addUserProfileDataResult.value="Error ${it.message.toString()}"
                }
        }
        else
        {
            addUserProfileDataResult.value="Switch on your internet"
        }
    }
    fun observerAddUserProfileData():LiveData<String> = addUserProfileDataResult


    var getUserProfileDataResult: MutableLiveData<Map<String,String>> = MutableLiveData()
    fun getUserProfileData(context: Context,fragment:Profile)
    {
        if(checkForInternet1(context))
        {
            val docRef = db.collection("users").document(AuthenticationViewModel().getUserId())
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        var res=document.data as Map<String,String>
                      getUserProfileDataResult.value=res
                    } else {
                        fragment.errorFn("Something went wrong try after some time")
                    }
                }
                .addOnFailureListener { exception ->
                    fragment.errorFn(exception.toString())
                }
        }
        else
        {
            fragment.errorFn("switch on your internet")
        }
    }

    fun observerGetUserProfileData():LiveData<Map<String,String>> = getUserProfileDataResult
    fun updateUserPersonalDataIntoDB(context:Context,fragment:Profile,value:HashMap<String,String>)
    {
        if(checkForInternet1(context))
        {
            viewModelScope.launch(Dispatchers.IO)
            {
                val washingtonRef = db.collection("users").document(AuthenticationViewModel().getUserId())
                washingtonRef
                    .update(value as Map<String, String>)
                    .addOnSuccessListener { Log.d("rk","Successfully updated image") }
                    .addOnFailureListener { e-> Log.d("rk",e.message.toString())}
                    .addOnCompleteListener {
                        fragment.errorFn("Successfully updated")
                    }
            }
        }
        else
        {
            fragment.errorFn("Please switch on your internet")
        }


    }

    fun addDataToHistorymain(context: Context, fragment: Fragment, data: HashMap<String, String>)
    {
        if(checkForInternet1(context))
        {
           viewModelScope.launch(Dispatchers.IO)
           {
               val washingtonRef = db.collection("UserHistory").document(AuthenticationViewModel().getUserId())
               washingtonRef.update("array", FieldValue.arrayUnion(data))
                   .addOnFailureListener {
                       Log.d("rk",it.message.toString())
                   }
           }
        }
        else
        {
            when(fragment)
            {
                is CropPrediction->{
                    fragment.errorFn("Switch on your internet please")
                }
                is CropYieldPrediction->{
                    fragment.errorFn("Switch on your internet please")
                }
                is FertilizerRecommendation->{
                    fragment.errorFn("Switch on your internet please")
                }
//                is PestDetection->{
//                    fragment.errorFn("Switch on your internet please")
//                }
            }
        }
    }

    fun addDataToHistoryintial(activity:Activity, data: Map<String,ArrayList<Map<String,String>>>)
    {
        if(checkForInternet1(activity))
        {
            db.collection("UserHistory").document(AuthenticationViewModel().getUserId())
                .set(data, SetOptions.merge())
                .addOnFailureListener {
                    Log.d("rk",it.message.toString())
                }
        }
        else {
            when (activity) {
                is SignInActivity -> {
                    activity.errorFn("Switch on your internet please")
                }

                is MobileNumberSignInActivity -> {
                    activity.errorFn("Switch on your internet please")
                }

            }
        }
    }

    var getUserHistoryDataResult:MutableLiveData<Map<String,ArrayList<Map<String,String>>>> = MutableLiveData()
    fun getUserHistoryData(context: Context,fragment:History)
    {
        if(checkForInternet1(context))
        {
            try {
                val docRef = db.collection("UserHistory").document(AuthenticationViewModel().getUserId())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            var res=document.data as Map<String,ArrayList<Map<String,String>>>
                            getUserHistoryDataResult.value=res
                        } else {
                            fragment.errorFn("Something went wrong try after some time")
                        }
                    }
                    .addOnFailureListener { exception ->
                        fragment.errorFn(exception.toString())
                    }
            }catch (e:Exception)
            {
                Log.d("rk",e.message.toString())
            }

        }
        else
        {
            fragment.errorFn("switch on your internet")
        }
    }
    fun observeUserHistoryData():LiveData<Map<String,ArrayList<Map<String,String>>>> = getUserHistoryDataResult
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