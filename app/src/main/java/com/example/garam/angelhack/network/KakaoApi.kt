package com.example.garam.angelhack.network

import android.app.Application
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KakaoApi : Application() {
    val base = "https://kapi.kakao.com"
    val key = "KakaoAK 15896e1e908f60dccf79b49fa9b8987c"
    lateinit var networkService: NetworkService

    companion object{
        var instance : KakaoApi = KakaoApi()
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        buildNetWork()
    }

    fun buildNetWork(){
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(base).addConverterFactory(
            GsonConverterFactory.create()).build()
        networkService = retrofit.create(NetworkService::class.java)
    }
}