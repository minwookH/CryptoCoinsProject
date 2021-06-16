package com.minwook.cryptocoinsproject.di

import android.content.Context
import androidx.room.Room
import com.minwook.cryptocoinsproject.db.CoinDAO
import com.minwook.cryptocoinsproject.db.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideChannelDao(coinDatabase: CoinDatabase): CoinDAO {
        return coinDatabase.coinDAO()
    }
    
    @Provides
    @Singleton
    fun provideCoinDatabase(@ApplicationContext appContext: Context): CoinDatabase {
        return Room.databaseBuilder(
            appContext,
            CoinDatabase::class.java,
            "coin.db"
        ).build()
    }
}