package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.IListener
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.db.FavoriteCompany
import com.example.myapplication.repository.CompanyRepo
import com.example.myapplication.repository.LocalRepo
import com.example.myapplication.util.CompanyMapper
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.flowable.FlowableAllSingle
import io.reactivex.schedulers.Schedulers

class StockViewModel(private var companyRepo: CompanyRepo, private var localRepo: LocalRepo): ViewModel(), IListener{

    private lateinit var disposableStocks: Disposable
    var companyList: MutableLiveData<List<CompanyInfoDst>> = MutableLiveData()
    private var mapper : CompanyMapper = CompanyMapper()

    init{
        loadData()
    }


    private fun loadData() {

        disposableStocks = companyRepo.getPopularCompany()
                .flatMapIterable { s -> s }
                .map { s -> mapper.map(s) }
                .zipWith(localRepo.getAllFavoriteCompany()) { comp, favLst ->
                    //условие
                    return@zipWith comp
                }
                .toList()
                .subscribe({ v -> companyList.postValue(v) },
                        { error -> Log.d("FavoriteViewModel", "Error in downloading: " + error.message) })
    }

    fun clear(){
        disposableStocks.dispose()
    }

    override fun pressButtonFavorite(bool: Boolean, ticker: String) {
        if(bool) {
            localRepo.insertTicker(FavoriteCompany(ticker))
        }
        else{
            localRepo.deleteTicker(FavoriteCompany(ticker))
        }
    }
}
/*
.zipWith(localRepo.getAllFavoriteCompany()){comp, favLst->

                    return@zipWith comp
                }
*/