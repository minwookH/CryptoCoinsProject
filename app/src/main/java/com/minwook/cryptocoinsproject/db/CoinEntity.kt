package com.minwook.cryptocoinsproject.db

import androidx.room.Entity

@Entity(tableName = "COIN_BOOK_MARK_TABLE", primaryKeys = ["symbol"])
data class CoinEntity(
    val symbol: String,
    val isBookmark: Boolean
)