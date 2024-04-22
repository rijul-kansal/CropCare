package com.learning.cropcare.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.learning.agrovision.Model.YeildInputModel
import com.learning.cropcare.Model.ReverseGeoCode.LocationInputModel
import com.learning.cropcare.R
import com.learning.cropcare.ViewModel.APIViewModel
import com.learning.cropcare.ViewModel.FireStoreDataBaseViewModel
import com.learning.cropcare.databinding.FragmentCropYieldPredictionBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class CropYieldPrediction : Fragment() {
    lateinit var viewModel: APIViewModel
    lateinit var viewModel1: FireStoreDataBaseViewModel
    var dialog: Dialog?=null
    val statesArrayList = ArrayList<String>()
    val cropsArrayList = ArrayList<String>()
    val seasonsArrayList = ArrayList<String>()
    lateinit var singleValueTypePopUp:Dialog
    var areaValue:Int=-1
    var stateValue:Int=-1
    var cropValue:Int=-1
    var yearValue:Int=-1
    var seasonValue:Int=-1
    lateinit var binding:FragmentCropYieldPredictionBinding


    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var locationCallback: LocationCallback

    var latitude:Double?=null
    var longitude:Double?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentCropYieldPredictionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            viewModel= ViewModelProvider(this)[APIViewModel::class.java]
            viewModel1= ViewModelProvider(this)[FireStoreDataBaseViewModel::class.java]

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        Log.d("rk", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")

                        if(latitude==null && longitude==null)
                        {
                            latitude=location.latitude
                            longitude=location.longitude
                            var location = "$latitude,$longitude"
                            showProgressbar()
                            viewModel.locationData(requireContext(), LocationInputModel(location = location),this@CropYieldPrediction)


                        }

                    }
                }
            }
            if(checkPermissions())
            {
                if(!isLocationEnabled(requireContext())) {
                    Toast("Please turn on location")
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                else
                {
                    startLocationUpdates()
                }
            }
            else
            {
                requestPermissions()
            }

            stateArrFn()
            cropArrFn()
            seasonArrFn()

            viewModel.observe_locationData().observe(viewLifecycleOwner, Observer { task->
                cancelProgressbar()
                if(task!=null)
                {
                    Log.d("rk", task.results?.get(0)?.region.toString())
                    var region = task.results?.get(0)?.region.toString()

                    binding.stateValue.text=region
                    for(i in 0.. statesArrayList.size-1)
                    {
                        if(region== statesArrayList[i])
                        {
                            stateValue=i
                            break;
                        }
                    }
                    Log.d("rk",stateValue.toString())
                }
            })
            binding.areaCardView.setOnClickListener {
                singleValueTypePopUp("Please enter the area value", areaValue) { newValue ->
                    areaValue = newValue
                    Log.d("rk", areaValue.toString())
                    binding.areaValue.text="${areaValue.toString()}"
                }
            }

            binding.stateCardView.setOnClickListener {
                if(latitude==null && longitude==null)
                {
                    if(checkPermissions())
                    {
                        if(isLocationEnabled(requireContext()))
                        {
                            try {
                                startLocationUpdates()
                            }catch (e:Exception)
                            {
                                Log.d("rk",e.message.toString())
                            }
                        }
                        else
                        {
                            Toast("Please turn on location")
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                        }
                    }
                    else
                    {
                        requestPermissions()
                    }
                }
                else
                {
                    singleValueChoosePopUp("Please choose one state value",statesArrayList) { newValue ->
                        stateValue = newValue
                        Log.d("rk", seasonValue.toString())
                        binding.stateValue.text=statesArrayList[stateValue]
                    }
                }
            }
            binding.cropCardView.setOnClickListener {
                singleValueChoosePopUp("Please choose one crop value",cropsArrayList) { newValue ->
                    cropValue = newValue
                    Log.d("rk", cropValue.toString())
                    binding.cropValue.text="${cropsArrayList[cropValue]}"
                }
            }
            binding.yearCardView.setOnClickListener {
                val currentDate = LocalDate.now()
                val currentYear = currentDate.year
                binding.yearValue.text=currentYear.toString()
                yearValue=currentYear
            }
            binding.seasonCardView.setOnClickListener {
                singleValueChoosePopUp("Please choose one season value",seasonsArrayList) { newValue ->
                    seasonValue = newValue
                    Log.d("rk", seasonValue.toString())
                    binding.seasonValue.text="${seasonsArrayList[seasonValue]}"
                }
            }
            binding.predictYield.setOnClickListener {
                if(areaValue!=-1 && stateValue!=-1 && cropValue!=-1 && yearValue!=-1 && seasonValue!=-1)
                {
                    showProgressbar()
                    viewModel.yeild(requireContext(), YeildInputModel(areaValue,stateValue,cropValue,yearValue,seasonValue),this)
                }
                else
                {
                    Toast("Please enter all the values")
                }
            }
            viewModel.observe_yeild().observe(requireActivity(), Observer { res->
                cancelProgressbar()
                if(res.isSuccessful)
                {
                    var map : HashMap<String,String> = HashMap()
                    map["date"] = dataInHumanReadableFormat()
                    map["value"] = "Crop Yield Prediction"
                    map["seasonValue"] = seasonsArrayList[seasonValue]
                    map["stateValue"] = statesArrayList[stateValue]
                    map["areaValue"] = areaValue.toString()
                    map["cropValue"] = cropValue.toString()
                    map["result"] = "The production in  tone is  ${res.body()!!.prediction?.get(0)!!}"
                    viewModel1.addDataToHistorymain(requireContext(),this,map)
                    res.body()!!.prediction?.get(0)?.let { Log.d("rkk","value" +it.toString()) }
                    binding.value.text= "The production in  tone is  ${res.body()!!.prediction?.get(0)!!}"
                }
                else
                {
                }
            })
        }catch (e:Exception)
        {

        }
        return binding.root
    }
    fun stateArrFn()
    {
        val statesMap = mapOf(
            "Andaman and Nicobar Islands" to 1,
            "Andhra Pradesh" to 2,
            "Arunachal Pradesh" to 3,
            "Assam" to 4,
            "Bihar" to 5,
            "Chandigarh" to 6,
            "Chhattisgarh" to 7,
            "Dadra and Nagar Haveli" to 8,
            "Daman and Diu" to 9,
            "Delhi" to 10,
            "Goa" to 11,
            "Gujarat" to 12,
            "Haryana" to 13,
            "Himachal Pradesh" to 14,
            "Jammu and Kashmir" to 15,
            "Jharkhand" to 16,
            "Karnataka" to 17,
            "Kerala" to 18,
            "Madhya Pradesh" to 19,
            "Maharashtra" to 20,
            "Manipur" to 21,
            "Meghalaya" to 22,
            "Tamil Nadu" to 23
        )
        for (state in statesMap.keys) {
            statesArrayList.add(state)
        }
    }

    fun cropArrFn()
    {
        val cropsMap = mapOf(
            "Rice" to 1,
            "Sugarcane" to 2,
            "Arhar/Tur" to 3,
            "Groundnut" to 4,
            "Maize" to 5,
            "Moong(Green Gram)" to 6,
            "Rapeseed &Mustard" to 7,
            "Sesamum" to 8,
            "Urad" to 9,
            "Wheat" to 10
        )

// Add keys (crops) to the ArrayList
        for (crop in cropsMap.keys) {
            cropsArrayList.add(crop)
        }
    }

    fun seasonArrFn()
    {
        val seasonsMap = mapOf(
            "Kharif" to 1,
            "Whole Year" to 2,
            "Rabi" to 3,
            "Autumn" to 4,
            "Summer" to 5,
            "Winter" to 6
        )

// Add keys (seasons) to the ArrayList
        for (season in seasonsMap.keys) {
            seasonsArrayList.add(season)
        }
    }

    private fun singleValueTypePopUp(t: String, value: Int, callback: (Int) -> Unit) {
        try {
            singleValueTypePopUp = Dialog(requireContext())
            val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.single_value_type_popup, null)
            val submitOtpButton = view.findViewById<TextView>(R.id.Enter_otp_btn)
            val editTextValue = view.findViewById<EditText>(R.id.single_value_type_ed)
            view.findViewById<TextView>(R.id.single_value_type_tv).text = t
            singleValueTypePopUp.setContentView(view)
            singleValueTypePopUp.setCanceledOnTouchOutside(false)
            val window = singleValueTypePopUp.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setGravity(Gravity.BOTTOM)
            submitOtpButton.setOnClickListener {
                val newValue = editTextValue.text.toString().toInt()
                callback(newValue)
                singleValueTypePopUp.dismiss()
            }
            singleValueTypePopUp.show()
        } catch (e: Exception) {
            Log.d("rk", e.message.toString())
        }
    }

    fun singleValueChoosePopUp(title:String,lis:ArrayList<String>,callback: (Int) -> Unit) {
        var checkedItem = 0
        var selectedIndexForParamter = 0
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        val arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.select_dialog_singlechoice)
        for(item in lis)
        {
            arrayAdapter.add(item)
        }
        builder.setSingleChoiceItems(arrayAdapter, checkedItem) { dialog, which ->
            selectedIndexForParamter = which
        }
        builder.setPositiveButton("Select") { dialog, which ->
            Log.d("rk",selectedIndexForParamter.toString())
            try {
                callback(selectedIndexForParamter)
            } catch (e: Exception) {
                Log.d("rk", e.message.toString())
            }
        }
        builder.setNegativeButton("Cancel", null)
        val dialog: android.app.AlertDialog? = builder.create()
        if (dialog != null) {
            dialog.show()
        }
    }

    fun errorFn(message:String)
    {
        cancelProgressbar()
        Toast(message)
    }

    fun Toast( message:String)
    {
        android.widget.Toast.makeText(requireContext(),message, android.widget.Toast.LENGTH_LONG).show()
    }

    fun showProgressbar()
    {
        dialog= Dialog(requireContext())
        dialog!!.setContentView(R.layout.progress_bar)
        dialog!!.show()

    }
    fun cancelProgressbar()
    {
        if(dialog!=null)
        {
            dialog!!.cancel()
            dialog=null
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun dataInHumanReadableFormat():String
    {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDate = current.format(formatter)
        return formattedDate.toString()
    }
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startLocationUpdates()
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

}