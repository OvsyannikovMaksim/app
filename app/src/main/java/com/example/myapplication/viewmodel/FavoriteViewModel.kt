package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.repository.CompanyRepo
import com.example.myapplication.repository.LocalRepo
import com.example.myapplication.util.CompanyMapper
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavoriteViewModel(private var companyRepo: CompanyRepo, private var localRepo: LocalRepo) : ViewModel() {

    private lateinit var disposableFavorite: Disposable
    var companyList: MutableLiveData<List<CompanyInfoDst>> = MutableLiveData()
    private var mapper : CompanyMapper = CompanyMapper()



    init{
        loadData()
    }


    private fun loadData(){

        disposableFavorite=localRepo.getAllFavoriteCompany()
                .subscribeOn(Schedulers.io())
                .flatMapSingle {favComps->Flowable.fromIterable(favComps)
                        .flatMap {favComp->companyRepo.getCompanyInfo(favComp.ticker)}
                        .map{z->mapper.map(z)}
                        .doOnNext { v->v.isFavorite=true }
                        .toList()
                }
                .subscribe({v->companyList.postValue(v)},
                        {error-> Log.d("FavoriteViewModel","Error in downloading: "+error.message)})


    }

    fun clear(){
        disposableFavorite.dispose()
    }
}


