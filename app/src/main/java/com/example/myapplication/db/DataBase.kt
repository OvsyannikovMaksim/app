package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteCompany::class, SearchHistory::class], version=1)
abstract class DataBase: RoomDatabase() {

    abstract fun localDao(): LocalDao

    companion object {
        private var INSTANCE: DataBase? = null

        fun getDataBase(context: Context): DataBase? {
            if (INSTANCE == null){
                synchronized(DataBase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DataBase::class.java, "myDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}