package com.example.myapplication.util

interface Mapper <Src, Dst> {
    fun map(data: Src):Dst
}