package com.example.newsstand.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsstand.network.ArticleRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class FavoritesViewModelFactory @Inject constructor(val repository: ArticleRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoritesViewModel::class.java)){
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}