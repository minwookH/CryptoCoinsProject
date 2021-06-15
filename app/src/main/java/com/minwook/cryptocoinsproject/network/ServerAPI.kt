package com.minwook.cryptocoinsproject.network

import com.minwook.cryptocoinsproject.data.Ticker
import io.reactivex.Single
import retrofit2.http.GET

interface ServerAPI {

    @GET("ticker/24hr")
    fun getTickerList(): Single<ArrayList<Ticker>>

}