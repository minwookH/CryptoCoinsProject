package com.minwook.cryptocoinsproject

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.databinding.ListItemCoinBinding

class CoinListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Ticker> = arrayListOf()

    var onClickContents: ((Ticker) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bind = ListItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(bind).apply {
            onClick = { coin -> onClickContents?.invoke(coin) }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CoinViewHolder).bind(list[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addTickerList(list: List<Ticker>) {
        Log.d("coin", "CoinListAdapter addTickerList : ${list.size}")
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }
}