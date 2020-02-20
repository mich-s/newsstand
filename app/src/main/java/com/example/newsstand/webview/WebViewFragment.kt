package com.example.newsstand.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.newsstand.App
import com.example.newsstand.R
import com.example.newsstand.databinding.FragmentWebviewBinding
import com.example.newsstand.network.ArticleRepository
import com.example.newsstand.util.hide
import com.example.newsstand.util.show
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

@Suppress("setJavaScriptEnabled")
class WebViewFragment: Fragment(){

    @Inject lateinit var repository: ArticleRepository
    private val args: WebViewFragmentArgs by navArgs()
    private var url: String? = null
    private val viewModel: WebViewModel by viewModels { WebViewModelFactory(repository, args.url) }
    lateinit var binding: FragmentWebviewBinding

    override fun onAttach(context: Context) {
        (activity!!.application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebviewBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        url = viewModel.url
        if (url == null)
            setMenuVisibility(false)

        val progressBar = binding.progressBar
        progressBar.max = 100

        val webView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object: WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) progressBar.hide()
                else {
                    progressBar.show()
                    progressBar.progress = newProgress
                }

            }
        }
        webView.loadUrl(url)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_web_view,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_in_web -> {
                val uri = Uri.parse(url)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
                return true
            }
            R.id.add_to_favorites -> {
                if (item.icon.constantState == ContextCompat.getDrawable(activity!!, R.drawable.ic_favorite_border)!!.constantState) {
                    item.setIcon(R.drawable.ic_favorite)
                    viewModel.addToFavorites(url)
                    Snackbar.make(binding.root , "Added to favorites", Snackbar.LENGTH_SHORT).show()
                }
                else {
                    item.setIcon(R.drawable.ic_favorite_border)
                    viewModel.removeFromFavorites(url)
                    Snackbar.make(binding.root , "Removed from favorites", Snackbar.LENGTH_SHORT).show()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuItem = menu.findItem(R.id.add_to_favorites)
        viewModel.isFavorite.observe(viewLifecycleOwner, Observer<Boolean> { isFavorite ->
            if (isFavorite == null) return@Observer
            if (isFavorite) menuItem.setIcon(R.drawable.ic_favorite)
            else menuItem.setIcon(R.drawable.ic_favorite_border)
        })
        super.onPrepareOptionsMenu(menu)
    }
}