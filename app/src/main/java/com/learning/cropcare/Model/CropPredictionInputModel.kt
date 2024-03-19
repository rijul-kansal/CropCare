package com.learning.agrovision.Model

data class CropPredictionInputModel(
    var Annual_Rainfall: Int? = null, // 2051.5
    var Area: Int? = null, // 1234.5
    var Fertilizer: Int? = null, // 7024878.38
    var Season: Int? = null, // 3
    var State: Int? = null, // 0
    var Yield: Int? = null // 0.796087
)