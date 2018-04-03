package com.brentpanther.newsapi.sample.location

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*

class CurrentLocationViewData : LiveData<CurrentLocation>() {

    private lateinit var callback: LocationCallback
    private var locationClient: FusedLocationProviderClient? = null

    @SuppressLint("MissingPermission")
    fun start(context: Context) {
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        callback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult?) {
                location?.let {
                    geocode(it, this, context.applicationContext)
                }
            }
        }
        locationClient?.requestLocationUpdates(LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER), callback, null)
    }

    private fun geocode(location: LocationResult, callback: LocationCallback, context: Context) {
        try {
            val addresses = Geocoder(context, Locale.getDefault()).getFromLocation(location.lastLocation.latitude,
                    location.lastLocation.longitude, 1)
            if (addresses.isNotEmpty()) {
                locationClient?.removeLocationUpdates(callback)
                value = CurrentLocation(addresses[0])
            }
        } catch (ignored: IOException) {}
    }

    override fun onInactive() {
        super.onInactive()
        locationClient?.removeLocationUpdates(callback)
    }
}

data class CurrentLocation(val country: String, val countryCode: String, val state: String, val city: String) {

    constructor(address: Address) : this(address.countryName, address.countryCode, address.adminArea, address.locality)


}
