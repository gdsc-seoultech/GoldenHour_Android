package com.gdsc.goldenhour.network

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit

object DataRetrofit {
    private const val DATA_API_URL = "http://apis.data.go.kr"

    val parser = TikXml.Builder()
        .exceptionOnUnreadXml(false)
        .build()

    val retrofitAPI = Retrofit.Builder()
        .baseUrl(DATA_API_URL)
        .addConverterFactory(TikXmlConverterFactory.create(parser))
        .build()

    val dataService: DataApi = retrofitAPI.create(DataApi::class.java)
}