package com.example.newsstand.domain

data class Article(val author: String,
                   val title: String,
                   val description: String,
                   val url: String,
                   val urlToImg: String,
                   val publishedAt: String,
                   val content: String,
                   val favorite: Boolean?,
                   val source: Source?)