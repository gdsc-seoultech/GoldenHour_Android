package com.gdsc.goldenhour.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 싱글톤 패턴으로 객체가 한번만 생성되도록
object RetrofitObject {
    private const val SERVER_URL = "http://3.39.32.165:8080"

    private val retrofit = Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val networkService: INetworkService = retrofit.create(INetworkService::class.java)
}