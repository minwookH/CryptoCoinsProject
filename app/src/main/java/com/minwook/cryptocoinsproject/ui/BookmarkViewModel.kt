package com.minwook.cryptocoinsproject.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minwook.cryptocoinsproject.db.CoinEntity
import com.minwook.cryptocoinsproject.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    private val _bookmark = MutableLiveData<List<CoinEntity>>()
    val bookmark: LiveData<List<CoinEntity>>
        get() = _bookmark

    private val compositeDisposable = CompositeDisposable()

    fun getBookmarkList(): LiveData<List<CoinEntity>?> {
        return coinRepository.getBookmarkList()
    }


    fun getBookmark(symbol: String): LiveData<CoinEntity?> {
        return coinRepository.getBookmark(symbol)
    }

    fun deletBookmark(symbol: String) {
        compositeDisposable.add(
            Flowable.just(symbol).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({
                    coinRepository.deleteBookmark(it)
                }, {
                    Log.e("coin", it.localizedMessage)
                })
        )
    }

    fun insertBookmark(coin: CoinEntity) {
        compositeDisposable.add(
            Flowable.just(coin).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({
                    coinRepository.insertBookmark(it)
                }, {
                    Log.e("coin", it.localizedMessage)
                })
        )
    }

    fun disposableAll() {
        compositeDisposable.dispose()
    }
}