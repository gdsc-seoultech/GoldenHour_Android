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
    ): Call<GoodsPostResponse>

    @GET("/user/relief_goods")
    fun readReliefGoods(
        @Header("Authorization")
        idToken: String
    ): Call<GoodsGetResponse>

    @PUT("/user/relief_goods/{id}")
    fun updateReliefGoods(
        @Header("Authorization")
        idToken: String,
        @Path("id") id: Int,
        @Body name: GoodsRequest
    ): Call<GoodsPutResponse>

    @DELETE("/user/relief_goods/{id}")
    fun deleteReliefGoods(
        @Header("Authorization")
        idToken: String,
        @Path("id") id: Int
    ): Call<GoodsDeleteResponse>

    @GET("/user/emergency_contact")
    fun getContactList(
        @Header("Authorization")
        idToken: String
    ): Call<ContactList>

    @GET("/guide")
    fun getGuideList(): Call<GuideList>

    @GET("/guide/{id}")
    fun getGuideWebtoonList(@Path("id") id: Int): Call<GuideWebtoonList>

    @GET("/disaster")
    fun getDisasterList(): Call<DisasterList>

    @GET("/disaster/{id}")
    fun getDisasterWebtoonList(@Path("id") id: Int): Call<DisasterWebtoonList>
    
    @GET("/message/{type}")
    fun getSituationList(@Path("type") type: String): Call<SituationList>

    @GET("/message/{id}/type_situation")
    fun getTypeSituationList(@Path("id") id: Int): Call<TypeSituationList>

    @GET("/message/{id}/detail_situation")
    fun getDetailSituationList(@Path("id") id: Int): Call<DetailSituationList>

    @GET("/message/{id}/information")
    fun getInformationList(@Path("id") id: Int): Call<InformationList>
}