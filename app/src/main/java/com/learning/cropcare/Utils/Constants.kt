package com.learning.cropcare.Utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val LANGUAGE="choose one language"
    const val EMAIL="email"
    const val NAME="name"
    const val PASSWORD="password"
    const val SELECT_PICTURE=2
    const val BASE_URL ="."
    const val BASE_URL1 ="https://441c-35-186-172-80.ngrok-free.app"
    const val BASE_URL2 ="https://e1e8-34-106-115-91.ngrok-free.app"
    const val BASE_URL3 ="https://b30e-34-70-55-200.ngrok-free.app"
    const val SIGNUP_OR_SIGN_IN="signinorsignup"
    // for fertilizer
    fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // crop yeild , pre
    fun getInstance1() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // rainfall
    fun getInstance2() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // pest
    fun getInstance3() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL3)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}