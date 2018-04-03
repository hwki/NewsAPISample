package com.brentpanther.newsapi.sample.location

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

/**
 * Helper functions for activities requesting access to location.
 */
interface LocationActivityHelper {

    companion object {
        private const val PERMISSION_REQUEST_LOCATION: Int = 1001
    }

    fun requestLocation(activity: AppCompatActivity, viewModel: CurrentLocationViewModel, l: (CurrentLocation) -> Unit) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_LOCATION)
        } else {
            viewModel.location?.start(activity)
            val currentLocationViewModel = ViewModelProviders.of(activity).get(CurrentLocationViewModel::class.java)
            currentLocationViewModel.location?.observe(activity, Observer {
                if (it == null) return@Observer
                l.invoke(it)
            })
        }
    }

    fun locationPermissionGranted(activity: AppCompatActivity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) : Boolean {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        return false
    }
}