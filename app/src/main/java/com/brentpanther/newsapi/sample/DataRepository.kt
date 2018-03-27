package com.brentpanther.newsapi.sample

import android.arch.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder


object DataRepository {

    fun top(country: String? = null, category: String? = null, sources: String? = null,
            keyword: String? = null, pageSize: Int = 25, page: Int = 1) : MutableLiveData<List<Article>> {
        return ServiceConfiguration().service.top(country?.toLowerCase(), category, sources, keyword, pageSize, page).executeLiveData {
            liveData, response -> liveData.value = response?.articles
        }
    }

    fun local(city: String, state: String, pageSize: Int = 25, page: Int = 1) : MutableLiveData<List<Article>> {
        val query = URLEncoder.encode("$city OR $state", "utf-8")
        return ServiceConfiguration().service.everything(query, pageSize=pageSize, page=page).executeLiveData {
            liveData, response -> liveData.value = response?.articles
        }
    }

    // wrap retrofit call into function that returns MutableLiveData instance
    private fun <T, L> Call<T>.executeLiveData(liveData: (MutableLiveData<L>, T?) -> Unit): MutableLiveData<L> {
        val data = MutableLiveData<L>()
        this.enqueue(object: Callback<T> {
            override fun onFailure(call: Call<T>?, t: Throwable?) {}

            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                liveData.invoke(data, response?.body())
            }
        })
        return data
    }

}
