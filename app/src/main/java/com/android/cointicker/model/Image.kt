package com.android.cointicker.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Image(

    @SerializedName("small")
    val small: String,

): Serializable