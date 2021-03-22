package com.example.myapplication.repository

import com.example.myapplication.db.FavoriteCompany
import io.reactivex.Flowable
import io.reactivex.Maybe

interface LocalRepo {

    fun getAllFavoriteCompany() : Flowable<List<FavoriteCompany>>

    fun insertTicker(favComp: FavoriteCompany)

    fun updateTicker(favComp: FavoriteCompany)

    fun deleteTicker(favComp: FavoriteCompany)
}