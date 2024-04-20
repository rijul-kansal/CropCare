package com.learning.cropcare.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.cropcare.Model.HistorySummaryOutputModel
import com.learning.cropcare.R
import com.learning.cropcare.databinding.HistorySummaryBinding

class HistorySummaryAdapter(
    private val items: List<HistorySummaryOutputModel>,
    private val context: Context
) :
    RecyclerView.Adapter<HistorySummaryAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HistorySummaryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.result.text = item.result
        holder.date.text = item.date
        if(item.type==0)
        {
            Glide
                .with(context)
                .load(R.drawable.crop_prediction_icon)
                .centerCrop()
                .placeholder(R.drawable.main_farmer_image)
                .into(holder.imageView)
        }
        else if(item.type==1)
        {
            if(item.type==0)
            {
                Glide
                    .with(context)
                    .load(R.drawable.crop_yield_icon)
                    .centerCrop()
                    .placeholder(R.drawable.main_farmer_image)
                    .into(holder.imageView)
            }
        }
        else if(item.type==2)
        {
            Glide
                .with(context)
                .load(R.drawable.fertilizer_recomm_icon)
                .centerCrop()
                .placeholder(R.drawable.main_farmer_image)
                .into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, item )
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    fun setOnClickListener(onClickListener: HistorySummaryAdapter.OnClickListener?) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, model: HistorySummaryOutputModel)
    }
    class ViewHolder(binding: HistorySummaryBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val imageView = binding.imageView
        val result=binding.result
        val date= binding.date
    }
}