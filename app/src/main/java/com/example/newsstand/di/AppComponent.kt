package com.example.newsstand.di

import android.content.Context
import com.example.newsstand.MainActivity
import com.example.newsstand.favorites.FavoritesFragment
import com.example.newsstand.home.HomeFragment
import com.example.newsstand.webview.WebViewFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(webViewFragment: WebViewFragment)
    fun inject(favoritesFragment: FavoritesFragment)

}