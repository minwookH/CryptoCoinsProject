package com.minwook.cryptocoinsproject.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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

    fun addTicker(data: Ticker) {
        Log.d("coin", "CoinListAdapter addTicker : ${data}")
        this.list.add(data)
    }

    fun updateList(updateList: ArrayList<Ticker>) {
        val calculateDiff = DiffUtil.calculateDiff(CoinDiffCallback(list, updateList))

        list.clear()
        list.addAll(updateList)

        calculateDiff.dispatchUpdatesTo(this)
    }

    fun getCoinList(): ArrayList<Ticker> = list

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }
}

class CoinDiffCallback(private val oldList: ArrayList<Ticker>, private val newList: ArrayList<Ticker>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].symbol == newList[newItemPosition].symbol
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].lastPrice == newList[newItemPosition].lastPrice
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}