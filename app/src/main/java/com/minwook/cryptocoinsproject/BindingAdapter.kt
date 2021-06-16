package com.minwook.cryptocoinsproject

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("textColor")
    fun bindCoinTextColor(view: TextView, percent: String) {
        when {
            percent.toDouble() > 0 -> view.setTextColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.red
                )
            )
            percent.toDouble() == 0.0 -> view.setTextColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.black
                )
            )
            else -> view.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
        }
    }

    @JvmStatic
    @BindingAdapter("coinImage")
    fun bindCoinImage(view: ImageView, symbol: String) {
        Glide.with(view.context)
            .load("https://cryptoicons.org/api/icon/${symbol}/200".toLowerCase())
            .into(view)
    }

}