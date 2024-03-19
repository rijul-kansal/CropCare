package com.learning.cropcare.Fragment

import android.app.Dialog
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learning.agrovision.Model.YeildInputModel
import com.learning.cropcare.R
import com.learning.cropcare.ViewModel.APIViewModel
import com.learning.cropcare.databinding.FragmentCropYieldPredictionBinding
import java.time.LocalDate

class CropYieldPrediction : Fragment() {
    lateinit var viewModel: APIViewModel
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
            stateArrFn()
            cropArrFn()
            seasonArrFn()
            binding.areaValue.setOnClickListener {
                singleValueTypePopUp("Please enter the area value", areaValue) { newValue ->
                    areaValue = newValue
                    Log.d("rk", areaValue.toString())
                    binding.areaValue1.text="Area ${areaValue.toString()}"
                }
            }

            binding.stateValue.setOnClickListener {
                singleValueChoosePopUp("Please choose one state value",statesArrayList) { newValue ->
                    stateValue = newValue
                    Log.d("rk", stateValue.toString())
                    binding.stateValue1.text="State ${statesArrayList[stateValue]}"
                }
            }
            binding.cropValue.setOnClickListener {
                singleValueChoosePopUp("Please choose one crop value",cropsArrayList) { newValue ->
                    cropValue = newValue
                    Log.d("rk", cropValue.toString())
                    binding.cropValue1.text="Crop ${cropsArrayList[cropValue]}"
                }
            }
            binding.yearValue.setOnClickListener {
                val currentDate = LocalDate.now()
                val currentYear = currentDate.year
                binding.yearValue1.text=currentYear.toString()
                yearValue=currentYear
            }
            binding.seasonValue.setOnClickListener {
                singleValueChoosePopUp("Please choose one season value",seasonsArrayList) { newValue ->
                    seasonValue = newValue
                    Log.d("rk", seasonValue.toString())
                    binding.seasonValue1.text="Season ${seasonsArrayList[seasonValue]}"
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
            "Meghalaya" to 22
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

}