package com.example.myapplication.repository

import com.example.myapplication.db.FavoriteCompany
import com.example.myapplication.db.SearchHistory
import io.reactivex.Flowable

interface LocalRepo {

    fun getAllFavoriteCompany() : Flowable<List<FavoriteCompany>>

    fun insertTicker(favComp: FavoriteCompany)

    fun updateTicker(favComp: FavoriteCompany)

    fun deleteTicker(favComp: FavoriteCompany)

    fun getSearchCompany() : Flowable<List<SearchHistory>>

    fun insertSearch(lastSearch: SearchHistory)

    fun updateSearch(lastSearch: SearchHistory)

    fun deleteSearch(lastSearch: SearchHistory)
}