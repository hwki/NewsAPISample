package com.brentpanther.newsapi.sample.article

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.brentpanther.newsapi.sample.db.DataRepository
import com.brentpanther.newsapi.sample.network.ArticleResponse

class ArticleListViewModel : ViewModel() {

    var articles: LiveData<ArticleResponse>? = null

    fun initializeTop(section: String, country: String? = null, category: String? = null, sources: String? = null,
                      keyword: String? = null)  {
        articles = articles ?: DataRepository.top(section, country, category, sources, keyword)
    }

    fun initializeLocal(section: String, city: String, state: String) {
        articles = articles ?: DataRepository.local(section, city, state)
    }

    fun clear(section: String, lifecycleOwner: LifecycleOwner) {
        DataRepository.clearSection(section)
        articles?.removeObservers(lifecycleOwner)
        articles = null
    }
}
