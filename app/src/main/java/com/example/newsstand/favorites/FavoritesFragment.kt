package com.example.newsstand.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsstand.App    
import com.example.newsstand.databinding.FragmentFavoritesBinding
import com.example.newsstand.domain.Article
import javax.inject.Inject

class FavoritesFragment: Fragment(){

    @Inject lateinit var factory: FavoritesViewModelFactory
    private val viewModel: FavoritesViewModel by viewModels { factory }

    private var favoritesAdapter: FavoritesAdapter? = null

    override fun onAttach(context: Context) {
        (activity!!.application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        favoritesAdapter = FavoritesAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = favoritesAdapter
        }

        viewModel.favoriteArticles.observe(viewLifecycleOwner, Observer<List<Article>> {
                favorites -> favorites?.apply { favoritesAdapter?.favoriteArticles = favorites }
        })

        return binding.root
    }
}