package com.ych.itunessearch

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.LocaleListCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.ych.itunessearch.databinding.ActHomeBinding
import kotlinx.coroutines.launch

class HomeAct : AppCompatActivity() {

    private lateinit var homeBinding: ActHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private val adapter: ItemAdapter by lazy { ItemAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupVMLang()

        homeBinding = ActHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        setSupportActionBar(homeBinding.toolbar)

        val toggle = ActionBarDrawerToggle(this, homeBinding.drawerLayout, homeBinding.toolbar, 0, 0)
        homeBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        homeBinding.navView.setNavigationItemSelectedListener(object: OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                LanguageDialog(this@HomeAct).show()
                homeBinding.drawerLayout.closeDrawer(GravityCompat.START)
                return false
            }
        })

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        homeBinding.rvResult.layoutManager = layoutManager
        adapter.setHasStableIds(true)
        homeBinding.rvResult.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    adapter.entities = it.entities
                    adapter.isLoading = it.isLoading

                    homeBinding.toolbar.title = if (it.query.isNullOrBlank())
                        getString(R.string.app_name)
                    else getString(R.string.result_for, it.query)
                }
            }
        }

        homeBinding.rvResult.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                        recyclerView.post {
                            viewModel.loadMore()
                        }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val searchItem = menu?.findItem(R.id.search)
//        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
//            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
//                lang?.isVisible = false
//                return true
//            }
//
//            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
//                lang?.isVisible = true
//                return true
//            }
//        })

        val searchView = menu?.findItem(R.id.search)?.getActionView() as? SearchView
        searchView?.queryHint = getString(R.string.search)
        searchView?.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.search(query)
                }
                searchItem?.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }
    override fun onLocalesChanged(locales: LocaleListCompat) {
        super.onLocalesChanged(locales)
        setupVMLang()
    }

    private fun setupVMLang() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)?.toLanguageTag()?.let {
                viewModel.changeLang(it)
            }
        } else {
            viewModel.changeLang(resources.configuration.locale.toLanguageTag())
        }
    }
    companion object {
        private const val TAG = "HomeAct"
    }
}