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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/fertilizer_predict")
    suspend fun fertilizer(@Body request: FertilizerInputModel): Response<FertilizerOutputModel>

    @POST("/yeild_predict")
    suspend fun cropYield(@Body request: YeildInputModel): Response<YeidlOutputModel>

    @POST("/cropPrediction")
    suspend fun cropPrediction(@Body request: CropPredictionInputModel): Response<CropPridictionOutputModel>
    @POST("/")
    suspend fun rainfall(@Body request: RainfallInputModel): Response<RainfallOutputModel>

    @POST("/")
    suspend fun pest(@Body request: PestDetectionInputModel): Response<PestPredictionOutputModel>
}