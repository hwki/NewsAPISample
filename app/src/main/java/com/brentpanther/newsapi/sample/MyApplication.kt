package com.brentpanther.newsapi.sample

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Companion.INSTANCE = this
    }

    companion object {
        private lateinit var INSTANCE: MyApplication
        fun getInstance() = INSTANCE
    }
}