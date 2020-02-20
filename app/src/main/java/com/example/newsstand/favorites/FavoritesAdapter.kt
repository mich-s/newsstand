package com.example.newsstand.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsstand.R
import com.example.newsstand.databinding.FavoritesListItemBinding
import com.example.newsstand.domain.Article

class FavoritesAdapter: RecyclerView.Adapter<FavoritesAdapter.ViewHolder>(){

    var favoriteArticles: List<Article> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FavoritesListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                ViewHolder.layout,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = favoriteArticles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = favoriteArticles[position]
        holder.bind(item, createClickListener(item.url))
    }

    private fun createClickListener(url: String): View.OnClickListener{
        return View.OnClickListener {
            val direction = FavoritesFragmentDirections.actionFavDestToWebviewDest(url)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(val binding: FavoritesListItemBinding): RecyclerView.ViewHolder(binding.root){

        companion object {
            @LayoutRes
            val layout = R.layout.favorites_list_item
        }

        fun bind(item: Article, articleClickListener: View.OnClickListener){
            binding.apply{
                article = item
                clickListener = articleClickListener
                executePendingBindings()
            }
        }

    }

}