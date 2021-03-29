package com.example.myapplication.api

object RetrofitIEXCloud{

    private const val BASE_URL = "https://cloud.iexapis.com/stable/"
    val iexCloudApi: IEXCloudApi
        get() = RetrofitClientIEXCloud.getClient(BASE_URL).create(IEXCloudApi::class.java)

}

