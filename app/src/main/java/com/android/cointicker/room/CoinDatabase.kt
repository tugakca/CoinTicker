package com.android.cointicker.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.cointicker.model.Coin
import com.android.cointicker.model.CoinPrice

@Database(entities = [CoinPrice::class, Coin::class], version = 1)
abstract class CoinDatabase: RoomDatabase() {
    abstract fun coinDao(): CoinDao

    companion object{
        val DATABASE_NAME: String = "coin_db"
    }
}