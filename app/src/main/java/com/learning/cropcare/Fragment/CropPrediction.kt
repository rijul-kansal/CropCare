package com.learning.cropcare.Fragment


import android.os.Bundle
import java.util.*
import android.app.Dialog
import android.os.Build
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
import com.learning.agrovision.Model.CropPredictionInputModel
import com.learning.cropcare.R
import com.learning.cropcare.ViewModel.APIViewModel
import com.learning.cropcare.ViewModel.FireStoreDataBaseViewModel
import com.learning.cropcare.databinding.FragmentCropPredictionBinding

class CropPrediction : Fragment() {
    var dialog: Dialog?=null
    lateinit var binding: FragmentCropPredictionBinding
    lateinit var viewModel: APIViewModel
    lateinit var viewModel1: FireStoreDataBaseViewModel
    lateinit var singleValueTypePopUp: Dialog
    var nitrogenValue:Int=-1
    var potassiumValue:Int=-1
    var phospherousValue:Int=-1
    var tempValue:Int=-1
    var humidityValue:Int=-1
    var phValue:Int=-1
    var rainfallValue:Int=-1
    var soilValue:Int=-1
    var state:String="Haryana"
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentCropPredictionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            viewModel = ViewModelProvider(requireActivity())[APIViewModel::class.java]
            val soilTypeMap = mapOf(
                "Black" to 0,
                "Clayey" to 1,
                "Loamy" to 2,
                "Red" to 3,
                "Sandy" to 4
            )
            val cropList = arrayListOf(
                "apple",       // 0
                "banana",      // 1
                "blackgram",   // 2
                "chickpea",    // 3
                "coconut",     // 4
                "coffee",      // 5
                "cotton",      // 6
                "grapes",      // 7
                "jute",        // 8
                "kidneybeans", // 9
                "lentil",      // 10
                "maize",       // 11
                "mango",       // 12
                "mothbeans",   // 13
                "mungbean",    // 14
                "muskmelon",   // 15
                "orange",      // 16
                "papaya",      // 17
                "pigeonpeas",  // 18
                "pomegranate", // 19
                "rice",        // 20
                "watermelon"   // 21
            )

            val soilList: ArrayList<String> = ArrayList(soilTypeMap.keys)
            binding.nitrogenCardView.setOnClickListener {
                singleValueTypePopUp("Please enter the nitrogen value", tempValue) { newValue ->
                    nitrogenValue = newValue
                    Log.d("rk", nitrogenValue.toString())
                    binding.nitrogenValue.text="${ nitrogenValue}"
                }
            }
            binding.potassiumCardView.setOnClickListener {
                singleValueTypePopUp("Please enter the potassium value", tempValue) { newValue ->
                    potassiumValue = newValue
                    Log.d("rk", potassiumValue.toString())
                    binding.pottasiumValue.text="${potassiumValue}"
                }
            }
            binding.phosphorousCardView.setOnClickListener {
                singleValueTypePopUp("Please enter the phosphorous value", tempValue) { newValue ->
                    phospherousValue = newValue
                    Log.d("rk", phospherousValue.toString())
                    binding.phosphorrusValue.text="${ phospherousValue}"
                }
            }
            binding.tempCardView.setOnClickListener {
                singleValueTypePopUp("Please enter the temperature value", tempValue) { newValue ->
                    tempValue = newValue
                    Log.d("rk", tempValue.toString())
                    binding.tempValue.text="${tempValue}"
                }
            }
            binding.humidityCardView.setOnClickListener {
                singleValueTypePopUp("Please enter the humidity value", tempValue) { newValue ->
                    humidityValue = newValue
                    Log.d("rk", humidityValue.toString())
                    binding.humidityValue.text="${ humidityValue}"
                }
            }
            binding.phCardView.setOnClickListener {
                singleValueTypePopUp("Please enter the ph value", tempValue) { newValue ->
                    phValue = newValue
                    Log.d("rk", phValue.toString())
                    binding.phValue.text="${phValue}"
                }
            }
            binding.rainfallValue.setOnClickListener {
                singleValueTypePopUp("Please enter the rainfall value", tempValue) { newValue ->
                    rainfallValue = newValue
                    Log.d("rk", rainfallValue.toString())
                    binding.rainfallValue.text="${rainfallValue}"
                }
            }
            binding.cropCardView.setOnClickListener {
                singleValueChoosePopUp("Please choose one crop value",soilList) { newValue ->
                    soilValue = newValue
                    Log.d("rk", soilList.toString())
                    binding.cropValue.text="${soilList[soilValue]}"
                }
            }
            binding.predictYield.setOnClickListener {
                if(nitrogenValue!=-1 && potassiumValue!=-1 && phospherousValue!=-1 && tempValue!=-1 &&
                    humidityValue!=-1 && rainfallValue!=-1 && phValue!=-1 && soilValue!=-1)
                {
                    showProgressbar()
                    viewModel.cropPrediction(requireContext(),CropPredictionInputModel(nitrogenValue,phospherousValue,potassiumValue,tempValue,humidityValue,phValue,rainfallValue,soilValue),this)
                }
            }
            viewModel.observe_cropPrediction().observe(requireActivity(), Observer { res ->
                cancelProgressbar()
                if (res.isSuccessful) {
                    binding.value.text = "The crop  ${cropList[res.body()!!.prediction?.get(0)!!]}"
                }
            })
        } catch (e: Exception) {
            Log.d("rk", e.message.toString())
        }
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
}
