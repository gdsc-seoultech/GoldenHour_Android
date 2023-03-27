package com.gdsc.goldenhour.network.model

import com.gdsc.goldenhour.network.INetworkService
import com.gdsc.goldenhour.network.RetrofitObject
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object DataRetrofit {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)

    // 맵

    private const val DATA_API_URL = "http://apis.data.go.kr"

    val parser = TikXml.Builder()
        .exceptionOnUnreadXml(false)
        .build()

    val retrofitAPI = Retrofit.Builder()
        .baseUrl(DATA_API_URL)
        .addConverterFactory(TikXmlConverterFactory.create(parser))
        .client(RetrofitObject.okHttpClient.build())
        .build()

    val dataService: DataApi = retrofitAPI.create(DataApi::class.java)

}