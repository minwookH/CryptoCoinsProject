package com.minwook.cryptocoinsproject.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.minwook.cryptocoinsproject.R
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable {

    val baseAsset: String
        get() = symbol.removeSuffix("BTC")

    val percent: String
        get() {
            val value = priceChangePercent.toDouble()
            return if (value > 0) {
                "+$value%"
            } else {
                "$value%"
            }
        }
}

data class SocketTicker(
    @SerializedName("E")
    var eventTime: String,
    @SerializedName("s")
    var symbol: String,
    @SerializedName("p")
    var priceChange: String,
    @SerializedName("P")
    var priceChangePercent: String,
    @SerializedName("c")
    var lastPrice: String,
    @SerializedName("q")
    var volume: String,
    @SerializedName("o")
    var openPrice: String,
    @SerializedName("h")
    var highPrice: String,
    @SerializedName("l")
    var lowPrice: String,
) {
    val percent: String
        get() {
            val value = priceChangePercent.toDouble()
            return if (value > 0) {
                "+$value%"
            } else {
                "$value%"
            }
        }

    val color: Int
        get() {
            return when {
                priceChangePercent.toDouble() > 0 -> R.color.red
                priceChangePercent.toDouble() == 0.0 -> R.color.black
                else -> R.color.blue
            }
        }
}
