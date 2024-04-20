package com.learning.cropcare.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.cropcare.Model.HistorySummaryInDetailOutputModel
import com.learning.cropcare.databinding.HistoryInDetailRvBinding

class HistorySummaryInDetailAdapter(
    private val items: List<HistorySummaryInDetailOutputModel>,
    private val context: Context
) :
    RecyclerView.Adapter<HistorySummaryInDetailAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HistoryInDetailRvBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.result.text = item.result
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, item )
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    fun setOnClickListener(onClickListener: HistorySummaryInDetailAdapter.OnClickListener?) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, model: HistorySummaryInDetailOutputModel)
    }
    class ViewHolder(binding: HistoryInDetailRvBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val result=binding.value
    }
}