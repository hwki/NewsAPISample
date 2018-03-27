package com.brentpanther.newsapi.sample

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


data class ArticleResponse(val status: String, val totalResults: Int, val articles: List<Article>)

data class SourceResponse(val status: String, val sources: List<Source>)

@Parcelize
data class Article(val author: String, val title: String, val description: String,
                   val url: String, val urlToImage: String, val publishedAt: Date,
                   val source: Source) : Comparable<Article>, Parcelable {

    override fun compareTo(other: Article) = publishedAt.compareTo(other.publishedAt)
}

@Parcelize
data class Source(val id: String, val name: String, val description: String,
                  val url: String, val category: String, val language: String,
                  val country: String) : Parcelable

