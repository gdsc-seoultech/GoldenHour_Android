package com.gdsc.goldenhour.network

import com.gdsc.goldenhour.network.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("/disaster/{name}")
    fun getDisasterWebtoonList(@Path("name") name: String): Call<DisasterWebtoonList>

    @GET("/user/emergency_contact")
    fun getContactList(
        @Header("Authorization")
        idToken: String
    ): Call<ContactList>
}