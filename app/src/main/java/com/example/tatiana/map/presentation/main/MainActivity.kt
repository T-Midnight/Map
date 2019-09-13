package com.example.tatiana.map.presentation.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.tatiana.map.App
import com.example.tatiana.map.R
import com.example.tatiana.map.presentation.map.MapFragment
import ru.agima.mobile.cicerone.CustomNavigator

class MainActivity : AppCompatActivity() {

    private val MAP_SCREEN = "mapScreen"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState?.getStringArrayList("screens") == null) {
            App.getRouter().navigateTo("mapScreen")
        } else {
            navigator.screenNames = savedInstanceState.getStringArrayList("screens")
        }
    }

    private var navigator = object : CustomNavigator(supportFragmentManager, R.id.container, null) {
        override fun getEnterAnimation(p0: String?, p1: String?): Int {
            return 0
        }

        override fun getPopExitAnimation(p0: String?, p1: String?): Int {
            return 0
        }

        override fun getPopEnterAnimation(p0: String?, p1: String?): Int {
            return 0
        }

        override fun getExitAnimation(p0: String?, p1: String?): Int {
            return 0
        }

        override fun showSystemMessage(p0: String?, p1: Int) {
        }

        override fun createFragment(screenKey: String?, data: Any?): Fragment {
            return when (screenKey) {
                MAP_SCREEN -> MapFragment()
                else -> MapFragment()
            }
        }

        override fun exit() {
        }
    }

    override fun onResume() {
        super.onResume()
        App.getNavigationHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.getNavigationHolder().removeNavigator()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putStringArrayList("screens", navigator.screenNames as ArrayList<String>)
    }
}
