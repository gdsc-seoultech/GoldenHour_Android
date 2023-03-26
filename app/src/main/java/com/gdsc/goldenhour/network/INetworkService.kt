package com.gdsc.goldenhour.network

import com.gdsc.goldenhour.network.model.*
import com.gdsc.goldenhour.network.model.AedResponse
import retrofit2.Call
import retrofit2.Response
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

    // 대피소
    @GET("/1741000/TsunamiShelter3/getTsunamiShelter1List")
    fun getShelterList(
        @Query("ServiceKey") serviceKey: String,
        @Query("type") type: String = "json",
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 10
    ): Call<ShelterBody>
}