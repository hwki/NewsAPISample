package com.brentpanther.newsapi.sample.di

import com.brentpanther.newsapi.sample.db.DataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsDataRepository(dataRepositoryImpl: DataRepositoryImpl) : DataRepository

}
