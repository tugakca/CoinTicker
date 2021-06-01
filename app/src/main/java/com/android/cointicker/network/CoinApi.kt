package com.android.cointicker.network

import com.android.cointicker.model.Coin
import com.android.cointicker.model.CoinDetail
import com.android.cointicker.model.CoinPrice
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {

    @GET("coins/list")
    suspend fun getAllCoinList(): List<Coin>


    @GET("coins/{id}")
    suspend fun getCoinDetail(@Path("id") id: String): CoinDetail

    @GET("simple/price")
    suspend fun getPriceList(
        @Query("ids") id: String,
        @Query("vs_currencies") currency: String,
        @Query("include_24hr_change") change: String
    ): Map<String, CoinPrice>
}