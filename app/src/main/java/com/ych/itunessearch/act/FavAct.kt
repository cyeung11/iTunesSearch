package com.ych.itunessearch.act

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ych.itunessearch.adapter.FavAdapter
import com.ych.itunessearch.act.vm.FavViewModel
import com.ych.itunessearch.model.MediaItem
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

            if (it.isEmpty()) {
                favBinding.txtEmpty.visibility = View.VISIBLE
                favBinding.rvList.visibility = View.GONE
            } else {
                favBinding.txtEmpty.visibility = View.GONE
                favBinding.rvList.visibility = View.VISIBLE
            }
        }
    }

    override fun removeFav(item: MediaItem) {
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