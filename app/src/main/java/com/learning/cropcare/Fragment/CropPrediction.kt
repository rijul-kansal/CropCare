package com.learning.cropcare.Fragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.*
import android.app.Dialog
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.learning.agrovision.Model.CropPredictionInputModel
import com.learning.agrovision.Model.RainfallInputModel
import com.learning.cropcare.Model.ReverseGeoCode.LocationInputModel
import com.learning.cropcare.R
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.ViewModel.APIViewModel
import com.learning.cropcare.ViewModel.FireStoreDataBaseViewModel
import com.learning.cropcare.databinding.FragmentCropPredictionBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CropPrediction : Fragment() {
    var dialog: Dialog?=null
    lateinit var binding: FragmentCropPredictionBinding
    lateinit var viewModel: APIViewModel
    lateinit var viewModel1: FireStoreDataBaseViewModel
    lateinit var singleValueTypePopUp: Dialog

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var locationCallback: LocationCallback

    var latitude:Double?=null
    var longitude:Double?=null

    var seasonValue :Int=-1
    var stateValue :Int=-1
    var areaValue :Int=-1
    var annunal_RainfallValue :Int=-1
    var fertilizerValue :Int=-1
    var yeildValue :Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentCropPredictionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel= ViewModelProvider(requireActivity())[APIViewModel::class.java]
        viewModel1= ViewModelProvider(requireActivity())[FireStoreDataBaseViewModel::class.java]
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
                        viewModel.locationData(requireContext(), LocationInputModel(location = location),this@CropPrediction)


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



        val cropList = ArrayList(Constants.cropMapping().keys)
        val seasonList = ArrayList(Constants.seasonMapping().keys)
        val stateList = ArrayList(Constants.stateMapping().keys)

        viewModel.observe_locationData().observe(viewLifecycleOwner, Observer { task->
            cancelProgressbar()
            if(task!=null)
            {
                Log.d("rk", task.results?.get(0)?.region.toString())
                var region = task.results?.get(0)?.region.toString()

                binding.stateValue.text=region
                for(i in 0.. stateList.size-1)
                {
                    if(region== stateList[i])
                    {
                        stateValue=i
                        Log.d("rk",stateValue.toString())
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
                binding.areaValue.text=areaValue.toString()
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
                singleValueChoosePopUp("Please choose one state value",stateList) { newValue ->
                    stateValue = newValue
                    Log.d("rk", seasonValue.toString())
                    binding.stateValue.text=stateList[stateValue]
                }
            }

        }
        binding.seasonCardView.setOnClickListener {
            singleValueChoosePopUp("Please choose one season value",seasonList) { newValue ->
                seasonValue = newValue
                Log.d("rk", seasonValue.toString())
                binding.seasonValue.text=seasonList[seasonValue]
            }
        }
        binding.rainfallCardView.setOnClickListener {
            singleValueTypePopUp("Please enter the annual rainfall  value", areaValue) { newValue ->
                annunal_RainfallValue = newValue
                Log.d("rk", annunal_RainfallValue.toString())
                binding.rainfallValue.text=annunal_RainfallValue.toString()
            }
        }
        binding.fertilizerCardView.setOnClickListener {
            singleValueTypePopUp("Please enter the fertilizer value", areaValue) { newValue ->
                fertilizerValue = newValue
                Log.d("rk", fertilizerValue.toString())
                binding.fertilizerValue.text=fertilizerValue.toString()
            }
        }
        binding.yieldCardView.setOnClickListener {
            singleValueTypePopUp("Please enter the yeild value", areaValue) { newValue ->
                yeildValue = newValue
                Log.d("rk", yeildValue.toString())
                binding.yieldValue.text=yeildValue.toString()
            }
        }


        binding.predictYield.setOnClickListener {
            if(annunal_RainfallValue==-1)
            {
                showProgressbar()
                viewModel.rainfall(requireContext(),RainfallInputModel(stateList[stateValue],seasonList[seasonValue]),this)

            }
            else
            {
                if(seasonValue!=-1 && stateValue!=-1 && areaValue!=-1 && annunal_RainfallValue!=-1 && fertilizerValue!=-1 && yeildValue!=-1 )
                {
                    showProgressbar()
                    viewModel.cropPrediction(requireContext(), CropPredictionInputModel(annunal_RainfallValue,areaValue,fertilizerValue,seasonValue,stateValue,yeildValue),this)
                }
                else
                {
                    Toast("Please enter all the values")
                }
            }
        }
        viewModel.observe_cropPrediction().observe(requireActivity(), Observer { res->
            cancelProgressbar()
            if(res.isSuccessful)
            {
                var map : HashMap<String,String> = HashMap()

                map["date"] = dataInHumanReadableFormat()
                map["value"] = "Crop Prediction"
                map["seasonValue"] = seasonList[seasonValue]
                map["stateValue"] = stateList[stateValue]
                map["annual_rainfall"] = annunal_RainfallValue.toString()
                map["fertilizerValue"] = fertilizerValue.toString()
                map["yieldValue"] = yeildValue.toString()
                map["result"] = "The crop  ${cropList[res.body()!!.prediction?.get(0)!!]}"
                viewModel1.addDataToHistorymain(requireContext(),this,map)
                res.body()!!.prediction?.get(0)?.let { Log.d("rkk","value" +it.toString()) }
                binding.value.text= "The crop  ${cropList[res.body()!!.prediction?.get(0)!!]}"
            }
            else
            {
            }
        })
        viewModel.observe_rainfall().observe(requireActivity(), Observer { res->
            cancelProgressbar()
            if(res.isSuccessful)
            {
                annunal_RainfallValue = res.body()!!.total_avg!!.toInt()
                Log.d("rk",annunal_RainfallValue.toString())
                binding.rainfallValue.text=annunal_RainfallValue.toString()
                if(seasonValue!=-1 && stateValue!=-1 && areaValue!=-1 && annunal_RainfallValue!=-1 && fertilizerValue!=-1 && yeildValue!=-1 )
                {
                    showProgressbar()
                    viewModel.cropPrediction(requireContext(), CropPredictionInputModel(annunal_RainfallValue,areaValue,fertilizerValue,seasonValue,stateValue,yeildValue),this)
                }
                else
                {
                    Toast("Please enter all the values")
                }
            }
            else
            {
            }
        })
        return binding.root
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
        val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_singlechoice)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataInHumanReadableFormat(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
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
