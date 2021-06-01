package com.android.cointicker.utils

import com.android.cointicker.model.CoinPrice

interface FirebaseListener {

    fun onDataFetched(value:Boolean,coinPriceList:ArrayList<CoinPrice>?)
}