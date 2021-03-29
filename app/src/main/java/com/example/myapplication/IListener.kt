package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.common.CompanyInfoDst

interface IListener {

    fun pressButtonFavorite(bool:Boolean, ticker: String)
    fun getSearch():MutableLiveData<List<CompanyInfoDst>>
    fun find(name: String)
}