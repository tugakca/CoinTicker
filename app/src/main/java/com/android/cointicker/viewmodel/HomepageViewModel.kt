package com.android.cointicker.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cointicker.model.Coin
import com.android.cointicker.Event
import com.android.cointicker.model.CoinPrice
import com.android.cointicker.usecase.CoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(var coinUseCase: CoinUseCase) : ViewModel() {


    val idListLiveData: MutableLiveData<Event<List<Coin>>> by lazy { MutableLiveData<Event<List<Coin>>>() }

    val priceListLiveData: MutableLiveData<Event<List<CoinPrice>>> by lazy { MutableLiveData<Event<List<CoinPrice>>>() }
    val loading: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    val error: MutableLiveData<Event<Exception>> by lazy { MutableLiveData<Event<Exception>>() }


    init {

        loading.value=Event(true)
    }

    fun getAllCoinList() {
        viewModelScope.launch {
            try {
                val data = coinUseCase.getAllCoinList()
                idListLiveData.value = data
            } catch (e: Exception) {
                error.value=Event(e)

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getCoinListWithPrice(list: List<Coin>, begin: Int) {
        viewModelScope.launch {
            try {
                val data = coinUseCase.getCoinListWithPrice(list, begin)
                priceListLiveData.value = data
            } catch (e: Exception) {
                error.value = Event(e)
            }
        }
    }
}