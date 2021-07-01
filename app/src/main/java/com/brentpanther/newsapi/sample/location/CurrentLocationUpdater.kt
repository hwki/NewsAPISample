package com.brentpanther.newsapi.sample.location

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.IOException
import javax.inject.Inject

class CurrentLocationUpdater @Inject constructor(private val client: FusedLocationProviderClient,
                                                 private val geocoder: Geocoder) {

    @SuppressLint("MissingPermission")
    @ExperimentalCoroutinesApi
    fun fetchUpdates(): Flow<CurrentLocation> = callbackFlow {
        client.lastLocation.addOnSuccessListener { loc ->
            loc?.let {
                geocode(it)?.let { curLoc ->
                    trySend(curLoc)
                }
            }
        }
        awaitClose { }
    }

    private fun geocode(location: Location): CurrentLocation? {
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.isNotEmpty()) {
                return CurrentLocation(addresses[0])
            }
        } catch (ignored: IOException) {
        }
        return null
    }
}

data class CurrentLocation(val country: String, val countryCode: String, val state: String, val city: String) {

    constructor(address: Address) : this(address.countryName, address.countryCode, address.adminArea, address.locality)


}
