package com.ych.itunessearch.act

import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.ych.itunessearch.adapter.FilterAdapter
import com.ych.itunessearch.dialog.FilterDialog
import com.ych.itunessearch.act.vm.HomeViewModel
import com.ych.itunessearch.adapter.SearchResultAdapter
import com.ych.itunessearch.dialog.LanguageDialog
import com.ych.itunessearch.R
import com.ych.itunessearch.databinding.ActHomeBinding
import com.ych.itunessearch.model.MediaDetail
import com.ych.itunessearch.model.MediaFilter
import kotlinx.coroutines.launch

class HomeAct : AppCompatActivity(), SearchResultAdapter.FavToggleDelegate {

    private lateinit var homeBinding: ActHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private val adapter: SearchResultAdapter by lazy { SearchResultAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupVMLang()

        homeBinding = ActHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        setupBody()
        scheduleUiUpdate()
    }

    private fun setupBody() {
        setSupportActionBar(homeBinding.toolbar)

        val toggle = ActionBarDrawerToggle(this, homeBinding.drawerLayout, homeBinding.toolbar, 0, 0)
        homeBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        homeBinding.navView.setNavigationItemSelectedListener(object: OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.itemId == R.id.lang) {
                    LanguageDialog(this@HomeAct).show()
                    homeBinding.drawerLayout.closeDrawer(GravityCompat.START)
                } else if (item.itemId == R.id.fav) {
                    startActivity(Intent(this@HomeAct, FavAct::class.java))
                }
                return false
            }
        })

        homeBinding.imgTypeFilterClear.setOnClickListener {
            viewModel.search(mediaType = null)
        }
        homeBinding.imgCountryFilterClear.setOnClickListener {
            viewModel.search(country = null)
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        homeBinding.rvResult.layoutManager = layoutManager
        adapter.setHasStableIds(true)
        homeBinding.rvResult.adapter = adapter


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
            val dialog = FilterDialog.newInstance(this, MediaFilter.getTypeFilter(this))
            dialog.listener = object : FilterAdapter.OnFilterSelectListener {
                override fun onFilterSelect(filter: MediaFilter) {
                    dialog.dismiss()
                    viewModel.search(mediaType = filter)
                }
            }
            dialog.show()
        }
        homeBinding.txtCountry.setOnClickListener {
            val dialog = FilterDialog.newInstance(this, MediaFilter.getCountryFilters(this))
            dialog.listener = object : FilterAdapter.OnFilterSelectListener {
                override fun onFilterSelect(filter: MediaFilter) {
                    dialog.dismiss()
                    viewModel.search(country = filter)
                }
            }
            dialog.show()
        }
    }

    private fun scheduleUiUpdate() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    adapter.items = it.items
                    adapter.isLoading = it.isLoading

                    homeBinding.toolbar.title = if (it.query.isNullOrBlank())
                        getString(R.string.app_name)
                    else getString(R.string.result_for, it.query)

                    if (it.mediaType == null) {
                        homeBinding.imgTypeFilterClear.visibility = View.GONE
                        homeBinding.txtMedia.setTextColor(ContextCompat.getColor(this@HomeAct,
                            R.color.text_color
                        ))
                        homeBinding.txtMedia.setText(R.string.media_type)
                        homeBinding.llTypeFilter.setBackgroundResource(R.drawable.bg_border_rounded)
                    } else {
                        homeBinding.imgTypeFilterClear.visibility = View.VISIBLE
                        homeBinding.txtMedia.setTextColor(ContextCompat.getColor(this@HomeAct,
                            R.color.white
                        ))
                        homeBinding.txtMedia.text = it.mediaType?.getDisplayText()
                        homeBinding.llTypeFilter.setBackgroundResource(R.drawable.bg_grey_rounded)
                    }

                    if (it.country == null) {
                        homeBinding.imgCountryFilterClear.visibility = View.GONE
                        homeBinding.txtCountry.setTextColor(ContextCompat.getColor(this@HomeAct,
                            R.color.text_color
                        ))
                        homeBinding.txtCountry.setText(R.string.country)
                        homeBinding.llCountryFilter.setBackgroundResource(R.drawable.bg_border_rounded)
                    } else {
                        homeBinding.imgCountryFilterClear.visibility = View.VISIBLE
                        homeBinding.txtCountry.setTextColor(ContextCompat.getColor(this@HomeAct,
                            R.color.white
                        ))
                        homeBinding.txtCountry.text = it.country?.getDisplayText()
                        homeBinding.llCountryFilter.setBackgroundResource(R.drawable.bg_grey_rounded)
                    }
                }
            }
        }

        viewModel.savedItemIds.observe(this) {
            adapter.favIds = it
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
                    viewModel.search(query = query)
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

    override fun toggleFavourite(item: MediaDetail, wasFav: Boolean) {
        if (wasFav) {
            viewModel.removeFav(item)
        } else {
            viewModel.addToFav(item)
        }
    }

    // Locale info is used by the VM during search call
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