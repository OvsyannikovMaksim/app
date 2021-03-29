package com.example.myapplication.api

import com.example.myapplication.common.CompanyInfoSrc
import com.example.myapplication.common.SearchInfo
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IEXCloudApi {

    @GET("stock/{symbol}/quote")
    fun getCompanyInfo(@Path("symbol") symbol: String,
                       @Query("token") api_token: String): Flowable<CompanyInfoSrc>

    @GET("stock/market/list/mostactive")
    fun getPopularCompany(@Query("token") api_token: String): Flowable<List<CompanyInfoSrc>>

    @GET("stock/market/list/gainers")
    fun getGainersCompany(@Query("token") api_token: String): Flowable<List<CompanyInfoSrc>>


}