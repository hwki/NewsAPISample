package com.brentpanther.newsapi.sample.db

import com.brentpanther.newsapi.sample.Section
import com.brentpanther.newsapi.sample.ArticleResponse
import com.brentpanther.newsapi.sample.network.NewsAPIService

class TopArticleRemoteMediator(
    section: Section,
    private val feedParams: FeedParams,
    private val service: NewsAPIService,
    database: ArticleDatabase
) : ArticleRemoteMediator(section, database) {

    override suspend fun loadArticles(page: Int, pageSize: Int): ArticleResponse {
        return service.top(
            sources = feedParams.sources?.joinToString(),
            category = feedParams.category,
            language = feedParams.language ?: "en",
            country = feedParams.countryCode,
            page = page, pageSize = pageSize
        )
    }
}

