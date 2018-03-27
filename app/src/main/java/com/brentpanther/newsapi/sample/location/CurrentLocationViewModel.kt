package com.brentpanther.newsapi.sample.location

import android.arch.lifecycle.ViewModel

class CurrentLocationViewModel : ViewModel() {

    var location: CurrentLocationViewData? = null

    fun initialize() {
        location = location ?: CurrentLocationViewData()
    }

}