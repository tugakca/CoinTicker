package com.android.cointicker.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CoinDetail(

        @SerializedName("image")
        var image: Image,
        @SerializedName("market_data")
        var marketData: MarketData,
        @SerializedName("name")
        var name: String,
        @SerializedName("symbol")
        var symbol: String,
        @SerializedName("description")
        var description: Description,
        @SerializedName("hashing_algorithm")
        var hashingAlgorithm: String,
) : Serializable {

    var isFavorite: Boolean? = false

}