package com.example.myapplication.repository

import com.example.myapplication.common.CompanyInfoSrc
import com.example.myapplication.common.SearchInfo
import com.example.myapplication.common.SearchInfoComp
import io.reactivex.Flowable
import io.reactivex.Observable

interface CompanyRepo {

    fun getCompanyInfo(ticker:String): Flowable<CompanyInfoSrc>
    fun getPopularCompany(): Flowable<List<CompanyInfoSrc>>
    fun getGainersCompany(): Flowable<List<CompanyInfoSrc>>
    fun doSearch(fragment:String): Observable<SearchInfo>
}