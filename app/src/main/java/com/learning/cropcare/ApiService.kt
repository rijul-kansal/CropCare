package com.learning.cropcare

import com.learning.agrovision.Model.CropPredictionInputModel
import com.learning.agrovision.Model.CropPridictionOutputModel
import com.learning.agrovision.Model.FertilizerInputModel
import com.learning.agrovision.Model.FertilizerOutputModel
import com.learning.agrovision.Model.RainfallInputModel
import com.learning.agrovision.Model.RainfallOutputModel
import com.learning.agrovision.Model.YeidlOutputModel
import com.learning.agrovision.Model.YeildInputModel
import com.learning.cropcare.Model.PestDetectionInputModel
import com.learning.cropcare.Model.PestPredictionOutputModel
import com.learning.cropcare.Model.ReverseGeoCode.LocationOutputModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/fertilizer_predict")
    suspend fun fertilizer(@Body request: FertilizerInputModel): Response<FertilizerOutputModel>

    @POST("/yeild_predict")
    suspend fun cropYield(@Body request: YeildInputModel): Response<YeidlOutputModel>

    @POST("/cropPrediction")
    suspend fun cropPrediction(@Body request: CropPredictionInputModel): Response<CropPridictionOutputModel>
    @POST("/rainfall")
    suspend fun rainfall(@Body request: RainfallInputModel): Response<RainfallOutputModel>

    @GET("/ReverseGeocode")
    suspend fun reverseGeocode(
        @Query("location") location: String,
        @Query("language") language: String,
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") apiHost: String
    ): Response<LocationOutputModel>


}