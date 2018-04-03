package com.brentpanther.newsapi.sample.network

import com.brentpanther.newsapi.sample.MyApplication
import com.brentpanther.newsapi.sample.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


interface NewsAPIService {

    @GET("/v2/top-headlines")
    fun top(@Query("country") country: String?, @Query("category") category: String?,
            @Query("sources") sources: String?, @Query("q") keyword: String?,
            @Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<ArticleResponse>

    @GET("/v2/everything")
    fun everything(@Query("q") query: String, @Query("sources") sources: String? = null,
                   @Query("from") from: Date? = null, @Query("language") language: String = "en",
                   @Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<ArticleResponse>

    @GET("/v2/sources")
    fun sources(@Query("country") country: String = "us") : Call<SourceResponse>
}

class ServiceConfiguration {

    val service: NewsAPIService by lazy {
        val context = MyApplication.getInstance()
        val okHttpClient = OkHttpClient.Builder().addInterceptor {
            it.proceed(it.request().newBuilder()
                    .addHeader("X-Api-Key", context.getString(R.string.api_key))
                    .build())
        }.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
                .build()
        val moshi = Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build()
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.webservice_url))
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        retrofit.create(NewsAPIService::class.java)
    }

}
