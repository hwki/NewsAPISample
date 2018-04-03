package com.brentpanther.newsapi.sample.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.brentpanther.newsapi.sample.db.ApiResponse
import org.jetbrains.anko.doAsync


abstract class NetworkResource<DataType> {

    private val result = MediatorLiveData<DataType>()

    abstract fun loadFromDb(): LiveData<DataType>

    abstract fun shouldDownload(): Boolean

    abstract fun createCall(): LiveData<ApiResponse<DataType?>>

    abstract fun saveResult(data: DataType)

    abstract fun onFetchFailed()

    fun init(): NetworkResource<DataType> {
        val dbSource = this.loadFromDb()
        result.addSource(dbSource, {
            result.removeSource(dbSource)
            doAsync {
                if (shouldDownload()) {
                    fetchFromNetwork(dbSource)
                } else {
                    result.addSource(dbSource) { result.postValue(it) }
                }
            }
        })
        return this
    }

    private fun fetchFromNetwork(dbSource: LiveData<DataType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) { result.postValue(it) }
        result.addSource(apiResponse) { response ->
            if (response == null) return@addSource
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            if (response.isSuccessful) {
                doAsync {
                    saveResult(response.data!!)
                }
            } else {
                onFetchFailed()
            }
            result.addSource(dbSource) {
                result.postValue(it)
            }
        }
    }

    fun getLiveData() = result

}