package com.android.cointicker.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CurrentPrice(

    @SerializedName("usd")
    val usd: Double,

): Serializable