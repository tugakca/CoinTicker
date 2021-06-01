package com.android.cointicker.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MarketData(

        @SerializedName("current_price")
        val currentPrice: CurrentPrice,
        @SerializedName("price_change_percentage_24h")
        var priceChange24h:Double,

        ):Serializable