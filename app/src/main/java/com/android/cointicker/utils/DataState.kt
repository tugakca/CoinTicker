package com.android.cointicker.utils

import java.lang.Exception

sealed class DataState<out R> {

    data class Success<out T>(val data: T): DataState<T>()
    data class Error(val exception: Exception): DataState<Nothing>()
    data class SearchError(val error: String): DataState<Nothing>()

    object Loading: DataState<Nothing>()
}



