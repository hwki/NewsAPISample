package com.brentpanther.newsapi.sample.di

import androidx.paging.PagingData
import com.brentpanther.newsapi.sample.Section
import com.brentpanther.newsapi.sample.db.FeedParams
import com.brentpanther.newsapi.sample.Article
import kotlinx.coroutines.flow.Flow

interface DataRepository {

    fun getTopArticles(section: Section, params: FeedParams) : Flow<PagingData<Article>>

    fun getArticles(section: Section, params: FeedParams) : Flow<PagingData<Article>>
}


