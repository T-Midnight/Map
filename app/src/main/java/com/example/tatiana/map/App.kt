package com.example.tatiana.map

import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import ru.terrakok.cicerone.Cicerone

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    companion object {
        lateinit var context: Context
        private val cicerone = Cicerone.create()

        fun getNavigationHolder() = cicerone.navigatorHolder
        fun getRouter() = cicerone.router
    }
}