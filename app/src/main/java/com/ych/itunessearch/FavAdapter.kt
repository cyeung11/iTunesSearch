package com.ych.itunessearch

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.ych.itunessearch.database.ITunesItem
import com.ych.itunessearch.databinding.ItemEntryBinding

class FavAdapter(act: Activity, private val delegate: RemoveFavDelegate) :
    RecyclerView.Adapter<FavAdapter.EntityHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(act)

    var items = listOf<ITunesItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class EntityHolder(private val binding: ItemEntryBinding) : ViewHolder(binding.root), View.OnClickListener {

        var item: ITunesItem? = null

        init {
            binding.btnFav.setOnClickListener(this)
            binding.btnFav.setImageResource(R.drawable.btn_fav)
        }

        override fun onClick(v: View) {
            if (item != null) {
                delegate.removeFav(item!!)
            }
        }

        fun bind(entity: ITunesItem) {
            this.item = entity
            binding.txtName.text = entity.trackName ?: entity.collectionName
            binding.txtArtist.text = entity.artistName
            Glide.with(binding.imgPhoto).load(entity.artworkUrl100).into(binding.imgPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityHolder {
        val binding = ItemEntryBinding.inflate(layoutInflater)
        binding.root.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        return EntityHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].trackId.toLong()
    }

    interface RemoveFavDelegate {
        fun removeFav(item: ITunesItem)
    }
}
