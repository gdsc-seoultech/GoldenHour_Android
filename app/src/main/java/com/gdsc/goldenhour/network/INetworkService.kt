package com.gdsc.goldenhour.network

import com.gdsc.goldenhour.network.model.*
import retrofit2.Call
import retrofit2.http.*

interface INetworkService {
    @POST("/user")
    fun createUser(
        @Header("Authorization")
        idToken: String
    ): Call<SignInResponse>

    @POST("/user/relief_goods")
    fun createReliefGoods(
        @Header("Authorization")
        idToken: String,
        @Body name: GoodsRequest
    ): Call<GoodsCreateResponse>

    @GET("/user/relief_goods")
    fun readReliefGoods(
        @Header("Authorization")
        idToken: String
    ): Call<GoodsReadResponse>

    @PUT("/user/relief_goods/{id}")
    fun updateReliefGoods(
        @Header("Authorization")
        idToken: String,
        @Path("id") id: Int,
        @Body goodsRequest: GoodsRequest
    ): Call<GoodsUpdateResponse>

    @DELETE("/user/relief_goods/{id}")
    fun deleteReliefGoods(
        @Header("Authorization")
        idToken: String,
        @Path("id") id: Int
    ): Call<GoodsDeleteResponse>

    @GET("/user/emergency_contact")
    fun readEmergencyContacts(
        @Header("Authorization")
        idToken: String
    ): Call<ContactReadResponse>

    @POST("/user/emergency_contact")
    fun createEmergencyContact(
        @Header("Authorization")
        idToken: String,
        @Body contactRequest: ContactRequest
    ): Call<ContactCreateResponse>

    @GET("/guide")
    fun getGuideList(): Call<GuideList>

    @GET("/guide/{id}")
    fun getGuideWebtoonList(@Path("id") id: Int): Call<WebtoonList>

    @GET("/disaster")
    fun getDisasterList(): Call<DisasterList>

    @GET("/disaster/{id}")
    fun getDisasterWebtoonListById(@Path("id") id: Int): Call<WebtoonList>

    @GET("/disaster/")
    fun getDisasterWebtoonListByName(@Query("name") name: String): Call<WebtoonList>
    
    @GET("/message/{type}")
    fun getSituationList(@Path("type") type: String): Call<SituationList>

    @GET("/message/{id}/type_situation")
    fun getTypeSituationList(@Path("id") id: Int): Call<TypeSituationList>

    @GET("/message/{id}/detail_situation")
    fun getDetailSituationList(@Path("id") id: Int): Call<DetailSituationList>

    @GET("/message/{id}/information")
    fun getInformationList(@Path("id") id: Int): Call<InformationList>
}