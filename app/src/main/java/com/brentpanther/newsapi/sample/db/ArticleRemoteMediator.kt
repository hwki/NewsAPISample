package com.brentpanther.newsapi.sample.db

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.brentpanther.newsapi.sample.Section
import com.brentpanther.newsapi.sample.Article
import com.brentpanther.newsapi.sample.ArticleResponse
import com.brentpanther.newsapi.sample.ArticleSectionRemoteKeys

@OptIn(ExperimentalPagingApi::class)
abstract class ArticleRemoteMediator(
    private val section: Section,
    private val database: ArticleDatabase
) :
    RemoteMediator<Int, Article>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Article>): MediatorResult {
        try {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(section, state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(section, state)
                remoteKeys?.nextKey ?: return MediatorResult.Success(remoteKeys != null)
            }
        }
        val response = loadArticles(page, state.config.pageSize)
        val end = response.totalResults <= state.config.pageSize * page
        database.withTransaction {
            if (loadType == LoadType.REFRESH) {
                database.articleSectionRemoteKeysDao().clearSection(section)
                database.articleDao().clearSection(section)
            }
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (end) null else page + 1
            val keys = response.articles.map {
                ArticleSectionRemoteKeys(it.url, section, prevKey, nextKey)
            }
            database.articleDao().insertAll(response.articles, section)
            database.articleSectionRemoteKeysDao().insertAll(keys)
        }
        return MediatorResult.Success(end)

        } catch(e: Exception) {
            return MediatorResult.Error(e)
        }
    }



    abstract suspend fun loadArticles(page: Int, pageSize: Int): ArticleResponse

    private suspend fun getRemoteKeyForLastItem(section: Section, state: PagingState<Int, Article>): ArticleSectionRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { database.articleSectionRemoteKeysDao().remoteKeysByArticleSection(it.url, section)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(section: Section, state: PagingState<Int, Article>): ArticleSectionRemoteKeys? {
        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.let {
                database.articleSectionRemoteKeysDao().remoteKeysByArticleSection(it.url, section)
            }
        }
    }
}
