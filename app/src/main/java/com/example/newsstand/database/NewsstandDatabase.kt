package com.example.newsstand.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsstand.database.dao.ArticleDao
import com.example.newsstand.database.entity.ArticleDb

@Database(entities = [ArticleDb::class],version = NewsstandDatabase.VERSION,exportSchema = false)
abstract class NewsstandDatabase: RoomDatabase(){

    abstract fun articleDao(): ArticleDao

    companion object {
        const val VERSION = 1

        private var instance: NewsstandDatabase? = null

        fun getDatabase(context: Context): NewsstandDatabase {
            val tempInstance = instance
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsstandDatabase::class.java,
                    "newsstand.db").build()
                this.instance = instance
                return instance
            }
        }

        //todo addCallback - init db data
        /*fun getDb(context: Context): NewsstandDatabase{
            return instance ?: synchronized(this){
                instance ?: buildDb(context).also { instance = it }
            }
        }

        private fun buildDb(context: Context): NewsstandDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                NewsstandDatabase::class.java,
                "newsstand.db")
                .addCallback()
                .build()
        }*/

    }

}