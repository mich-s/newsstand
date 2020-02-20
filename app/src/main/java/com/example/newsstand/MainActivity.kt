package com.example.newsstand

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsstand.databinding.ActivityMainBinding
import com.example.newsstand.network.ArticleRepository
import com.example.newsstand.util.FilterClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.filter_backdrop.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var repository: ArticleRepository
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private var menuMain: Menu? = null

    private var filterMenuItem: MenuItem? = null
    private val listener = NavController.OnDestinationChangedListener {
            navController, _, _ -> filterMenuItem?.isVisible = navController.currentDestination?.id == R.id.home_dest
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(listener)

//        appBarConfiguration = AppBarConfiguration(navController.graph)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.home_dest, R.id.fav_dest))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavView = binding.bottomNavView
        bottomNavView.setupWithNavController(navController)

        setBackdropFilterAction()

    }

    private fun setBackdropFilterAction(){
        val backdrop = binding.layoutBackdrop
        backdrop.also {
            val searchText = it.search_text
            val category = it.radio_group_category
            val country = it.radio_group_country

            var countryTxt: String? = "us"
            country.setOnCheckedChangeListener {_, checkedId ->
                countryTxt = when(checkedId){
                    R.id.country_1 -> "us"
                    R.id.country_2 -> "gb"
                    R.id.country_3 -> "pl"
                    R.id.country_4 -> "fr"
                    R.id.country_5 -> "de"
                    else -> "us"
                }
            }

            var categoryTxt: String? = null
            category.setOnCheckedChangeListener {_, checkedId ->
                val searchCategory = findViewById<RadioButton>(checkedId).text.toString().decapitalize()
                categoryTxt = if (searchCategory != "none") searchCategory else null
            }

            it.search_button.apply {
                setOnClickListener {
                    scope.launch {
                        repository.refreshArticles(countryTxt,categoryTxt, searchText.text.toString())
                    }
                    menuMain!!.performIdentifierAction(R.id.filter,0)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        menuMain = menu
        val menuItem = menu!!.findItem(R.id.filter)

        menuItem.setOnMenuItemClickListener(FilterClickListener(this,binding.frame) {
            val favDest = bottomNavView.menu.findItem(R.id.fav_dest)
            favDest.isVisible = !favDest.isVisible
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        filterMenuItem = menu!!.findItem(R.id.filter)
        if(navController.currentDestination?.id == R.id.home_dest)
            filterMenuItem?.isVisible = (navController.currentDestination?.id == R.id.home_dest)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(listener)
    }

}
