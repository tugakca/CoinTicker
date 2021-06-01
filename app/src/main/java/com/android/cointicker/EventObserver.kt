package com.android.cointicker

import androidx.lifecycle.Observer


class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(t: Event<T>) {
        t.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }



}