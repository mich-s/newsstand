package com.example.newsstand.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsstand.database.entity.ArticleDb
import com.example.newsstand.domain.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles where favorite is null or favorite = 0")
    fun getArticles(): LiveData<List<ArticleDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(articles: List<ArticleDb>)

    @Query("UPDATE articles SET favorite = :isFavorite WHERE url = :url" )
    suspend fun updateArticle(url: String?, isFavorite: Boolean)

    @Query("SELECT favorite FROM articles where url = :url")
    suspend fun isFavorite(url: String?): Boolean

    @Query("SELECT * FROM articles WHERE favorite = 1")
    fun getFavoriteArticles(): LiveData<List<ArticleDb>>

    @Query("DELETE FROM articles WHERE favorite = 0 or favorite is null")
    fun removeAllArticles()

}