package com.minwook.cryptocoinsproject.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.databinding.ActivityDetailBinding
import com.minwook.cryptocoinsproject.db.CoinEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COIN = "EXTRA_COIN"
    }

    private lateinit var binding: ActivityDetailBinding
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()
    private var extraCoin: Ticker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initExtra()
        initView()
        initObserve()
    }

    private fun initExtra() {
        if (intent.hasExtra(EXTRA_COIN)) {
            extraCoin = intent.getParcelableExtra(EXTRA_COIN)
        }
    }

    private fun initView() {
        binding.apply {
            coin = extraCoin
            swBookmark.setOnCheckedChangeListener { buttonView, isChecked ->
                coin?.let {
                    Log.d("coin", "initView swBookmark : $isChecked")
                    if (isChecked) {
                        bookmarkViewModel.insertBookmark(CoinEntity(it.symbol, true))
                    } else {
                        bookmarkViewModel.deletBookmark(it.symbol)
                    }
                }
            }
        }
    }

    private fun initObserve() {
        extraCoin?.let {
            bookmarkViewModel.getBookmark(it.symbol).observe(this, { coin ->
                coin?.let {
                    Log.d("coin", "initObserve coin : $coin")
                    binding.swBookmark.isChecked = it.isBookmark
                }
            })
        }
    }

    override fun onDestroy() {
        bookmarkViewModel.disposableAll()
        super.onDestroy()
    }
}