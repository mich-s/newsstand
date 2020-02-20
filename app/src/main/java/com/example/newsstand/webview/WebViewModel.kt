package com.example.newsstand.webview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsstand.network.ArticleRepository
import kotlinx.coroutines.*

class WebViewModel (private val repository: ArticleRepository, val url: String?) : ViewModel(){

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _isFavorite =  MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    init {
        isFavorite(url)
    }

    private fun isFavorite(url: String?){
        viewModelScope.launch {
            _isFavorite.value = repository.isFavorite(url)
        }
    }

    fun addToFavorites(url: String?) {
        viewModelScope.launch {
            repository.updateArticle(url, true)
        }
    }

    fun removeFromFavorites(url: String?) {
        viewModelScope.launch {
            repository.updateArticle(url, false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}