package com.example.newsstand.home

import androidx.lifecycle.ViewModel
import com.example.newsstand.network.ArticleRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(repository: ArticleRepository) : ViewModel(){

    val articles = repository.articles

}