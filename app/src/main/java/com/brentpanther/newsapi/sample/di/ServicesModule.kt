package com.brentpanther.newsapi.sample.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ServicesModule {

    @Provides
    fun providesGeocoder(@ApplicationContext context: Context) = Geocoder(context)

    @Provides
    fun providesLocationProvider(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

}
