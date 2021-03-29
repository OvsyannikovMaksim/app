package com.example.myapplication.api

object RetrofitFinHub {


    private const val BASE_URL = "https://finnhub.io/"
    val finHubApi: FinHubApi
        get() = RetrofitClientFinHub.getClient(BASE_URL).create(FinHubApi::class.java)

}