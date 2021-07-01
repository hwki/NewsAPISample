package com.brentpanther.newsapi.sample.di

import android.content.Context
import com.brentpanther.newsapi.sample.R
import com.brentpanther.newsapi.sample.network.NewsAPIService
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader("X-Api-Key", context.getString(R.string.api_key))
                    .build()
            )
        }.addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }).build()
    }

    @Singleton
    @Provides
    fun provideNewsAPIService(@ApplicationContext context: Context, client: OkHttpClient) : NewsAPIService {
        val moshi = Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.webservice_url))
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(NewsAPIService::class.java)
    }


}
