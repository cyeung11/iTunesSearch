package com.ych.itunessearch

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
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

        homeBinding.imgTypeFilterClear.setOnClickListener {
            viewModel.filter(viewModel.uiState.value.country, null)
        }
        homeBinding.imgCountryFilterClear.setOnClickListener {
            viewModel.filter(null, viewModel.uiState.value.mediaType)
        }

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

                    if (it.mediaType == null) {
                        homeBinding.imgTypeFilterClear.visibility = View.GONE
                        homeBinding.txtMedia.setTextColor(ContextCompat.getColor(this@HomeAct, R.color.text_color))
                        homeBinding.txtMedia.setText(R.string.media_type)
                        homeBinding.llTypeFilter.setBackgroundResource(R.drawable.bg_border_rounded)
                    } else {
                        homeBinding.imgTypeFilterClear.visibility = View.VISIBLE
                        homeBinding.txtMedia.setTextColor(ContextCompat.getColor(this@HomeAct, R.color.white))
                        homeBinding.txtMedia.text = it.mediaType?.getDisplayText()
                        homeBinding.llTypeFilter.setBackgroundResource(R.drawable.bg_grey_rounded)
                    }

                    if (it.country == null) {
                        homeBinding.imgCountryFilterClear.visibility = View.GONE
                        homeBinding.txtCountry.setTextColor(ContextCompat.getColor(this@HomeAct, R.color.text_color))
                        homeBinding.txtCountry.setText(R.string.country)
                        homeBinding.llCountryFilter.setBackgroundResource(R.drawable.bg_border_rounded)
                    } else {
                        homeBinding.imgCountryFilterClear.visibility = View.VISIBLE
                        homeBinding.txtCountry.setTextColor(ContextCompat.getColor(this@HomeAct, R.color.white))
                        homeBinding.txtCountry.text = it.country?.getDisplayText()
                        homeBinding.llCountryFilter.setBackgroundResource(R.drawable.bg_grey_rounded)
                    }
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

        homeBinding.txtMedia.setOnClickListener {
            val dialog = FilterDialog.newInstance(this, ItunesFilter.getTypeFilter(this))
            dialog.listener = object : FilterAdapter.OnFilterSelectListener{
                override fun onFilterSelect(filter: FilterAdapter.Filter) {
                    dialog.dismiss()
                    viewModel.filter(viewModel.uiState.value.country, filter)
                }
            }
            dialog.show()
        }
        homeBinding.txtCountry.setOnClickListener {
            val dialog = FilterDialog.newInstance(this, ItunesFilter.countryFilters)
            dialog.listener = object : FilterAdapter.OnFilterSelectListener{
                override fun onFilterSelect(filter: FilterAdapter.Filter) {
                    dialog.dismiss()
                    viewModel.filter(filter, viewModel.uiState.value.mediaType)
                }
            }
            dialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val filterItem = menu?.findItem(R.id.filter)
        filterItem?.setIcon(if (homeBinding.scrollViewFilter.isVisible) R.drawable.btn_filter_on else R.drawable.btn_filter_off)
        filterItem?.setOnMenuItemClickListener{
            homeBinding.scrollViewFilter.visibility = if (homeBinding.scrollViewFilter.isVisible) View.GONE else View.VISIBLE
            it.setIcon(if (homeBinding.scrollViewFilter.isVisible) R.drawable.btn_filter_on else R.drawable.btn_filter_off)
            return@setOnMenuItemClickListener false
        }

        val searchItem = menu?.findItem(R.id.search)
        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                filterItem?.isVisible = false
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                filterItem?.isVisible = true
                return true
            }
        })

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