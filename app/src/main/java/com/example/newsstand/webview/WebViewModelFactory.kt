package com.example.newsstand.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsstand.network.ArticleRepository

class WebViewModelFactory (private val repository: ArticleRepository,private val url: String?) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebViewModel::class.java)){
            return WebViewModel(repository, url) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}