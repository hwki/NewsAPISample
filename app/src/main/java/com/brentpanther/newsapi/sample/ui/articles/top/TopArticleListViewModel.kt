package com.brentpanther.newsapi.sample.ui.articles.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.brentpanther.newsapi.sample.Section
import com.brentpanther.newsapi.sample.db.FeedParams
import com.brentpanther.newsapi.sample.di.DataRepository
import com.brentpanther.newsapi.sample.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TopArticleListViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    private var params: FeedParams? = null
    private var articles : Flow<PagingData<Article>>? = null

    private fun getTopArticles(section: Section, params: FeedParams) : Flow<PagingData<Article>> {
        val topArticles = articles
        if (topArticles != null && params == this.params) {
            return topArticles
        }
        val result = dataRepository.getTopArticles(section, params).cachedIn(viewModelScope)
        articles = result
        this.params = params
        return result
    }

    fun getArticles(view: Section): Flow<PagingData<Article>> {
        val default = FeedParams(null, null, "en", null, null)
        return when(view) {
            Section.TOP -> getTopArticles(Section.TOP, default.copy(sources = listOf("associated-press","google-news")))
            Section.SPORTS -> getTopArticles(Section.SPORTS, default.copy(category = "sports"))
            Section.TECH -> getTopArticles(Section.TECH, default.copy(category = "technology"))
            else -> throw IllegalArgumentException()
        }
    }


}
