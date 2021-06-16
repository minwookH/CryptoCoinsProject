package com.minwook.cryptocoinsproject.di

import android.app.Application
import com.minwook.cryptocoinsproject.constant.Constants
import com.minwook.cryptocoinsproject.network.ServerAPI
import com.minwook.cryptocoinsproject.network.SocketAPI
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val SOCKET_TIMEOUT = 10L

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BINANCE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideServerAPI(retrofit: Retrofit): ServerAPI {
        return retrofit.create(ServerAPI::class.java)
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(SOCKET_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(SOCKET_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(SOCKET_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideScarlet(
        application: Application,
        okHttpClient: OkHttpClient
    ): Scarlet {
        val lifecycle = AndroidLifecycle.ofApplicationForeground(application)

        return Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(Constants.BINANCE_WEB_SOCKET))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .lifecycle(lifecycle)
            .build()
    }

    @Provides
    fun provideSocketAPI(scarlet: Scarlet): SocketAPI {
        return scarlet.create()
    }
}