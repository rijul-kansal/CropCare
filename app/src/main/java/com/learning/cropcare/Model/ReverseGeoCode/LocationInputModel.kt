package com.learning.cropcare.Model.ReverseGeoCode

data class LocationInputModel(
    var location: String,
    var languahe:String="en",
    var apiKey:String="7034315408mshb7e110448985e65p1f22b2jsnbe933ee5cb72",
    var apiHost:String="trueway-geocoding.p.rapidapi.com"
)