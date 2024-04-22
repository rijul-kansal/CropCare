package com.learning.cropcare.ViewModel


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
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.learning.agrovision.Model.CropPredictionInputModel
import com.learning.agrovision.Model.CropPridictionOutputModel
import com.learning.agrovision.Model.FertilizerInputModel
import com.learning.agrovision.Model.FertilizerOutputModel
import com.learning.agrovision.Model.RainfallInputModel
import com.learning.agrovision.Model.RainfallOutputModel
import com.learning.agrovision.Model.YeidlOutputModel
import com.learning.agrovision.Model.YeildInputModel
import com.learning.cropcare.ApiService
import com.learning.cropcare.Fragment.CropPrediction
import com.learning.cropcare.Fragment.CropYieldPrediction
import com.learning.cropcare.Fragment.FertilizerRecommendation
import com.learning.cropcare.Model.ReverseGeoCode.LocationInputModel
import com.learning.cropcare.Model.ReverseGeoCode.LocationOutputModel
import com.learning.cropcare.Utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class APIViewModel : ViewModel() {

    var result_fertilizer  : MutableLiveData<Response<FertilizerOutputModel>> = MutableLiveData()
    fun Fertilizer(activity: Context, input: FertilizerInputModel,fragment:FertilizerRecommendation) {
        if (checkForInternet1(activity)) {
            val matchApi = Constants.getInstance().create(ApiService::class.java)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = matchApi.fertilizer(input)
                    Log.d("rk",result.toString() )
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            result_fertilizer.value = result
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("rk", "Exception: ${e.message}")
                    fragment.errorFn("Registration failed. Please check your internet connection and try again.")
                }
            }
        } else {
            fragment.errorFn("Please switch on your internet and retry")
        }
    }
    fun observe_registerNewUser(): LiveData<Response<FertilizerOutputModel>> = result_fertilizer


    var result_yeild  : MutableLiveData<Response<YeidlOutputModel>> = MutableLiveData()
    fun yeild(context: Context,input: YeildInputModel,fragment:CropYieldPrediction) {
        if (checkForInternet1(context)) {
            val matchApi = Constants.getInstance().create(ApiService::class.java)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = matchApi.cropYield(input)
                    Log.d("rk",result.toString() )
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            result_yeild.value = result
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("rk", "Exception: ${e.message}")
                    fragment.errorFn("Registration failed. Please check your internet connection and try again.")
                }
            }
        } else {
            fragment.errorFn("Please switch on your internet and retry")
        }
    }
    fun observe_yeild(): LiveData<Response<YeidlOutputModel>> = result_yeild


    var result_cropProduction  : MutableLiveData<Response<CropPridictionOutputModel>> = MutableLiveData()
    fun cropPrediction(context: Context,input: CropPredictionInputModel,fragment:CropPrediction) {
        if (checkForInternet1(context)) {
            val matchApi = Constants.getInstance().create(ApiService::class.java)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = matchApi.cropPrediction(input)
                    Log.d("rk",result.toString() )
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            result_cropProduction.value = result
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("rk", "Exception: ${e.message}")
                    fragment.errorFn("Registration failed. Please check your internet connection and try again.")
                }
            }
        } else {
            fragment.errorFn("Please switch on your internet and retry")
        }
    }
    fun observe_cropPrediction(): LiveData<Response<CropPridictionOutputModel>> = result_cropProduction


    var result_rainfall  : MutableLiveData<Response<RainfallOutputModel>> = MutableLiveData()
    fun rainfall(context: Context, input: RainfallInputModel, fragment:CropPrediction) {
        if (checkForInternet1(context)) {
            val matchApi = Constants.getInstance().create(ApiService::class.java)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = matchApi.rainfall(input)
                    Log.d("rk",result.toString() )
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            result_rainfall.value = result
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("rk", "Exception: ${e.message}")
                    fragment.errorFn("Registration failed. Please check your internet connection and try again.")
                }
            }
        } else {
            fragment.errorFn("Please switch on your internet and retry")
        }
    }
    fun observe_rainfall(): LiveData<Response<RainfallOutputModel>> = result_rainfall

    var result_location  : MutableLiveData<LocationOutputModel> = MutableLiveData()
    fun locationData(context: Context, input: LocationInputModel, fragment: Fragment) {
        if (checkForInternet1(context)) {
            val matchApi = Constants.getInstance1().create(ApiService::class.java)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = matchApi.reverseGeocode(input.location,input.languahe,input.apiKey,input.apiHost)
                    Log.d("rk",result.toString() )
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            result_location.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = parseErrorMessage(errorBody)
                            when(fragment)
                            {
                                is CropPrediction->{
                                    fragment.errorFn(errorMessage ?: "Unknown error")
                                }
                                is CropYieldPrediction->{
                                    fragment.errorFn(errorMessage ?: "Unknown error")
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("rk", "Exception: ${e.message}")
                    when(fragment)
                    {
                        is CropPrediction->{
                            fragment.errorFn("Registration failed. Please check your internet connection and try again.")
                        }
                        is CropYieldPrediction->{
                            fragment.errorFn("Registration failed. Please check your internet connection and try again.")
                        }
                    }
                }
            }
        } else {
            when(fragment)
            {
                is CropPrediction->{
                    fragment.errorFn("Please switch on your internet and retry")
                }
                is CropYieldPrediction->{
                    fragment.errorFn("Please switch on your internet and retry")
                }
            }
        }
    }
    fun observe_locationData(): LiveData<LocationOutputModel> = result_location


    private fun parseErrorMessage(errorBody: String?): String? {
        errorBody?.let {
            try {
                val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                return jsonObject?.get("error")?.asString
            } catch (e: Exception) {
                Log.e("rk", "Error parsing error message: ${e.message}")
            }
        }
        return null
    }
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