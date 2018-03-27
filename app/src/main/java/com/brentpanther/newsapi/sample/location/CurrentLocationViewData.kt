package com.brentpanther.newsapi.sample.location

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.*
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
                    val addresses = Geocoder(context.applicationContext, Locale.getDefault()).getFromLocation(location.lastLocation.latitude,
                            location.lastLocation.longitude, 1)
                    if (addresses.isNotEmpty()) {
                        locationClient?.removeLocationUpdates(this)
                        value = CurrentLocation(addresses[0])
                    }
                }
            }
        }
        locationClient?.requestLocationUpdates(LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER), callback, null)
    }

    override fun onInactive() {
        super.onInactive()
        locationClient?.removeLocationUpdates(callback)
    }
}

data class CurrentLocation(val country: String, val countryCode: String, val state: String, val city: String) {

    constructor(address: Address) : this(address.countryName, address.countryCode, address.adminArea, address.locality)


}
