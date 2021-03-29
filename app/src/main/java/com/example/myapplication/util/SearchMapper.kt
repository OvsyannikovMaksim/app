package com.example.myapplication.util

import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.db.SearchHistory

class SearchMapper : Mapper<SearchHistory, CompanyInfoDst> {


    override fun map(data: SearchHistory) =
            CompanyInfoDst(data.search, data.search, "",0.0, 0.0,
                    0.0)
}