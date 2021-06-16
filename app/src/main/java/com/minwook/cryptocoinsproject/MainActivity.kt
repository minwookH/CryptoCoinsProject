package com.minwook.cryptocoinsproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.tabs.TabLayout
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.databinding.ActivityMainBinding
import com.minwook.cryptocoinsproject.db.CoinEntity
import com.minwook.cryptocoinsproject.ui.detail.BookmarkViewModel
import com.minwook.cryptocoinsproject.ui.detail.DetailActivity
import com.minwook.cryptocoinsproject.ui.main.CoinListAdapter
import com.minwook.cryptocoinsproject.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    enum class SortStatus {
        ASCENDING, // 오름차순
        DESCENDING, // 내림차순
        NONE
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var coinListAdapter: CoinListAdapter

    private val mainViewModel by viewModels<MainViewModel>()
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    private var coinList = arrayListOf<Ticker>()
    private var bookmarkList = arrayListOf<CoinEntity>()
    private var coinNameSortStatus = SortStatus.NONE
    private var coinPriceSortStatus = SortStatus.NONE
    private var coinPersentSortStatus = SortStatus.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObserve()
        mainViewModel.loadCoinTickerList()
        mainViewModel.loadBitcoinData()
        bookmarkViewModel.getBookmarkList()
    }

    private fun initView() {
        binding.apply {
            tlTab.addTab(tlTab.newTab().setText("BTC"))
            tlTab.addTab(tlTab.newTab().setText("즐겨찾기"))
            tlTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        Log.d("coin", "initView onTabSelected : ${it.position}")

                        when (it.position) {
                            0 -> {
                                coinListAdapter.clear()
                                coinListAdapter.addTickerList(coinList)
                            }
                            else -> {
                                bookmarkListSync()
                            }
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            coinListAdapter = CoinListAdapter().apply {
                onClickContents = { coin ->
                    Log.d("coin", "CoinListAdapter onClickContents : $coin")
                    goDetailActivity(coin)
                }
            }
            rvList.adapter = coinListAdapter
            rvList.layoutManager = LinearLayoutManager(this@MainActivity)
            (rvList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            // 이름 정렬
            tvCoinNameTitle.setOnClickListener {
                coinListAdapter.clear()
                coinNameSortStatus = when (coinNameSortStatus) {
                    SortStatus.NONE, SortStatus.ASCENDING -> {
                        coinListAdapter.addTickerList(coinList.sortedByDescending { it.symbol })
                        tvCoinNameTitle.text = "${tvCoinNameTitle.text.removeSuffix("(오름)")}(내림)"
                        removeSortText(tvPresentPriceTitle)
                        removeSortText(tvChangeRateTitle)
                        SortStatus.DESCENDING
                    }
                    else -> {
                        coinListAdapter.addTickerList(coinList.sortedBy { it.symbol })
                        tvCoinNameTitle.text = "${tvCoinNameTitle.text.removeSuffix("(내림)")}(오름)"
                        removeSortText(tvPresentPriceTitle)
                        removeSortText(tvChangeRateTitle)
                        SortStatus.ASCENDING
                    }
                }
            }

            // 가격 정렬
            tvPresentPriceTitle.setOnClickListener {
                coinListAdapter.clear()
                coinPriceSortStatus = when (coinPriceSortStatus) {
                    SortStatus.NONE, SortStatus.ASCENDING -> {
                        coinListAdapter.addTickerList(coinList.sortedByDescending { it.lastPrice })
                        tvPresentPriceTitle.text = "${tvPresentPriceTitle.text.removeSuffix("(오름)")}(내림)"
                        removeSortText(tvCoinNameTitle)
                        removeSortText(tvChangeRateTitle)
                        SortStatus.DESCENDING
                    }
                    else -> {
                        coinListAdapter.addTickerList(coinList.sortedBy { it.symbol })
                        tvPresentPriceTitle.text = "${tvPresentPriceTitle.text.removeSuffix("(내림)")}(오름)"
                        removeSortText(tvCoinNameTitle)
                        removeSortText(tvChangeRateTitle)
                        SortStatus.ASCENDING
                    }
                }
            }

            // 변동률 정렬
            tvChangeRateTitle.setOnClickListener {
                coinListAdapter.clear()
                coinPersentSortStatus = when (coinPersentSortStatus) {
                    SortStatus.NONE, SortStatus.ASCENDING -> {
                        coinListAdapter.addTickerList(coinList.sortedByDescending { it.priceChangePercent })
                        tvChangeRateTitle.text = "${tvChangeRateTitle.text.removeSuffix("(오름)")}(내림)"
                        removeSortText(tvCoinNameTitle)
                        removeSortText(tvPresentPriceTitle)
                        SortStatus.DESCENDING
                    }
                    else -> {
                        coinListAdapter.addTickerList(coinList.sortedBy { it.symbol })
                        tvChangeRateTitle.text = "${tvChangeRateTitle.text.removeSuffix("(내림)")}(오름)"
                        removeSortText(tvCoinNameTitle)
                        removeSortText(tvPresentPriceTitle)
                        SortStatus.ASCENDING
                    }
                }
            }
        }
    }

    private fun initObserve() {
        mainViewModel.coinTickers.observe(this, Observer {
            coinList.clear()
            coinList.addAll(it)
            coinListAdapter.addTickerList(it)
            binding.tvSplash.visibility = View.GONE
            binding.clMain.visibility = View.VISIBLE
        })

        mainViewModel.bitCoinTicker.observe(this, Observer {
            Log.d("coin", "bitCoinTicker : ${it.lastPrice}\n${it.percent}")
            binding.tvBitcoinPrice.text = "${it.lastPrice}\n${it.percent}"
            binding.tvBitcoinPrice.setTextColor(it.color)
        })

        mainViewModel.error.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        bookmarkViewModel.getBookmarkList().observe(this, {
            Log.d("coin", "--  --  MainActivity getBookmarkList : ${it}")
            bookmarkList.clear()

            if (!it.isNullOrEmpty()) {
                bookmarkList.addAll(it)
            }

            if (binding.tlTab.selectedTabPosition == 1) {
                bookmarkListSync()
            }
        })
    }

    override fun onDestroy() {
        mainViewModel.disposableAll()
        super.onDestroy()
    }

    private fun goDetailActivity(coin: Ticker) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_COIN, coin)
        }
        startActivity(intent)
    }

    private fun bookmarkListSync() {
        val tempBookmarkList = arrayListOf<Ticker>()

        bookmarkList.forEach {
            coinList.find { coin ->
                coin.symbol == it.symbol
            }?.let {
                tempBookmarkList.add(it)
            }
        }

        coinListAdapter.updateList(tempBookmarkList)
    }

    private fun removeSortText(view: TextView) {
        view.text = "${view.text.toString().replace("(오름)", "")}".replace("(내림)", "")
    }
}
