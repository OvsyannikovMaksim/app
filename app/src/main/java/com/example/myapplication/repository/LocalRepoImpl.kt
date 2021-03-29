package com.example.myapplication.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.myapplication.db.FavoriteCompany
import com.example.myapplication.db.LocalDao
import com.example.myapplication.db.SearchHistory
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class LocalRepoImpl(private val localDao:LocalDao) : LocalRepo {

    override fun getAllFavoriteCompany(): Flowable<List<FavoriteCompany>> {
        return localDao.getAllFavoriteCompany()
    }

    override fun getSearchCompany(): Flowable<List<SearchHistory>> {
        return localDao.getSearchCompany()
    }
    @SuppressLint("CheckResult")
    override fun deleteTicker(favComp: FavoriteCompany) {
        Single.fromCallable {
            localDao.deleteTicker(favComp)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Blog Db: delete was successful")
                }, {
                    Log.d("TAG", "Blog Db: delete wasn't successful")
                    it.printStackTrace()
                })

    }

    @SuppressLint("CheckResult")
    override fun insertTicker(favComp: FavoriteCompany) {
        Log.d("LocalRepoIMPL", "$favComp")
        Single.fromCallable {
            localDao.insertTicker(favComp)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Blog Db: list insertion was successful")
                }, {
                    Log.d("TAG", "Blog Db: list insertion wasn't successful")
                    it.printStackTrace()
                })
    }

    @SuppressLint("CheckResult")
    override fun updateTicker(favComp: FavoriteCompany) {
        Single.fromCallable {
            localDao.updateTicker(favComp)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Ticker: update was successful")
                }, {
                    Log.d("TAG", "Blog Db: update wasn't successful")
                    it.printStackTrace()
                })

    }

    @SuppressLint("CheckResult")
    override fun deleteSearch(lastSearch: SearchHistory) {
        Single.fromCallable {
            localDao.deleteSearch(lastSearch)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Blog Db: delete was successful")
                }, {
                    Log.d("TAG", "Blog Db: delete wasn't successful")
                    it.printStackTrace()
                })

    }

    @SuppressLint("CheckResult")
    override fun insertSearch(lastSearch: SearchHistory) {
        Single.fromCallable {
            localDao.insertSearch(lastSearch)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Blog Db: list insertion was successful")
                }, {
                    Log.d("TAG", "Blog Db: list insertion wasn't successful")
                    it.printStackTrace()
                })
    }

    @SuppressLint("CheckResult")
    override fun updateSearch(lastSearch: SearchHistory) {
        Single.fromCallable {
            localDao.updateSearch(lastSearch)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Blog Db: update was successful")
                }, {
                    Log.d("TAG", "Blog Db: update wasn't successful")
                    it.printStackTrace()
                }).dispose()

    }
}