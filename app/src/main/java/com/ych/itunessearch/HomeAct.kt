package com.ych.itunessearch

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ych.itunessearch.databinding.ActHomeBinding
import kotlinx.coroutines.launch

class HomeAct : AppCompatActivity() {

    private lateinit var homeBinding: ActHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private val adapter: ItemAdapter by lazy { ItemAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        setSupportActionBar(homeBinding.toolbar)

        homeBinding.rvResult.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        homeBinding.rvResult.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    adapter.entities = it.response?.results ?: listOf()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val lang = menu?.findItem(R.id.lang)
        lang?.setOnMenuItemClickListener {
            LanguageDialog(this@HomeAct).show()
            return@setOnMenuItemClickListener true
        }

        val searchItem = menu?.findItem(R.id.search)
        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                lang?.isVisible = false
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                lang?.isVisible = true
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
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }
}