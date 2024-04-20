package com.learning.cropcare.Utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val LANGUAGE="choose one language"
    const val EMAIL="email"
    const val NAME="name"
    const val PASSWORD="password"
    const val SELECT_PICTURE=2
    const val BASE_URL ="https://3a81-34-86-25-235.ngrok-free.app"
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
}