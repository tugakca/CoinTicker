
package com.android.cointicker.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.android.cointicker.model.CoinDetail
import com.android.cointicker.Event
import com.android.cointicker.model.Coin
import com.android.cointicker.model.CoinPrice
import com.android.cointicker.repo.CoinRepo
import com.android.cointicker.utils.FirebaseListener
import com.android.cointicker.utils.DataState
import java.lang.Exception
import javax.inject.Inject



class CoinUseCase  @Inject constructor(var  coinRepo:CoinRepo) {


    suspend fun  getAllCoinList(): Event<List<Coin>> {
        var coinList = emptyList<Coin>()
        try {
            coinList = coinRepo.getAllCoin()

        } catch (e: Exception) {
            throw Exception(e)
        }

        return Event(coinList)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun  getCoinListWithPrice(list:List<Coin>, begin:Int):Event<List<CoinPrice>>{
        var coinList = emptyList<CoinPrice>()
        try {
            coinList = coinRepo.getCoinListWithPrice(list,begin)

        } catch (e: Exception) {
          throw Exception(e)
        }
        return Event(coinList)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun searchItem(query:String):List<CoinPrice>{
        var searchQueryList = emptyList<CoinPrice>()
        try{
            searchQueryList =coinRepo.searchQuery(query)

        }catch (e:Exception){
            return throw Exception(e)
        }
        return searchQueryList

    }

    suspend fun getIdListFromDb(): DataState<List<Coin>> {
        var coinList = emptyList<Coin>()
        try{
            coinList =coinRepo.getCoinIdListFromDb()

        }catch (e:Exception){
            return DataState.Error(e)
        }

        return DataState.Success(coinList)
    }

    suspend fun getCoinDetail(coinName:String):Event<CoinDetail>{
        var coinDetail: CoinDetail?=null
        try{
              coinDetail =  coinRepo.getCoinDetail(coinName)
            }
            catch (e:Exception){
              throw Exception(e)
        }
        return Event(coinDetail)
    }

    suspend fun  isFavorite(coinName: String, listener: FirebaseListener) {
        try {
           coinRepo.isFavorite(coinName,listener)

        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    suspend fun addToFavorite (coinPrice:CoinPrice,listener: FirebaseListener){
        try {
            coinRepo.addToFavorite(coinPrice,listener)

        } catch (e: Exception) {
            throw Exception(e)
        }
    }


    suspend fun getFavorite (listener: FirebaseListener){
        try {
            coinRepo.getFavorites(listener)

        } catch (e: Exception) {
            throw Exception(e)
        }
    }


}