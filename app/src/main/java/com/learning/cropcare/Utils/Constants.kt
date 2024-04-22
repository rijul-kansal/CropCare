package com.learning.cropcare.Utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val LANGUAGE="choose one language"
    const val EMAIL="email"
    const val NAME="name"
    const val PASSWORD="password"
    const val SELECT_PICTURE=2
    const val BASE_URL ="https://5409-34-139-102-120.ngrok-free.app"
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

    fun cropMapping(): Map<String, Int>
    {
        return mapOf(
            "Arecanut" to 0,
            "Arhar/Tur" to 1,
            "Bajra" to 2,
            "Banana" to 3,
            "Barley" to 4,
            "Black pepper" to 5,
            "Cardamom" to 6,
            "Cashewnut" to 7,
            "Castor seed" to 8,
            "Coconut" to 9,
            "Coriander" to 10,
            "Cotton(lint)" to 11,
            "Cowpea(Lobia)" to 12,
            "Dry chillies" to 13,
            "Garlic" to 14,
            "Ginger" to 15,
            "Gram" to 16,
            "Groundnut" to 17,
            "Guar seed" to 18,
            "Horse-gram" to 19,
            "Jowar" to 20,
            "Jute" to 21,
            "Khesari" to 22,
            "Linseed" to 23,
            "Maize" to 24,
            "Masoor" to 25,
            "Mesta" to 26,
            "Moong(Green Gram)" to 27,
            "Moth" to 28,
            "Niger seed" to 29,
            "Oilseeds total" to 30,
            "Onion" to 31,
            "Other Rabi pulses" to 32,
            "Other Cereals" to 33,
            "Other Kharif pulses" to 34,
            "Other Summer Pulses" to 35,
            "Peas & beans (Pulses)" to 36,
            "Potato" to 37,
            "Ragi" to 38,
            "Rapeseed &Mustard" to 39,
            "Rice" to 40,
            "Safflower" to 41,
            "Sannhamp" to 42,
            "Sesamum" to 43,
            "Small millets" to 44,
            "Soyabean" to 45,
            "Sugarcane" to 46,
            "Sunflower" to 47,
            "Sweet potato" to 48,
            "Tapioca" to 49,
            "Tobacco" to 50,
            "Turmeric" to 51,
            "Urad" to 52,
            "Wheat" to 53,
            "other oilseeds" to 54
        )
    }

    fun seasonMapping(): Map<String, Int>
    {
        return  mapOf(
            "autumn" to 0,
            "kharif" to 1,
            "rabi" to 2,
            "summer" to 3,
            "whole Year" to 4,
            "winter" to 5
        )
    }

    fun stateMapping(): Map<String,Int>
    {
        return mapOf(
            "Andhra Pradesh" to 0,
            "Arunachal Pradesh" to 1,
            "Assam" to 2,
            "Bihar" to 3,
            "Chhattisgarh" to 4,
            "Delhi" to 5,
            "Goa" to 6,
            "Gujarat" to 7,
            "Haryana" to 8,
            "Himachal Pradesh" to 9,
            "Jammu and Kashmir" to 10,
            "Jharkhand" to 11,
            "Karnataka" to 12,
            "Kerala" to 13,
            "Madhya Pradesh" to 14,
            "Maharashtra" to 15,
            "Manipur" to 16,
            "Meghalaya" to 17,
            "Mizoram" to 18,
            "Nagaland" to 19,
            "Odisha" to 20,
            "Puducherry" to 21,
            "Punjab" to 22,
            "Sikkim" to 23,
            "Tamil Nadu" to 24,
            "Telangana" to 25,
            "Tripura" to 26,
            "Uttar Pradesh" to 27,
            "Uttarakhand" to 28,
            "West Bengal" to 29
        )
    }
}