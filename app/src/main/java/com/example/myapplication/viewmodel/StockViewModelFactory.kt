package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.CompanyRepo
import com.example.myapplication.repository.LocalRepo

class StockViewModelFactory(private val companyRepo: CompanyRepo, private var localRepo: LocalRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            return StockViewModel(companyRepo,localRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}