package com.learning.agrovision.Model

data class FertilizerInputModel(
    var Temperature:Int?=null,
    var Humidity:Int?=null,
    var Moisture:Int?=null,
    var SoilType:Int?=null,
    var CropType:Int?=null,
    var Nitrogen:Int?=null,
    var Potassium:Int?=null,
    var Phosphorous:Int?=null
)
