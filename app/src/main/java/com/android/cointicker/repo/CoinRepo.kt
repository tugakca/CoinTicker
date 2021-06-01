package com.android.cointicker.repo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.cointicker.model.Coin
import com.android.cointicker.App
import com.android.cointicker.model.CoinDetail
import com.android.cointicker.model.CoinPrice
import com.android.cointicker.network.CoinApi
import com.android.cointicker.utils.FirebaseListener
import com.google.firebase.firestore.FirebaseFirestore
import com.android.cointicker.room.CoinDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CoinRepo
@Inject constructor(
    var ioDispatcher: CoroutineDispatcher,
    var coinDao: CoinDao,
    var api: CoinApi,
    var fireStore: FirebaseFirestore

) {


    suspend fun getAllCoin(): List<Coin> {
        var coinIdList = emptyList<Coin>()
        withContext(ioDispatcher) {
            try {
                val response = api.getAllCoinList()
                coinIdList = response
                val tempDbList = coinDao.getCoinIdList()
                if (tempDbList.isNullOrEmpty())
                    coinDao.insertIdList(*coinIdList.toTypedArray())
                else {
                    val firstListObjectIds = tempDbList.map { it.coinId }.toSet()
                    val filteredList = coinIdList.filter { !firstListObjectIds.contains(it.coinId) }
                    filteredList.let {
                        coinDao.insertIdList(*filteredList.toTypedArray())
                    }
                }
                coinIdList = coinDao.getCoinIdList()

            } catch (e: Exception) {
                throw Exception()
            }
        }
        return coinIdList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getCoinListWithPrice(list: List<Coin>, begin: Int): List<CoinPrice> {
        var coinPriceList = arrayListOf<CoinPrice>()
        var concatString = ""
        try {
            withContext(ioDispatcher) {
                val end = begin + 20
                for (i in begin..end) {
                    concatString += if (i + 1 == end)
                        list[i].coinId
                    else
                        list[i].coinId + ","
                }
                var i = 0
                val response = api.getPriceList(concatString, "usd", "false")
                response.forEach { t, u ->
                    val coin = CoinPrice(t, u.price)
                    coinPriceList.add(coin)
                }
                val tempDbList = coinDao.getCoinListWithPrice()
                if (tempDbList.isNullOrEmpty())
                    coinDao.insert(*coinPriceList.toTypedArray())
                else {
                    val firstListObjectIds = tempDbList.map { it.name }.toSet()
                    val filteredList =
                        coinPriceList.filter { !firstListObjectIds.contains(it.name) }
                    if (filteredList.isNotEmpty()) {
                        coinDao.insert(*filteredList.toTypedArray())
                        coinPriceList.clear()
                        coinPriceList.addAll(filteredList)
                    }
                    coinPriceList.forEach { newValue ->
                        tempDbList.forEach { db ->
                            if (newValue.name == db.name) {
                                val coin = CoinPrice(db.name, newValue.price)
                                coin.uuid = db.uuid
                                coinDao.updateCoinItem(coin)
                            }
                        }
                    }
                }

            }
        } catch (e: Exception) {
            throw Exception()
        }
        return coinPriceList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun searchQuery(query: String): List<CoinPrice> {
        var coinIdList = emptyList<Coin>()
        var concatString = ""
        var searchList = arrayListOf<CoinPrice>()
        var searchIdList = arrayListOf<Coin>()
        withContext(ioDispatcher) {
            try {
                searchIdList.clear()
                searchList.clear()
                coinIdList = coinDao.getCoinIdList()
                for (coin in coinIdList) {
                    if (coin.name!!.contains(query, ignoreCase = true)) {
                        searchIdList.add(coin)
                    }
                }
                for (i in searchIdList.indices) {
                    concatString += if (i + 1 == searchIdList.size)
                        searchIdList[i].coinId
                    else
                        searchIdList[i].coinId + ","
                }
                val response = api.getPriceList(concatString, "usd", "false")
                response.forEach { t, u ->
                    val coin = CoinPrice(t, u.price)
                    searchList.add(coin)
                }

            } catch (e: Exception) {
                throw Exception(e)
            }
        }
        return searchList
    }


    suspend fun getCoinIdListFromDb(): List<Coin> {
        var coinIdList = emptyList<Coin>()
        withContext(ioDispatcher) {
            try {
                coinIdList = coinDao.getCoinIdList()

            } catch (e: Exception) {
                throw Exception()
            }
        }
        return coinIdList
    }


    suspend fun getCoinDetail(coinName: String): CoinDetail {
        var coinDetail: CoinDetail? = null
        withContext(ioDispatcher) {
            try {
                val response = api.getCoinDetail(coinName)
                coinDetail = response
            } catch (e: Exception) {
                throw  Exception(e)
            }
        }
        return coinDetail!!
    }

    suspend fun isFavorite(coinName: String,listener: FirebaseListener) {

        withContext(ioDispatcher) {
            if(!App.userId.isNullOrEmpty()){
                try{

                    fireStore.collection(App.userId!!)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                if (document.data.get("name").toString() == coinName) {
                                    listener.onDataFetched(true,null)
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            listener.onDataFetched(false,null)
                            Log.w("TAG", "Error getting documents.", exception)
                        }
                }catch (e:Exception){
                    throw Exception(e)
                }
            }else listener.onDataFetched(false,null)

        }

    }



    suspend fun getFavorites(listener: FirebaseListener) {

        val coinList = arrayListOf<CoinPrice>()

        withContext(ioDispatcher) {
            if(!App.userId.isNullOrEmpty()){
                try{

                    fireStore.collection(App.userId!!)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val coin = CoinPrice (document.data.get("name").toString(),document.data.get("currentPrice")!!.toString().toDouble())
                                   coinList.add(coin)
                            }
                            listener.onDataFetched(true,coinList)
                        }
                        .addOnFailureListener { exception ->
                            listener.onDataFetched(false,null)
                            Log.w("TAG", "Error getting documents.", exception)
                        }
                }catch (e:Exception){
                    throw Exception(e)
                }
            }else listener.onDataFetched(false,null)

        }

    }
    suspend fun addToFavorite(coinName: CoinPrice,listener: FirebaseListener) {

        withContext(ioDispatcher) {

            if(!App.userId.isNullOrEmpty()){
                try{
                    val userV = hashMapOf(
                        "name" to coinName.name, "currentPrice" to coinName.price,
                    )
                    fireStore.collection(App.userId!!)
                        .add(userV)
                        .addOnSuccessListener { documentReference ->
                            listener.onDataFetched(true,null)
                        }
                        .addOnFailureListener { e ->
                            listener.onDataFetched(false,null)

                        }
                }catch (e:Exception){
                    throw Exception(e)
                }
            }else listener.onDataFetched(false,null)

        }

    }

}