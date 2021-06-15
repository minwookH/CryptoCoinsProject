package com.minwook.cryptocoinsproject.network

import com.minwook.cryptocoinsproject.data.ExchangeInfo
import com.minwook.cryptocoinsproject.data.Ticker
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET

interface ServerAPI {

    @GET("ticker/24hr")
    fun getTickerList(): Single<ArrayList<Ticker>>

}