package com.minwook.cryptocoinsproject.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDAO {

    @Query("DELETE FROM COIN_BOOK_MARK_TABLE WHERE symbol == :symbol")
    fun deleteBookmark(symbol: String)

    @Query("SELECT * FROM COIN_BOOK_MARK_TABLE")
    fun getBookmarkList(): LiveData<List<CoinEntity>?>

    @Query("SELECT * FROM COIN_BOOK_MARK_TABLE WHERE symbol == :symbol")
    fun getBookmark(symbol: String): LiveData<CoinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coin: CoinEntity)

}