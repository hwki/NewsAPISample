package com.brentpanther.newsapi.sample.ui.articles.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.brentpanther.newsapi.sample.Section
import com.brentpanther.newsapi.sample.db.FeedParams
import com.brentpanther.newsapi.sample.di.DataRepository
import com.brentpanther.newsapi.sample.location.CurrentLocationUpdater
import com.brentpanther.newsapi.sample.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationArticleListViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    @Inject lateinit var locationUpdater: CurrentLocationUpdater
    private var params: FeedParams? = null
    private var articles : Flow<PagingData<Article>>? = null

    private fun getArticles(section: Section, params: FeedParams): Flow<PagingData<Article>> {
        val articles = this.articles
        if (articles != null && params == this.params) {
            return articles
        }
        val result = dataRepository.getArticles(section, params).cachedIn(viewModelScope)
        this.articles = result
        this.params = params
        return result
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getArticles(section: Section) = channelFlow {
        viewModelScope.launch(Dispatchers.IO) {
            locationUpdater.fetchUpdates().collect {
                val query = "${it.state} OR (${it.state} AND ${it.city})"
                val params = FeedParams(null, null, "en", it.countryCode, query)
                getArticles(section, params).cancellable().collect { data ->
                    trySend(data)
                }
            }
        }
        awaitClose {  }
    }

}