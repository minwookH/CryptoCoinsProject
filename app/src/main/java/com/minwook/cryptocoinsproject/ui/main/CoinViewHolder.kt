package com.minwook.cryptocoinsproject.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.databinding.ListItemCoinBinding

class CoinViewHolder(private var bind: ListItemCoinBinding) : RecyclerView.ViewHolder(bind.root) {

    var onClick: ((Ticker) -> Unit)? = null

    fun bind(ticker: Ticker) {
        bind.ticker = ticker
        bind.clCoinContent.setOnClickListener { onClick?.invoke(ticker) }
    }
}