package com.learning.cropcare.Fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.learning.cropcare.Activity.InDetailHistory
import com.learning.cropcare.Adapter.HistorySummaryAdapter
import com.learning.cropcare.Model.HistorySummaryOutputModel
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.ViewModel.FireStoreDataBaseViewModel
import com.learning.cropcare.databinding.FragmentHistoryBinding


class History : Fragment() {

    lateinit var binding:FragmentHistoryBinding
    lateinit var viewModel:FireStoreDataBaseViewModel
    var allHistodauyDetail: ArrayList<Map<String, String>>?=null
    var dialog: Dialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel=ViewModelProvider(this)[FireStoreDataBaseViewModel::class.java]
        showProgressbar()
        viewModel.getUserHistoryData(requireContext(),this)
        viewModel.observeUserHistoryData().observe(viewLifecycleOwner , Observer {
            res->
            cancelProgressbar()
            if(res!=null)
            {
                allHistodauyDetail=res["array"]
                var historySummaryDisplay :ArrayList<HistorySummaryOutputModel> = ArrayList()
                try {
                    val arr = res["array"]
                    Log.d("rk", arr?.size.toString())
                    for (i in 1..<(res["array"]?.size ?: 0)) {
                        if(res["array"]?.get(i)?.get("value")=="Crop Prediction")
                        historySummaryDisplay.add(HistorySummaryOutputModel(res["array"]?.get(i)?.get("value"),res["array"]?.get(i)?.get("result"),0,res["array"]?.get(i)?.get("date")))
                        else if(res["array"]?.get(i)?.get("value")=="Crop Yield Prediction")
                        historySummaryDisplay.add(HistorySummaryOutputModel(res["array"]?.get(i)?.get("value"),res["array"]?.get(i)?.get("result"),1,res["array"]?.get(i)?.get("date")))
                        else
                        historySummaryDisplay.add(HistorySummaryOutputModel(res["array"]?.get(i)?.get("value"),res["array"]?.get(i)?.get("result"),2,res["array"]?.get(i)?.get("date")))
                    }
                    displayAdapter(historySummaryDisplay)
                    Log.d("rk",historySummaryDisplay.toString())
                } catch (e: Exception) {
                    Log.d("rk", e.message.toString())
                }


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

    fun displayAdapter(lis:ArrayList<HistorySummaryOutputModel>)
    {
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        val ItemAdapter = HistorySummaryAdapter(lis,requireContext())
        binding.recycleView.adapter = ItemAdapter
        ItemAdapter.setOnClickListener(object :
            HistorySummaryAdapter.OnClickListener {
            override fun onClick(position: Int, model: HistorySummaryOutputModel) {
                Log.d("rk",model.title.toString())
                Log.d("rk",position.toString())
                allHistodauyDetail?.get(position+1)?.get("value")?.let { Log.d("rk", it) }
                var arr= allHistodauyDetail!![position+1]
                val gson = Gson()
                val dataString = gson.toJson(arr)
                var intent=Intent(activity,InDetailHistory::class.java)
                intent.putExtra(Constants.HISTORY,dataString)
                startActivity(intent)
            }
        })
    }
}