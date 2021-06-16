package com.minwook.cryptocoinsproject.repository

import androidx.lifecycle.LiveData
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.db.CoinDAO
import com.minwook.cryptocoinsproject.db.CoinEntity
import com.minwook.cryptocoinsproject.network.ServerAPI
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val serverAPI: ServerAPI,
    private val coinDAO: CoinDAO
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

    fun getBookmarkList(): LiveData<List<CoinEntity>?> {
        return coinDAO.getBookmarkList()
    }

    fun getBookmark(symbol: String): LiveData<CoinEntity?> {
        return coinDAO.getBookmark(symbol)
    }

    fun insertBookmark(coin: CoinEntity) {
        coinDAO.insert(coin)
    }

    fun deleteBookmark(symbol: String) {
        coinDAO.deleteBookmark(symbol)
    }
}