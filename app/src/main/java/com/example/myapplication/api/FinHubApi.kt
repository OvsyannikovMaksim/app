package com.example.myapplication.api

import com.example.myapplication.common.SearchInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FinHubApi {

    @GET("api/v1/search")
    fun doSearch(@Query("q") symbol: String,
                 @Query("token") api_token: String) : Observable<SearchInfo>
}