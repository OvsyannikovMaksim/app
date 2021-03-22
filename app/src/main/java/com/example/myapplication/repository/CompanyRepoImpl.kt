package com.example.myapplication.repository

import com.example.myapplication.common.CompanyInfoSrc
import com.example.myapplication.api.IEXCloudApi
import io.reactivex.Flowable

class CompanyRepoImpl(private val api: IEXCloudApi) : CompanyRepo {

    private val API_KEY = "pk_e429838e00054002ace614e8e4d032d1"

    override fun getCompanyInfo(ticker: String): Flowable<CompanyInfoSrc> {
        return api.getCompanyInfo(ticker, API_KEY)
    }

    override fun getPopularCompany(): Flowable<List<CompanyInfoSrc>> {
        return api.getPopularCompany(API_KEY)
    }
}