package com.upchiapas.bytecraft

import android.app.Application
import com.upchiapas.bytecraft.di.AppContainer
import com.upchiapas.bytecraft.di.DefaultAppContainer

class ByteCraftApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
