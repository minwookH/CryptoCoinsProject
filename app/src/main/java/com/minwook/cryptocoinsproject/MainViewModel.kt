package com.minwook.cryptocoinsproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.minwook.cryptocoinsproject.constant.Constants
import com.minwook.cryptocoinsproject.data.SocketTicker
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    private lateinit var observeBitCoinEvent: WebSocket

    private val _coinTickers = MutableLiveData<List<Ticker>>()
    val coinTickers: LiveData<List<Ticker>>
        get() = _coinTickers

    private val _bitCoinTicker = MutableLiveData<SocketTicker>()
    val bitCoinTicker: LiveData<SocketTicker>
        get() = _bitCoinTicker

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val compositeDisposable = CompositeDisposable()

    fun loadCoinTickerList() {
        compositeDisposable.add(
            coinRepository.getTickerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _coinTickers.value = it.filter { ticker -> ticker.symbol.endsWith("BTC") }
                }, {
                    Log.e("coin", "loadCoinTickerList error : ${it.localizedMessage}")
                })
        )
    }

    fun loadBitcoinData() {
        //ETHBTC
        observeBitCoinEvent = coinRepository.observeBitCoinEvent(
            "${Constants.BINANCE_WEB_SOCKET}${"BTCUSDT".toLowerCase()}@ticker",
            object : WebSocketListener() {
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e("coin", "onFailure : ${t.localizedMessage}")
                    _error.postValue(t.localizedMessage)
                    super.onFailure(webSocket, t, response)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    _bitCoinTicker.postValue(Gson().fromJson(text, SocketTicker::class.java))
                    super.onMessage(webSocket, text)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                }

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                }
            })
    }

    fun disposableAll() {
        compositeDisposable.dispose()
        observeBitCoinEvent.close(1000, null)
    }
}