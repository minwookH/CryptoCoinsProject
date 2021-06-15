package com.minwook.cryptocoinsproject.repository

import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.network.ServerAPI
import io.reactivex.Single
import javax.inject.Inject


class CoinRepository @Inject constructor(private val apiService: ServerAPI) {

    fun getTickerList(): Single<ArrayList<Ticker>> {
        return apiService.getTickerList()
    }

}