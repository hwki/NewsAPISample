package com.brentpanther.newsapi.sample.article

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.brentpanther.newsapi.sample.Article
import com.brentpanther.newsapi.sample.DataRepository

class ArticleListViewModel : ViewModel() {

    var articles: MutableLiveData<List<Article>>? = null

    fun initialize(country: String? = null, category: String? = null, sources: String? = null,
                   keyword: String? = null) {
        articles = articles ?: DataRepository.top(country, category, sources, keyword)
    }

    fun initializeLocal(city: String, state: String) {
        articles = articles ?: DataRepository.local(city, state)
    }

    fun clear(lifecycleOwner: LifecycleOwner) {
        articles?.removeObservers(lifecycleOwner)
        articles = null
    }
}
