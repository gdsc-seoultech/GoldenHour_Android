package com.gdsc.goldenhour.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// 싱글톤 패턴으로 객체가 한번만 생성되도록
object RetrofitObject {
    private const val SERVER_URL = "http://3.39.32.165:8080"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)

    private val retrofit = Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient.build())
        .build()


    val networkService: INetworkService = retrofit.create(INetworkService::class.java)

    // api

    private const val DATA_API_URL = "http://apis.data.go.kr"

    val retrofitAPI = Retrofit.Builder()
        .baseUrl(DATA_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient.build())
        .build()

    val dataService: INetworkService = retrofitAPI.create(INetworkService::class.java)
}


