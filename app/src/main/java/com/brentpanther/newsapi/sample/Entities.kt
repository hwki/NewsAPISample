package com.brentpanther.newsapi.sample

import androidx.room.*
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ArticleResponse(
    val totalResults: Int,
    val articles: List<Article>
    )

@JsonClass(generateAdapter = true)
@Entity(tableName = "articles",
    indices = [Index("url"), Index("publishedAt")])
open class Article(
    @PrimaryKey val url: String,
    val title: String,
    val description: String?,
    val publishedAt: Date,
    val urlToImage: String?
    ) {

    @Ignore
    var source: ArticleSource? = null
        set(value) { articleSource = value?.name}
    
    var articleSource : String? = null

}

@JsonClass(generateAdapter = true)
data class ArticleSource(val name: String)

@Entity(
    tableName = "article_sections",
    primaryKeys = ["articleUrl", "section"],
    foreignKeys = [ForeignKey(
        entity = Article::class,
        parentColumns = ["url"],
        childColumns = ["articleUrl"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ArticleSection(
    var articleUrl: String,
    var section: Section
)

@Entity(
    tableName = "articleSectionRemoteKeys",
    primaryKeys = ["articleUrl", "section"],
    foreignKeys = [ForeignKey(
        entity = ArticleSection::class,
        parentColumns = ["articleUrl", "section"],
        childColumns = ["articleUrl", "section"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ArticleSectionRemoteKeys(
    var articleUrl: String,
    var section: Section,
    var prevKey: Int?,
    var nextKey: Int?
)
