package com.android.cointicker.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Description(
    @SerializedName("en")
    val en: String,
): Serializable