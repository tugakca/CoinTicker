package com.android.stockmarket.di

import com.android.cointicker.repo.CoinRepo
import com.android.cointicker.usecase.CoinUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideUseCase(
        marketRepo: CoinRepo,
    ): CoinUseCase { // this is just fake repository
        return CoinUseCase(marketRepo)
    }
}