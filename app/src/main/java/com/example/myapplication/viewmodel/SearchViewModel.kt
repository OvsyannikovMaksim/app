package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.repository.CompanyRepo
import com.example.myapplication.repository.LocalRepo
import com.example.myapplication.util.CompanyMapper
import com.example.myapplication.util.SearchMapper
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private var companyRepo: CompanyRepo,
                      private var localRepo: LocalRepo):ViewModel() {

    private lateinit var disposableGainers: Disposable
    private lateinit var disposableLastSearch: Disposable
    var gainersList: MutableLiveData<List<CompanyInfoDst>> = MutableLiveData()
    var searchHistoryList: MutableLiveData<List<CompanyInfoDst>> = MutableLiveData()
    private var compMapper =CompanyMapper()
    private var searchMapper =SearchMapper()

    init{
        loadData()
    }


    private fun loadData() {
        disposableGainers=companyRepo.getGainersCompany()
                .subscribeOn(Schedulers.io())
                .flatMapIterable {s->s}
                .map{s->compMapper.map(s)}
                .toList()
                .subscribe({ v -> gainersList.postValue(v)},
                        { error -> Log.d("SearchViewModel", "Error in downloading gainers: " + error.message) })

        disposableLastSearch=localRepo.getSearchCompany()
                .subscribeOn(Schedulers.io())
                .flatMapSingle {
                    Flowable.fromIterable(it)
                            .map{s->searchMapper.map(s)}
                            .toList()
                }
                .subscribe({ v -> searchHistoryList.postValue(v)},
                        { error -> Log.d("SearchViewModel", "Error in downloading last search: " + error.message) })
    }

    fun clear(){
        disposableGainers.dispose()
    }
}