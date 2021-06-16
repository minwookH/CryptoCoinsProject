package com.minwook.cryptocoinsproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.minwook.cryptocoinsproject.data.Ticker
import com.minwook.cryptocoinsproject.databinding.ActivityMainBinding
import com.minwook.cryptocoinsproject.ui.BookmarkViewModel
import com.minwook.cryptocoinsproject.ui.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var coinListAdapter: CoinListAdapter
    private val mainViewModel by viewModels<MainViewModel>()
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObserve()
        mainViewModel.loadCoinTickerList()
        mainViewModel.loadBitcoinData()
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
                            }
                            else -> {
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
        }
    }

    private fun initObserve() {
        mainViewModel.coinTickers.observe(this, Observer {
            coinListAdapter.addTickerList(it)
            binding.tvSplash.visibility = View.GONE
            binding.clMain.visibility = View.VISIBLE
        })

        mainViewModel.bitCoinTicker.observe(this, Observer {
            binding.tvBitcoinPrice.text = "${it.lastPrice}\n${it.percent}"
            binding.tvBitcoinPrice.setTextColor(it.color)
        })

        mainViewModel.error.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        bookmarkViewModel.getBookmarkList().observe(this, {
            Log.d("coin", "--  --  MainActivity getBookmarkList : ${it}")
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
}
