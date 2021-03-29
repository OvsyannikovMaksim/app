package com.example.myapplication.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.constraintlayout.solver.Cache
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitClientIEXCloud {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit{
        if (retrofit ==null){
            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!
    }
}