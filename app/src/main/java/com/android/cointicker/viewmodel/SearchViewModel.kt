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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(private val coinUseCase: CoinUseCase) : ViewModel() {


    private var searchJob: Job? = null
    val _searchState: MutableLiveData<List<CoinPrice>> by lazy { MutableLiveData<List<CoinPrice>>() }
    val _coinIdListState: MutableLiveData<Event<List<Coin>>> by lazy { MutableLiveData<Event<List<Coin>>>() }
    val loading: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    val error: MutableLiveData<Event<Exception>> by lazy { MutableLiveData<Event<Exception>>() }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onSearchQuery(query: String) {
        loading.value = Event(true)
        searchJob = viewModelScope.launch() {
            val data = coinUseCase.searchItem(query)
            _searchState.value = data
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}