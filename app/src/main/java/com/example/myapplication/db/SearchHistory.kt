package com.example.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(@PrimaryKey val search:String)