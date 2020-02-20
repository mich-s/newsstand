package com.example.newsstand.network

import com.example.newsstand.network.dto.ArticleNet
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val articleService: ArticleService): BaseDataSource(){

    suspend fun getData(country:String?, category: String?, keyword: String?): Result<ResultsResponse<ArticleNet>>
            = getResult { articleService.getTopHeadlines(country,category,keyword)}

}