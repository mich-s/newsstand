package com.example.newsstand.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ResultsResponse<T>(
    val status: String,
    val totalResults: Int,
    val articles: List<T>
)

