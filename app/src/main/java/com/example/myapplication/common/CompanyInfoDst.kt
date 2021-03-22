package com.example.myapplication.common

data class CompanyInfoDst(val name: String, val ticker: String, val logo: String,
                          val curPrice: Double, val priceChange: Double,
                          val priceChangePercent: Double, var isFavorite:Boolean = false)