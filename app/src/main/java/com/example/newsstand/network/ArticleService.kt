package com.example.newsstand.network

import com.example.newsstand.network.dto.ArticleNet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {

    companion object {
        const val ENDPOINT = "https://newsapi.org/v2/"
    }

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("q") keyword: String?): Response<ResultsResponse<ArticleNet>>


}