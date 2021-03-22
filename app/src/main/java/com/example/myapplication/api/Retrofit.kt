package com.example.myapplication.api

object Retrofit{

    private val BASE_URL = "https://cloud.iexapis.com/stable/"
    val finHubApi: IEXCloudApi
        get() = RetrofitClient.getClient(BASE_URL).create(IEXCloudApi::class.java)

}