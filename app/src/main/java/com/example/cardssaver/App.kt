package com.example.cardssaver

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        private var instance: App? = null

        fun appContext() = instance!!.applicationContext
    }
    init {
        instance = this
    }
}