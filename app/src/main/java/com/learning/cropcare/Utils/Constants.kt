package com.learning.cropcare.Utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val LANGUAGE="choose one language"
    const val EMAIL="email"
    const val NAME="name"
    const val PASSWORD="password"
    const val SELECT_PICTURE=2
    const val BASE_URL ="https://4ace-34-168-54-48.ngrok-free.app"
    const val BASE_URL1 ="https://trueway-geocoding.p.rapidapi.com"
    const val SIGNUP_OR_SIGN_IN="signinorsignup"
    const val START_LANGUAGE_CHOSEN_OR_NOT="start language chosen or not "
    const val HISTORY="history"
    // for model + rainfall
    fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getInstance1() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    const val MODEL_PATH = "model.tflite"
    const val LABELS_PATH = "labels.txt"
}