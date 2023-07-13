package com.ych.itunessearch

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import com.ych.itunessearch.databinding.ActHomeBinding

class HomeAct : AppCompatActivity() {

    private lateinit var homeBinding: ActHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        setSupportActionBar(homeBinding.toolbar)
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

        return true
    }

    override fun onResume() {
        super.onResume()
        val list = AppCompatDelegate.getApplicationLocales()
        for (i in 0 until list.size()) {
            Log.d("Act", "onResume: " + list[i].toString())
        }
    }
}