package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.db.FavoriteCompany
import com.example.myapplication.repository.CompanyRepo
import com.example.myapplication.repository.LocalRepo
import com.example.myapplication.util.CompanyMapper
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class StockViewModel(private var companyRepo: CompanyRepo, private var localRepo: LocalRepo): ViewModel(){

    private lateinit var disposableStocks: Disposable
    var companyList: MutableLiveData<List<CompanyInfoDst>> = MutableLiveData()
    private var mapper : CompanyMapper = CompanyMapper()

    init{
        loadData()
    }


    private fun loadData() {

        disposableStocks = companyRepo.getPopularCompany()
                .subscribeOn(Schedulers.io())
                .doOnNext { s->Log.d("GOVNO", ""+s) }
                .withLatestFrom(localRepo.getAllFavoriteCompany()){ comps, favLst->
                    val compDstList:MutableList<CompanyInfoDst> = mutableListOf()
                    for(comp in comps){
                        val compDst = mapper.map(comp)
                        if(favLst.contains(FavoriteCompany(compDst.ticker))){
                            compDst.isFavorite=true
                        }
                        compDstList.add(compDst)
                    }
                    return@withLatestFrom compDstList
                }
                .subscribe({ v -> companyList.postValue(v) },
                        { error -> Log.d("StockViewModel", "Error in downloading: " + error.message) })
    }

    fun clear(){
        disposableStocks.dispose()
    }

}