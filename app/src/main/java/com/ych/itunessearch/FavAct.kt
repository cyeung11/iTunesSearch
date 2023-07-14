package com.ych.itunessearch

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ych.itunessearch.database.ITunesItem
import com.ych.itunessearch.databinding.ActFavBinding

class FavAct : AppCompatActivity(), FavAdapter.RemoveFavDelegate {

    private lateinit var favBinding: ActFavBinding

    private val viewModel: FavViewModel by viewModels()

    private val adapter: FavAdapter by lazy { FavAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favBinding = ActFavBinding.inflate(layoutInflater)
        setContentView(favBinding.root)

        setSupportActionBar(favBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        favBinding.rvList.layoutManager = layoutManager
        adapter.setHasStableIds(true)
        favBinding.rvList.adapter = adapter

        viewModel.savedItems.observe(this) {
            adapter.items = it
        }
    }

    override fun removeFav(item: ITunesItem) {
        viewModel.removeFav(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "FavAct"
    }
}