package com.learning.cropcare.Model.ReverseGeoCode

data class Result(
    var address: String? = null, // 39, NH 48 Service Rd, Kagithapatarai, Vellore, Tamil Nadu 632012, India
    var country: String? = null, // India
    var house: String? = null, // 39
    var locality: String? = null, // Vellore
    var location: Location? = null,
    var location_type: String? = null, // exact
    var postal_code: String? = null, // 632012
    var region: String? = null, // Tamil Nadu
    var street: String? = null, // NH 48 Service Road
    var sublocality: String? = null, // Kagithapatarai
    var type: String? = null // street_address
)