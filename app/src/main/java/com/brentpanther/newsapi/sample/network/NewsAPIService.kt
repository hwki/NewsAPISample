package com.brentpanther.newsapi.sample.network

import com.brentpanther.newsapi.sample.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


interface NewsAPIService {

    @GET("/v2/top-headlines")
    suspend fun top(
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("language") language: String = "en",
        @Query("sources") sources: String? = null,
        @Query("q") keyword: String? = null,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): ArticleResponse

    @GET("/v2/everything")
    suspend fun everything(
        @Query("q") query: String,
        @Query("sources") sources: String? = null,
        @Query("from") from: Date? = null,
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): ArticleResponse

}
