package com.example.newsstand.di

import android.content.Context
import com.example.newsstand.App
import com.example.newsstand.BuildConfig
import com.example.newsstand.database.NewsstandDatabase
import com.example.newsstand.database.dao.ArticleDao
import com.example.newsstand.home.HomeFragment
import com.example.newsstand.network.ArticleService
import com.example.newsstand.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideArticleService(okHttpClient: OkHttpClient) = provideService(okHttpClient,ArticleService::class.java)

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(BuildConfig.API_DEV_TOKEN))
        .build()

    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ArticleService.ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun <T> provideService(okHttpClient: OkHttpClient, clazz: Class<T>): T = createRetrofit(okHttpClient).create(clazz)

    @Singleton
    @Provides
    fun provideDatabase(context: Context) = NewsstandDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideArticleDao(db: NewsstandDatabase) = db.articleDao()


}