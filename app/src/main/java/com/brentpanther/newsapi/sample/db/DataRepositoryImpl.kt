package com.brentpanther.newsapi.sample.db

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.brentpanther.newsapi.sample.Section
import com.brentpanther.newsapi.sample.di.DataRepository
import com.brentpanther.newsapi.sample.Article
import com.brentpanther.newsapi.sample.network.NewsAPIService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(private val api: NewsAPIService, private val db: ArticleDatabase):
    DataRepository {

    private val config = PagingConfig(
        pageSize = 10,
        enablePlaceholders = true
    )

    @ExperimentalPagingApi
    override fun getTopArticles(section: Section, params: FeedParams) : Flow<PagingData<Article>> {
        return Pager(config = config,
            remoteMediator = TopArticleRemoteMediator(section, params, api, db),
            pagingSourceFactory = {
                db.articleDao().observeArticlesForSection(section)
            }).flow
    }

    @ExperimentalPagingApi
    override fun getArticles(section: Section, params: FeedParams) : Flow<PagingData<Article>> {
        return Pager(config = config,
            remoteMediator = TopicArticleRemoteMediator(section, params, api, db),
            pagingSourceFactory = {
                db.articleDao().observeArticlesForSection(section)
            }).flow
    }

}
