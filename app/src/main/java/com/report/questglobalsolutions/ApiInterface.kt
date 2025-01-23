package com.report.questglobalsolutions

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @POST("/users/login")
    fun postLogin(
        @Body reqBody: LoginBodyClass
    ): Call<LoginResponseClass>

    @GET("/users/home")
    fun getHome(
        @Header("Authorization") reqBody: String
    ): Call<HomeBodyClass>

    @GET("/users/profile")
    fun getProfile(
        @Header("Authorization") reqBody: String
    ): Call<ProfileBodyClass>
}