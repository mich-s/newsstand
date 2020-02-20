package com.example.newsstand.favorites

import androidx.lifecycle.ViewModel
import com.example.newsstand.network.ArticleRepository
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(repository: ArticleRepository): ViewModel() {

    val favoriteArticles = repository.favoriteArticles
}