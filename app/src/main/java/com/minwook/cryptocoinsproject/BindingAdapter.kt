package com.minwook.cryptocoinsproject

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("textColor")
    fun bindGoodImage(view: TextView, percent: String) {
        when {
            percent.toDouble() > 0 -> view.setTextColor(ContextCompat.getColor(view.context, R.color.red))
            percent.toDouble() == 0.0 -> view.setTextColor(ContextCompat.getColor(view.context, R.color.black))
            else -> view.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
        }
    }

}