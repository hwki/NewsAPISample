package com.brentpanther.newsapi.sample.db

import androidx.paging.PagingSource
import androidx.room.*
import com.brentpanther.newsapi.sample.Section
import com.brentpanther.newsapi.sample.Article
import com.brentpanther.newsapi.sample.ArticleSection
import com.brentpanther.newsapi.sample.ArticleSectionRemoteKeys
import java.util.*

class Converters {

    @TypeConverter
    fun fromDate(value: Date?) = value?.time

    @TypeConverter
    fun toDate(value: Long?) = value?.let { Date(value) }

}

@Database(exportSchema = false, entities = [Article::class, ArticleSection::class, ArticleSectionRemoteKeys::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    abstract fun articleSectionRemoteKeysDao(): ArticleSectionRemoteKeyDao

}

@Dao
interface ArticleDao {

    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
         select * from articles a
         inner join article_sections b on a.url = b.articleUrl
         where b.section = :section order by a.publishedAt desc
     """
    )
    fun observeArticlesForSection(section: Section): PagingSource<Int, Article>

    @Query("""
        delete from articles where url in
        (select articleUrl from article_sections where section = :section)
    """)
    suspend fun clearSection(section: Section)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllArticles(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllArticleSections(articles: List<ArticleSection>)

    @Transaction
    suspend fun insertAll(articles: List<Article>, section: Section) {
        insertAllArticles(articles)
        insertAllArticleSections(articles.map { ArticleSection(it.url, section) })
    }

}

@Dao
interface ArticleSectionRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<ArticleSectionRemoteKeys>)

    @Query("SELECT * FROM articleSectionRemoteKeys WHERE articleUrl = :articleUrl and section = :section")
    suspend fun remoteKeysByArticleSection(articleUrl: String, section: Section) : ArticleSectionRemoteKeys

    @Query("DELETE FROM articleSectionRemoteKeys WHERE section = :section")
    suspend fun clearSection(section: Section)
}

