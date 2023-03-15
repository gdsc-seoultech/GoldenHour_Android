package com.gdsc.goldenhour.network

import com.gdsc.goldenhour.network.model.*
import com.gdsc.goldenhour.view.map.data.AedItemList
import com.gdsc.goldenhour.view.map.data.AedResponse
import retrofit2.Call
import retrofit2.http.*

interface INetworkService {
    @POST("/user")
    fun createUser(
        @Header("Authorization")
        idToken: String
    ): Call<SignInResponse>

    @GET("/guide")
    fun getGuideList(): Call<GuideList>

    @GET("/guide/{id}")
    fun getGuideWebtoonList(@Path("id") id: Int): Call<GuideWebtoonList>

    @GET("/user/emergency_contact")
    fun getContactList(
        @Header("Authorization")
        idToken: String
    ): Call<ContactList>

    @GET("/disaster")
    fun getDisasterList(): Call<DisasterList>

    @GET("/disaster/{id}")
    fun getDisasterWebtoonList(@Path("id") id: Int): Call<DisasterWebtoonList>

    // 비상신고

    @GET("/message/{type}")
    fun getSituationList(@Path("type") type: String): Call<SituationList>

    @GET("/message/{id}/type_situation")
    fun getTypeSituationList(@Path("id") id: Int): Call<TypeSituationList>

    @GET("/message/{id}/detail_situation")
    fun getDetailSituationList(@Path("id") id: Int): Call<DetailSituationList>

    @GET("/message/{id}/information")
    fun getInformationList(@Path("id") id: Int): Call<InformationList>

    // map
    @GET("/B552657/AEDInfoInqireService/getAedLcinfoInqire")
    fun getAedList(
        @Query("WGS84_LON") longitude: Double,
        @Query("WGS84_LAT") latitude: Double,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("Servicekey", encoded = true) serviceKey: String
    ): Call<AedItemList>
}