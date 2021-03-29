package com.example.myapplication.db

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface LocalDao {

    @Query("SELECT ticker FROM FavoriteCompany")
    fun getAllFavoriteCompany() : Flowable<List<FavoriteCompany>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTicker(ticker: FavoriteCompany)

    @Update
    fun updateTicker(ticker: FavoriteCompany)

    @Delete
    fun deleteTicker(ticker: FavoriteCompany)

    @Query("SELECT search FROM SearchHistory LIMIT 11")
    fun getSearchCompany() : Flowable<List<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSearch(lastSearch: SearchHistory)

    @Update
    fun updateSearch(lastSearch: SearchHistory)

    @Delete
    fun deleteSearch(lastSearch: SearchHistory)

}