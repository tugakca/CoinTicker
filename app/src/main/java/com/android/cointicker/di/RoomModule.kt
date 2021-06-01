package com.android.stockmarket.di


import android.content.Context
import androidx.room.Room
import com.android.cointicker.room.CoinDao
import com.android.cointicker.room.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)

object RoomModule {
    @Provides
    @Singleton
    fun provideCoinDb(@ApplicationContext context: Context): CoinDatabase {
        return Room.databaseBuilder(
            context, CoinDatabase::class.java,
            CoinDatabase.DATABASE_NAME
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideCoinDAO(coinDatabase: CoinDatabase): CoinDao {
        return coinDatabase.coinDao()
    }
}