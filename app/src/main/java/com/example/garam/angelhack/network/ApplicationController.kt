package com.example.garam.angelhack.network

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationController : Application(){
    val baseURL = "https://80fac4eb1b11.ngrok.io"
    lateinit var networkService: NetworkService

    companion object{
       lateinit var instance : ApplicationController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        buildNetWork()
    }
    fun buildNetWork(){
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).client(OkHttpClient.Builder().connectTimeout(1,
            TimeUnit.MINUTES).readTimeout(1,TimeUnit.MINUTES).writeTimeout(1,TimeUnit.MINUTES).addInterceptor(HttpLoggingInterceptor()).build()).build()

        networkService = retrofit.create(NetworkService::class.java)
    }
}