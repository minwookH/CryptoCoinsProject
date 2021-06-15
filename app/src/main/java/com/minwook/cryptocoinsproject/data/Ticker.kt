package com.minwook.cryptocoinsproject.data

data class Ticker(
    var symbol: String,
    var priceChange: String,
    var priceChangePercent: String,
    var lastPrice: String,
    var quoteVolume: String,
    var openPrice: String,
    var highPrice: String,
    var lowPrice: String,
    var closeTime: String,
) {

    val baseAsset: String
        get() = symbol.removeSuffix("BTC")

    val percent: String
        get() {
            val value = priceChangePercent.toDouble()
            return if (value >= 0) "+$value%" else "$value%"
        }

}
