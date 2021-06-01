package com.android.cointicker

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: MultiDexApplication() {

    companion object{

        var userId :String?=null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}