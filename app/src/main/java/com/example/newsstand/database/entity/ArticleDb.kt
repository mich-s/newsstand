package com.example.newsstand.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsstand.domain.Article

@Entity(tableName = "articles")
data class ArticleDb(@PrimaryKey @ColumnInfo(name = "title") val title: String,
                     @ColumnInfo(name = "author") val author: String,
                     @ColumnInfo(name = "description")val description: String,
                     @ColumnInfo(name = "url") val url: String,
                     @ColumnInfo(name = "urlToImg")val urlToImg: String,
                     @ColumnInfo(name = "publishedAt") val publishedAt: String,
                     @ColumnInfo(name = "content") val content: String,
                     @ColumnInfo(name = "favorite") val favorite: Boolean?,
                     @Embedded val source: SourceDb?)


fun List<ArticleDb>.asDomainModel(): List<Article> {
    return map {
        Article(
            author = it.author,
            title = it.title,
            description = it.description,
            url = it.url,
            urlToImg = it.urlToImg,
            publishedAt = it.publishedAt,
            content = it.content,
            favorite = it.favorite,
            source = it.source?.asDomainModel()
        )
    }
}

