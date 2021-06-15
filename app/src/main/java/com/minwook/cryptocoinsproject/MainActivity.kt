package com.minwook.cryptocoinsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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
        coinListAdapter = CoinListAdapter()
        binding.rvList.adapter = coinListAdapter
        binding.rvList.layoutManager = LinearLayoutManager(this)
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
