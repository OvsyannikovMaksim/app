package com.example.myapplication.repository

import com.example.myapplication.api.FinHubApi
import com.example.myapplication.api.IEXCloudApi
import com.example.myapplication.common.CompanyInfoSrc
import com.example.myapplication.common.SearchInfo
import io.reactivex.Flowable
import io.reactivex.Observable

class CompanyRepoImpl(private val iexCloudApi: IEXCloudApi, private val finHubApi: FinHubApi) : CompanyRepo {

    private val API_KEY_IEXCLOUD = "sk_0949065ff216429480ccb4bb61510c12"
    private val API_KEY_FINHUB = "c1gfa1748v6p69n8v190"

    override fun getCompanyInfo(ticker: String): Flowable<CompanyInfoSrc> {
        return iexCloudApi.getCompanyInfo(ticker, API_KEY_IEXCLOUD)
    }

    override fun getPopularCompany(): Flowable<List<CompanyInfoSrc>> {
        return iexCloudApi.getPopularCompany(API_KEY_IEXCLOUD)
    }

    override fun getGainersCompany(): Flowable<List<CompanyInfoSrc>>{
        return iexCloudApi.getGainersCompany(API_KEY_IEXCLOUD)
    }

    override fun doSearch(fragment: String): Observable<SearchInfo> {
        return finHubApi.doSearch(fragment, API_KEY_FINHUB)
    }
}

