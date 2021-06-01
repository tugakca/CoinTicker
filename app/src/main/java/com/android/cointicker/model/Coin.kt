package com.android.cointicker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "coin_id_list")

data class Coin(
    @SerializedName("id")
    val coinId: String,
    @ColumnInfo(name = "symbol")
    @SerializedName("symbol")
    val symbol: String?,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String?
):Serializable{

    @PrimaryKey(autoGenerate = true)
    var uuidP :Int?=null
}