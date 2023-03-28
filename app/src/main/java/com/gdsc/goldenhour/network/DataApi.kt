package com.gdsc.goldenhour.network

import com.gdsc.goldenhour.network.model.AedResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DataApi {
    // map
    @GET("/B552657/AEDInfoInqireService/getAedLcinfoInqire")
    fun getAedList(
        @Query("WGS84_LON") longitude: Double,
        @Query("WGS84_LAT") latitude: Double,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("serviceKey", encoded = true) serviceKey: String
    ): Call<AedResponse>
}