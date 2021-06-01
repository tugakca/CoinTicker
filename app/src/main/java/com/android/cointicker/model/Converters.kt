package com.android.cointicker.model

import androidx.room.TypeConverter
import com.android.cointicker.model.CurrentPrice
import com.android.cointicker.model.Description
import com.android.cointicker.model.Image
import com.android.cointicker.model.MarketData
import com.google.gson.Gson

class Converters {


    @TypeConverter
    fun fromImage(image: Image):String{
        return image.small
    }

    @TypeConverter
    fun toImage(imageI:String):Image{
        return Image(imageI)
    }


    @TypeConverter
    fun fromCurrentPrice(value:CurrentPrice):Double{
        return value.usd
    }

    @TypeConverter
    fun toCurrentPrice(value:Double):CurrentPrice{
        return CurrentPrice(value)
    }

    @TypeConverter
    fun fromDescription(value:Description):String{
        return value.en
    }
    @TypeConverter
    fun toDescription(value:String):Description{
        return Description(value)
    }



    @TypeConverter
    fun fromMarketData(value:MarketData):String{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMarketData(value:String): MarketData {
        return Gson().fromJson(value,MarketData::class.java)
    }










}