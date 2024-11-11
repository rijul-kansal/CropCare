package com.learning.agrovision.Model

data class CropPredictionInputModel(
    var N: Int? = null, // 2051.5
    var P: Int? = null, // 1234.5
    var K: Int? = null, // 7024878.38
    var temperature: Int? = null, // 3
    var humidity: Int? = null, // 0
    var ph: Int? = null ,// 0.796087
    var rainfall: Int? = null, // 0.796087
    var soil: Int? = null // 0.796087
)