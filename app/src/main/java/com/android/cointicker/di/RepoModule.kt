package com.android.stockmarket.di


import com.android.cointicker.network.CoinApi
import com.android.cointicker.repo.CoinRepo

import com.google.firebase.firestore.FirebaseFirestore
import com.android.cointicker.room.CoinDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {


    @Provides
    @Singleton
    fun providePortfolioRepo(
        ioDispatcher: CoroutineDispatcher,
        coinDao: CoinDao,
        api: CoinApi,
        fireStore:FirebaseFirestore,
    ): CoinRepo {
        return CoinRepo(ioDispatcher, coinDao, api,fireStore)
    }


    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }





}







