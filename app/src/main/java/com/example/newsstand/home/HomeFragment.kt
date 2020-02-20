package com.example.newsstand.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsstand.App
import com.example.newsstand.database.entity.ArticleDb
import com.example.newsstand.database.entity.asDomainModel
import com.example.newsstand.databinding.FragmentHomeBinding
import com.example.newsstand.network.ArticleService
import com.example.newsstand.network.Result
import com.example.newsstand.util.createViewModel
import com.example.newsstand.util.hide
import com.example.newsstand.util.show
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import javax.inject.Inject

class HomeFragment: Fragment(){

    @Inject lateinit var factory: HomeViewModelFactory
    @Inject lateinit var service: ArticleService
    private lateinit var viewModel: HomeViewModel

    override fun onAttach(context: Context) {
        (activity!!.application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this, HomeViewModelFactory()).get(HomeViewModel::class.java)
        viewModel = createViewModel(factory)

        val adapter = ArticleAdapter(ArticleClick {
            val direction = HomeFragmentDirections.actionHomeDestToWebviewDest(it.url)
            findNavController().navigate(direction)
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.articles.observe(viewLifecycleOwner, Observer<Result<List<ArticleDb>>> { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    result.data?.let { adapter.submitList(it.asDomainModel()) }
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }
}