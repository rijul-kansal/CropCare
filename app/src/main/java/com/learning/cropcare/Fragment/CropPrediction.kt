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
import com.learning.agrovision.Model.CropPredictionInputModel
import com.learning.agrovision.Model.RainfallInputModel
import com.learning.cropcare.R
import com.learning.cropcare.ViewModel.APIViewModel
import com.learning.cropcare.databinding.FragmentCropPredictionBinding


class CropPrediction : Fragment() {
    var dialog: Dialog?=null
    lateinit var binding: FragmentCropPredictionBinding
    lateinit var viewModel: APIViewModel
    lateinit var singleValueTypePopUp: Dialog

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel= ViewModelProvider(requireActivity())[APIViewModel::class.java]
        val cropMappings = mapOf(
            "Arecanut" to 0,
            "Arhar/Tur" to 1,
            "Bajra" to 2,
            "Banana" to 3,
            "Barley" to 4,
            "Black pepper" to 5,
            "Cardamom" to 6,
            "Cashewnut" to 7,
            "Castor seed" to 8,
            "Coconut" to 9,
            "Coriander" to 10,
            "Cotton(lint)" to 11,
            "Cowpea(Lobia)" to 12,
            "Dry chillies" to 13,
            "Garlic" to 14,
            "Ginger" to 15,
            "Gram" to 16,
            "Groundnut" to 17,
            "Guar seed" to 18,
            "Horse-gram" to 19,
            "Jowar" to 20,
            "Jute" to 21,
            "Khesari" to 22,
            "Linseed" to 23,
            "Maize" to 24,
            "Masoor" to 25,
            "Mesta" to 26,
            "Moong(Green Gram)" to 27,
            "Moth" to 28,
            "Niger seed" to 29,
            "Oilseeds total" to 30,
            "Onion" to 31,
            "Other Rabi pulses" to 32,
            "Other Cereals" to 33,
            "Other Kharif pulses" to 34,
            "Other Summer Pulses" to 35,
            "Peas & beans (Pulses)" to 36,
            "Potato" to 37,
            "Ragi" to 38,
            "Rapeseed &Mustard" to 39,
            "Rice" to 40,
            "Safflower" to 41,
            "Sannhamp" to 42,
            "Sesamum" to 43,
            "Small millets" to 44,
            "Soyabean" to 45,
            "Sugarcane" to 46,
            "Sunflower" to 47,
            "Sweet potato" to 48,
            "Tapioca" to 49,
            "Tobacco" to 50,
            "Turmeric" to 51,
            "Urad" to 52,
            "Wheat" to 53,
            "other oilseeds" to 54
        )
        val cropList = ArrayList<String>(cropMappings.keys)
        val seasonMappings = mapOf(
            "autumn" to 0,
            "kharif" to 1,
            "rabi" to 2,
            "summer" to 3,
            "whole Year" to 4,
            "winter" to 5
        )
        val seasonList = ArrayList<String>(seasonMappings.keys)
        val stateMappings = mapOf(
            "Andhra Pradesh" to 0,
            "Arunachal Pradesh" to 1,
            "Assam" to 2,
            "Bihar" to 3,
            "Chhattisgarh" to 4,
            "Delhi" to 5,
            "Goa" to 6,
            "Gujarat" to 7,
            "Haryana" to 8,
            "Himachal Pradesh" to 9,
            "Jammu and Kashmir" to 10,
            "Jharkhand" to 11,
            "Karnataka" to 12,
            "Kerala" to 13,
            "Madhya Pradesh" to 14,
            "Maharashtra" to 15,
            "Manipur" to 16,
            "Meghalaya" to 17,
            "Mizoram" to 18,
            "Nagaland" to 19,
            "Odisha" to 20,
            "Puducherry" to 21,
            "Punjab" to 22,
            "Sikkim" to 23,
            "Tamil Nadu" to 24,
            "Telangana" to 25,
            "Tripura" to 26,
            "Uttar Pradesh" to 27,
            "Uttarakhand" to 28,
            "West Bengal" to 29
        )
        val stateList = ArrayList<String>(stateMappings.keys)

        binding.areaValue.setOnClickListener {
            singleValueTypePopUp("Please enter the area value", areaValue) { newValue ->
                areaValue = newValue
                Log.d("rk", areaValue.toString())
                binding.areaValue1.text=areaValue.toString()
            }
        }
        binding.stateValue.setOnClickListener {
            singleValueChoosePopUp("Please choose one state value",stateList) { newValue ->
                stateValue = newValue
                Log.d("rk", stateValue.toString())
                binding.stateValue1.text=stateList[stateValue]
            }
        }
        binding.seasonValue.setOnClickListener {
            singleValueChoosePopUp("Please choose one season value",seasonList) { newValue ->
                seasonValue = newValue
                Log.d("rk", seasonValue.toString())
                binding.seasonValue1.text=seasonList[seasonValue]
            }
        }
//        binding.annunalRainfallValue.setOnClickListener {
//            singleValueTypePopUp("Please enter the annual rainfall  value", areaValue) { newValue ->
//                annunal_RainfallValue = newValue
//                Log.d("rk", annunal_RainfallValue.toString())
//                binding.annunalRainfallValue.text=annunal_RainfallValue.toString()
//            }
//        }
        binding.fertilizerValue.setOnClickListener {
            singleValueTypePopUp("Please enter the fertilizer value", areaValue) { newValue ->
                fertilizerValue = newValue
                Log.d("rk", fertilizerValue.toString())
                binding.fertilizerValue1.text=fertilizerValue.toString()
            }
        }
        binding.yeildValue.setOnClickListener {
            singleValueTypePopUp("Please enter the yeild value", areaValue) { newValue ->
                yeildValue = newValue
                Log.d("rk", yeildValue.toString())
                binding.yeildValue1.text=yeildValue.toString()
            }
        }

        binding.predictYield.setOnClickListener {
            showProgressbar()
            viewModel.rainfall(requireContext(),RainfallInputModel(stateList[stateValue],seasonList[seasonValue]),this)
//
        }

        viewModel.observe_cropPrediction().observe(requireActivity(), Observer { res->
            cancelProgressbar()
            if(res.isSuccessful)
            {
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


}