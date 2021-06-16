package com.minwook.cryptocoinsproject.ui.detail

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
class DetaillViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    private val _bookmark = MutableLiveData<List<CoinEntity>>()
    val bookmark: LiveData<List<CoinEntity>>
        get() = _bookmark

    private val compositeDisposable = CompositeDisposable()

    fun getBookmarkList(): LiveData<List<CoinEntity>?> {
        Log.d("coin", "loadBookmarkList 1 : ")
        return coinRepository.getBookmarkList()
    }

    fun deletBookmark(symbol: String) {
        Log.d("coin", "deletBookmark 1 : $symbol")
        compositeDisposable.add(
            Flowable.just(symbol).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({
                    Log.d("coin", "deletBookmark 2 : $symbol")
                    coinRepository.deleteBookmark(it)
                }, {
                    Log.e("coin", it.localizedMessage)
                })
        )
    }

    fun insertBookmark(coin: CoinEntity) {
        Log.d("coin", "insertBookmark 1 : $coin")
        compositeDisposable.add(
            Flowable.just(coin).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({
                    Log.d("coin", "insertBookmark 2 : $coin")
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