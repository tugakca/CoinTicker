package com.android.cointicker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "coin_info")
data class CoinPrice(
    @ColumnInfo(name = "coin_name")
    val name: String,
    @ColumnInfo(name = "usd")
    @SerializedName("usd")
    val price: Double

) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int? = null
}