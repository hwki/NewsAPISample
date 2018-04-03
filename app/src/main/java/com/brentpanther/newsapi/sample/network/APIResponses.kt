package com.brentpanther.newsapi.sample.network

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

data class ArticleResponse(val totalResults: Int, val articles: List<Article>)

data class SourceResponse(val status: String, val sources: List<Source>)

@Entity(indices=[
    Index("url", unique = true),
    Index("publishedAt")])
open class Article(@PrimaryKey(autoGenerate = true) var id: Long?,
                   var title: String, var description: String,
                   var url: String, var urlToImage: String?, var publishedAt: Date,
                   var publisher: String?) {

    @Ignore
    var source: ArticleSource? = null

}

data class ArticleSource(val name: String)


@Entity
data class Source(@PrimaryKey val id: String, val name: String, val description: String?,
                  val url: String?, val category: String?, val language: String?,
                  val country: String?)

@Entity
data class Section(@PrimaryKey(autoGenerate = true) var id: Long? = null, var name: String, var lastUpdated: Long)


@Entity(primaryKeys = ["articleId", "sectionId"])
data class ArticleSection(var articleId: Long, var sectionId: Long)