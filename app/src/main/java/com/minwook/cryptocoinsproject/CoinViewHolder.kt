package com.minwook.cryptocoinsproject

import androidx.recyclerview.widget.RecyclerView
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.databinding.ListItemCoinBinding

class CoinViewHolder(private var bind: ListItemCoinBinding) : RecyclerView.ViewHolder(bind.root) {

    fun bind(ticker: Ticker) {
        bind.ticker = ticker
    }
}