package com.example.newsstand.network.dto

import com.example.newsstand.database.entity.ArticleDb
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleNet(val author: String? = "",
                      val title: String?,
                      val description: String?,
                      val url: String?,
                      val urlToImage: String?,
                      val publishedAt: String?,
                      val content: String?,
                      val source: SourceNet?)

fun ArticleNet.asDatabaseObject(): ArticleDb {
    return ArticleDb(
        author = author ?: "",
        title = title ?: "",
        description = description ?: "",
        url = url ?: "",
        urlToImg = urlToImage ?: "",
        publishedAt = publishedAt ?: "",
        content = content ?: "",
        source = source?.asDatabaseObject(),
        favorite = null
    )
}
