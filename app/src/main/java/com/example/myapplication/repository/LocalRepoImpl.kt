package com.example.myapplication.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myapplication.db.FavoriteCompany
import com.example.myapplication.db.FavoriteCompanyDao
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class LocalRepoImpl(private val favCompDao: FavoriteCompanyDao) : LocalRepo {

    override fun getAllFavoriteCompany(): Flowable<List<FavoriteCompany>> {
        return favCompDao.getAllFavoriteCompany()
    }

    @SuppressLint("CheckResult")
    override fun deleteTicker(favComp: FavoriteCompany) {
        Single.fromCallable {
            favCompDao.deleteTicker(favComp)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Blog Db: list insertion was successful")
                }, {
                    Log.d("TAG", "Blog Db: list insertion wasn't successful")
                    it.printStackTrace()
                })

    }

    @SuppressLint("CheckResult")
    override fun insertTicker(favComp: FavoriteCompany) {
        Single.fromCallable {
            favCompDao.insertTicker(favComp)
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
            favCompDao.updateTicker(favComp)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG", "Blog Db: list insertion was successful")
                }, {
                    Log.d("TAG", "Blog Db: list insertion wasn't successful")
                    it.printStackTrace()
                })

    }
}