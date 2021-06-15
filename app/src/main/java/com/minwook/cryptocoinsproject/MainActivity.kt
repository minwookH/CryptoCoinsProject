package com.minwook.cryptocoinsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.minwook.cryptocoinsproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var coinListAdapter: CoinListAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObserve()
        mainViewModel.loadCoinTickerList()
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
                            0 -> {}
                            else -> {}
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

            coinListAdapter = CoinListAdapter()
            rvList.adapter = coinListAdapter
            rvList.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun initObserve() {
        mainViewModel.coinTickers.observe(this, Observer {
            Log.d("coin", "initObserve coinTickers : ${it.size}")
            coinListAdapter.addTickerList(it)
        })
    }

    override fun onDestroy() {
        mainViewModel.disposableAll()
        super.onDestroy()
    }
}
