package com.learning.cropcare.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.learning.cropcare.Adapter.HistorySummaryInDetailAdapter
import com.learning.cropcare.Model.HistorySummaryInDetailOutputModel
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.databinding.ActivityInDetailHistoryBinding
import java.util.Locale

class InDetailHistory : AppCompatActivity() {
    lateinit var binding:ActivityInDetailHistoryBinding
    lateinit var maintitle:String
    lateinit var result:String
    var dataRv:ArrayList<HistorySummaryInDetailOutputModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityInDetailHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try {
            val dataString = intent.getStringExtra(Constants.HISTORY)
            val gson = Gson()
            val type = object : TypeToken<HashMap<String, String>>() {}.type
            val dataMap: HashMap<String, String> = gson.fromJson(dataString, type)
            Log.d("rk",dataMap.toString())

            for(i in dataMap)
            {
                if(i.key == "value")
                {
                    maintitle=i.value
                }
                else if(i.key == "result")
                {
                    result=i.value
                }
                else
                {
                    dataRv.add(HistorySummaryInDetailOutputModel(i.key.uppercase(Locale.ROOT),i.value))
                }
            }
            binding.mainTitle.text=maintitle
            dataRv.add(HistorySummaryInDetailOutputModel("Result" , result))
            displayAdapter(dataRv)
        }catch (e:Exception)
        {
            Log.d("rk",e.message.toString())
        }

    }

    fun displayAdapter(lis:ArrayList<HistorySummaryInDetailOutputModel>)
    {
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val ItemAdapter = HistorySummaryInDetailAdapter(lis,this)
        binding.recycleView.adapter = ItemAdapter
    }
}