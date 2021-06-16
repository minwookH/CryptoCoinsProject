package com.minwook.cryptocoinsproject.repository

import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.network.ServerAPI
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val serverAPI: ServerAPI
) {

    fun getTickerList(): Single<ArrayList<Ticker>> {
        return serverAPI.getTickerList()
    }

    fun observeBitCoinEvent(url: String, webSocketListener: WebSocketListener): okhttp3.WebSocket {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val newWebSocket = okHttpClient.newWebSocket(request, webSocketListener)
        okHttpClient.dispatcher.executorService.shutdown()

        return newWebSocket
    }
}