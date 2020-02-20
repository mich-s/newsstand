package com.example.newsstand.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsstand.network.ArticleRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(private val repository: ArticleRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}