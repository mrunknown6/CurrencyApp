package com.example.currencyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.databinding.ItemCurrencyDataListBinding
import com.example.currencyapp.models.CurrencyData

class CurrencyDataListAdapter(private val currencyDataList: MutableList<CurrencyData>) :
    RecyclerView.Adapter<CurrencyDataListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ItemCurrencyDataListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_currency_data_list,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount() = currencyDataList.size

    inner class ViewHolder(private val binding: ItemCurrencyDataListBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var currencyData: CurrencyData

        fun onBind() {
            currencyData = currencyDataList[adapterPosition]
            binding.currencyData = currencyData
        }
    }
}