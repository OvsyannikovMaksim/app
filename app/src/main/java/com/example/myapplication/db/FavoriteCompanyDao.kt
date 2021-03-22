package com.example.myapplication.db

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface FavoriteCompanyDao {

    @Query("SELECT ticker FROM FavoriteCompany")
    fun getAllFavoriteCompany() : Flowable<List<FavoriteCompany>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTicker(ticker: FavoriteCompany)

    @Update
    fun updateTicker(ticker: FavoriteCompany)

    @Delete
    fun deleteTicker(ticker: FavoriteCompany)

}