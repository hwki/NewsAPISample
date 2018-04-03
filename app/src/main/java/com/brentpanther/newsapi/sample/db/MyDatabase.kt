package com.brentpanther.newsapi.sample.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.brentpanther.newsapi.sample.MyApplication
import com.brentpanther.newsapi.sample.network.Article
import com.brentpanther.newsapi.sample.network.ArticleSection
import com.brentpanther.newsapi.sample.network.Section
import com.brentpanther.newsapi.sample.network.Source
import java.util.*

class Converters {

    @TypeConverter
    fun fromDate(date: Date?) = date?.time

    @TypeConverter
    fun toDate(millis: Long?) = millis?.let { Date(millis) }

}

@Database(entities = [Article::class, Source::class,
    ArticleSection::class, Section::class], version = 1)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {

    companion object {
        var db = Room.databaseBuilder(MyApplication.getInstance(), MyDatabase::class.java, "db").build()
    }

    abstract fun articleDao(): ArticleDao

}

@Dao
interface ArticleDao {

    @Query("""
        select a.* from article a inner join
        articlesection b on a.id = b.articleId
        inner join section c on b.sectionId = c.id
        where c.name = :section order by publishedAt desc limit 25
    """)
    fun getBySection(section: String): LiveData<List<Article>>

    @Query("select * from article where id = :id")
    fun findById(id: Long): Article

    @Query("select * from article where url = :url")
    fun findByUrl(url: String): Article

    @Insert
    fun insertSection(section: Section) : Long

    @Update
    fun updateSection(section: Section)

    @Query("select * from section where name = :name")
    fun findSectionByName(name: String): Section?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(articles: List<Article>) : List<Long>

    @Query("update section set lastupdated = :now where id = :sectionId")
    fun sectionUpdated(sectionId: Long, now: Long)

    @Transaction
    fun insertAll(section: String, articles: List<Article>) {
        val sectionId = getSectionId(section)
        articles.forEach {
            it.publisher = it.source?.name
        }
        val ids = insert(articles).filter {it > 0}.map{ArticleSection(it, sectionId)}
        insertArticleSections(ids)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArticleSections(articleSections: List<ArticleSection>)

    private fun getSectionId(sectionName: String) : Long {
        val now = System.currentTimeMillis()
        val section = findSectionByName(sectionName)

        return if (section == null) {
            insertSection(Section(name = sectionName, lastUpdated = now))
        } else {
            section.lastUpdated = now
            updateSection(section)
            section.id!!
        }
    }

    @Query("""
        delete from article where id IN
        (select articleid from articlesection where sectionid = :sectionId)
        """)
    fun deleteArticleBySection(sectionId: Long)

    @Query("delete from articlesection where sectionid = :sectionId")
    fun deleteArticleSectionBySection(sectionId: Long)

    @Query("update section set lastupdated = NULL where id = :sectionId")
    fun clearSectionTime(sectionId: Long)

    @Transaction
    fun clearSection(sectionName: String) {
        val sectionId = findSectionByName(sectionName)?.id
        sectionId?.let {
            deleteArticleBySection(it)
            deleteArticleSectionBySection(it)
            deleteArticleSectionBySection(it)
            clearSectionTime(it)
        }
    }

}
