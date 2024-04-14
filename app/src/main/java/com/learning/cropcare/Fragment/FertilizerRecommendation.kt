package com.learning.cropcare.Fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learning.agrovision.Model.FertilizerInputModel
import com.learning.cropcare.R
import com.learning.cropcare.ViewModel.APIViewModel
import com.learning.cropcare.databinding.FragmentFertilizerRecommendationBinding


class FertilizerRecommendation : Fragment() {
    lateinit var viewModel:APIViewModel
    var dialog: Dialog?=null
    var tempValue:Int=-1
    var humidityValue:Int=-1
    var moistureValue:Int=-1
    var soilValue:Int=-1
    var cropValue:Int=-1
    var nitrogenValue:Int=-1
    var potassiumValue:Int=-1
    var phospherousValue:Int=-1
    lateinit var singleValueTypePopUp: Dialog
    lateinit var binding:FragmentFertilizerRecommendationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentFertilizerRecommendationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[APIViewModel::class.java]
        val soilTypeMap = mapOf(
            "Black" to 0,
            "Clayey" to 1,
            "Loamy" to 2,
            "Red" to 3,
            "Sandy" to 4
        )
        val cropTypeMap = mapOf(
            "Barley" to 0,
            "Cotton" to 1,
            "Ground Nuts" to 2,
            "Maize" to 3,
            "Millets" to 4,
            "Oil seeds" to 5,
            "Paddy" to 6,
            "Pulses" to 7,
            "Sugarcane" to 8,
            "Tobacco" to 9,
            "Wheat" to 10
        )
        val fertilizerTypeMap = mapOf(
            "10-26-26" to 0,
            "14-35-14" to 1,
            "17-17-17" to 2,
            "20-20" to 3,
            "28-28" to 4,
            "DAP" to 5,
            "Urea" to 6
        )
        val cropNamesList: ArrayList<String> = ArrayList(cropTypeMap.keys)
        val soilList: ArrayList<String> = ArrayList(soilTypeMap.keys)
        val fertilizerList: ArrayList<String> = ArrayList(fertilizerTypeMap.keys)

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
        binding.moistureCardView.setOnClickListener {
            singleValueTypePopUp("Please enter the moisture value", tempValue) { newValue ->
                moistureValue = newValue
                Log.d("rk", moistureValue.toString())
                binding.moistureValue.text="${moistureValue}"
            }
        }
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
        binding.soilTypeCardView.setOnClickListener {
            singleValueChoosePopUp("Please choose one soil value",soilList) { newValue ->
                soilValue = newValue
                Log.d("rk", soilValue.toString())
                binding.soilTypeValue.text="${soilList[soilValue]}"
            }
        }
        binding.cropTypeCardView.setOnClickListener {
            singleValueChoosePopUp("Please choose one crop value",cropNamesList) { newValue ->
                cropValue = newValue
                Log.d("rk", cropValue.toString())
                binding.cropTypeValue.text="${cropNamesList[cropValue]}"
            }
        }
        try {
            binding.predictYield.setOnClickListener {
                Log.d("rk","hii")
                if(tempValue!=-1 && humidityValue!=-1 && moistureValue!=-1 && soilValue!=-1 && cropValue!=-1 && nitrogenValue!=-1 && potassiumValue !=-1 && phospherousValue!=-1)
                {
                    showProgressbar()
                    viewModel.Fertilizer(requireContext(), FertilizerInputModel(tempValue,humidityValue,moistureValue,soilValue,cropValue,nitrogenValue,potassiumValue,phospherousValue),this);
                }
                else
                {
                    Toast("Please enter all parameters")
                }
            }
            viewModel.observe_registerNewUser().observe(requireActivity(), Observer { res->
                cancelProgressbar()
                if(res.isSuccessful)
                {
                    res.body()!!.prediction?.get(0)?.let { Log.d("rk", it.toString()) }
                    binding.value.text= "The recommended fertilizer is ${fertilizerList[res.body()!!.prediction?.get(0)!!]}"
                }
            })
        }catch (e:Exception)
        {
            Log.d("rk",e.message.toString())
        }
        return binding.root
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
        dialog!!.setContentView(com.learning.cropcare.R.layout.progress_bar)
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
}