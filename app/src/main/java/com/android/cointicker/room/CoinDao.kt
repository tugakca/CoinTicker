package com.android.cointicker.room

import androidx.room.*
import com.android.cointicker.model.Coin

import com.android.cointicker.model.CoinPrice

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg  coin: CoinPrice): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdList(vararg  coin: Coin): List<Long>

    @Update
    public fun updateCoinItem(stock: CoinPrice)

    @Query("SELECT * FROM coin_info ")
    suspend fun getCoinListWithPrice(): List<CoinPrice>


    @Query("SELECT * FROM coin_id_list ")
    suspend fun getCoinIdList(): List<Coin>

}