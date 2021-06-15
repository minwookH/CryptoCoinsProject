package com.minwook.cryptocoinsproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    private val _coinTickers = MutableLiveData<List<Ticker>>()
    val coinTickers: LiveData<List<Ticker>>
        get() = _coinTickers

    private val compositeDisposable = CompositeDisposable()

    fun loadCoinTickerList() {
        compositeDisposable.add(
            coinRepository.getTickerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("coin", "loadCoinTickerList size : ${it.size}")
                    _coinTickers.value = it.filter { ticker -> ticker.symbol.endsWith("BTC") }
                }, {
                    Log.e("coin", "loadCoinTickerList error : ${it.localizedMessage}")
                })
        )
    }

    fun disposableAll() {
        compositeDisposable.dispose()
    }
}