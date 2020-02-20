package com.example.newsstand.network


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.newsstand.database.dao.ArticleDao
import com.example.newsstand.database.entity.asDomainModel
import com.example.newsstand.domain.Article
import com.example.newsstand.network.dto.asDatabaseObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(private val articleDao: ArticleDao,
                                            private val remoteDataSource: RemoteDataSource) {

    var country = "us"
    var category: String? = null
    var keyword: String? = null

    var articles = resultLiveData(
        databaseQuery = { articleDao.getArticles() },
        networkCall = { remoteDataSource.getData(country, category, keyword)},
        saveCallResult = { articleDao.insertAll(it.articles.map { articleNet ->  articleNet.asDatabaseObject() }) })


    val favoriteArticles: LiveData<List<Article>> = Transformations.map(articleDao.getFavoriteArticles()) {it.asDomainModel()}

    private fun <T, A> resultLiveData(databaseQuery: () -> LiveData<T>,
                              networkCall: suspend () -> Result<A>,
                              saveCallResult: suspend (A) -> Unit): LiveData<Result<T>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading())
            val source = databaseQuery.invoke().map { Result.success(it) }
            emitSource(source)
            val responseStatus = networkCall.invoke()
            if (responseStatus.status == Result.Status.SUCCESS) {
                saveCallResult(responseStatus.data!!)
            } else if (responseStatus.status == Result.Status.ERROR) {
                emit(Result.error<T>(null, responseStatus.message!!))
                emitSource(source)
            }
        }


    suspend fun updateArticle(url: String?, isFavorite: Boolean){
        withContext(Dispatchers.IO){
            articleDao.updateArticle(url, isFavorite)
        }
    }

    suspend fun isFavorite(url: String?): Boolean {
        return withContext(Dispatchers.IO){
            articleDao.isFavorite(url)
        }
    }

    //another approach to process data
    suspend fun refreshArticles(country: String?, category: String?, keyword: String?){
        withContext(Dispatchers.IO){
            articleDao.removeAllArticles()
            val filtered = remoteDataSource.getData(country, category, keyword)
            val responseStatus = filtered.status
            if (responseStatus == Result.Status.SUCCESS) {
                val articlesNet = filtered.data!!.articles
                articleDao.insertAll(articlesNet.map { it.asDatabaseObject()})
                articles = articleDao.getArticles().map { Result.success(it) }
            } else if (responseStatus == Result.Status.ERROR){
                Timber.d(filtered.message)
            }

        }
    }

}