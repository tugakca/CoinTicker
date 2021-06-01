package com.android.cointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cointicker.Event
import com.android.cointicker.model.CoinPrice
import com.android.cointicker.usecase.CoinUseCase
import com.android.cointicker.utils.FirebaseListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(var coinUseCase: CoinUseCase) : ViewModel() {

    val getFavoriteLiveData: MutableLiveData<ArrayList<CoinPrice>> by lazy { MutableLiveData<ArrayList<CoinPrice>>() }

    val loading: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    val error: MutableLiveData<Event<Exception>> by lazy { MutableLiveData<Event<Exception>>() }


    fun getFavorites() {
        loading.value = Event(true)
        viewModelScope.launch {
            try {
                coinUseCase.getFavorite(object : FirebaseListener {
                    override fun onDataFetched(
                        value: Boolean,
                        coinPriceList: ArrayList<CoinPrice>?
                    ) {
                        getFavoriteLiveData.value = coinPriceList!!
                    }
                })

            } catch (e: Exception) {
                error.value = Event(e)
            }
        }
    }

}