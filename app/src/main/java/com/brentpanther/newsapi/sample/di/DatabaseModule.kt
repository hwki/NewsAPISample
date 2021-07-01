package com.brentpanther.newsapi.sample.di

import android.content.Context
import androidx.room.Room
import com.brentpanther.newsapi.sample.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) : ArticleDatabase {
        return Room.databaseBuilder(context, ArticleDatabase::class.java, "db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesArticleDao(database: ArticleDatabase) = database.articleDao()
}
