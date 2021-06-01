package com.android.cointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cointicker.model.CoinDetail
import com.android.cointicker.Event
import com.android.cointicker.model.CoinPrice
import com.android.cointicker.usecase.CoinUseCase
import com.android.cointicker.utils.FirebaseListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(var coinUseCase: CoinUseCase) : ViewModel() {


    val coinDetailLiveData: MutableLiveData<Event<CoinDetail>> by lazy { MutableLiveData<Event<CoinDetail>>() }
    val loading: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    val isFavoriteLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val addToFavoriteLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val error: MutableLiveData<Event<Exception>> by lazy { MutableLiveData<Event<Exception>>() }

    init {
        loading.value = Event(true)
    }

    fun getCoinDetail(coinName: String) {
        viewModelScope.launch {
            try {
                val data = coinUseCase.getCoinDetail(coinName)
                coinDetailLiveData.value = data
            } catch (e: Exception) {
                error.value = Event(e)
            }
        }

    }

    fun addToFavorite(coinPrice: CoinPrice) {
        viewModelScope.launch {

            try {
                coinUseCase.addToFavorite(coinPrice, object : FirebaseListener {

                    override fun onDataFetched(
                        value: Boolean,
                        coinPriceList: ArrayList<CoinPrice>?
                    ) {
                        isFavoriteLiveData.postValue(value)
                    }
                })

            } catch (e: Exception) {
                error.value = Event(e)
            }
        }
    }

    fun isFavorite(coinName: String) {
        viewModelScope.launch {

            try {
                coinUseCase.isFavorite(coinName, object : FirebaseListener {

                    override fun onDataFetched(
                        value: Boolean,
                        coinPriceList: ArrayList<CoinPrice>?
                    ) {
                        isFavoriteLiveData.postValue(value)
                    }
                })

            } catch (e: Exception) {
                error.value = Event(e)
            }
        }
    }

}