package com.example.newsstand.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsstand.databinding.ArticleListItemBinding
import com.example.newsstand.domain.Article

class ArticleAdapter(private val callback: ArticleClick): ListAdapter<Article, ArticleAdapter.ViewHolder>(ArticleDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ArticleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, callback)
    }

    class ViewHolder constructor(private val binding: ArticleListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Article, callback: ArticleClick){
            binding.also {
                it.articleCallback = callback
                it.article = item
                it.executePendingBindings()
            }
        }

    }
}

class ArticleClick(val block: (Article) -> Unit){
    fun onItemClick(article: Article) = block(article)
}

private class ArticleDiffCallback: DiffUtil.ItemCallback<Article>(){
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}