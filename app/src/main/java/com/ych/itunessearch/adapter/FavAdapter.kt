package com.ych.itunessearch.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.ych.itunessearch.R
import com.ych.itunessearch.model.MediaItem
import com.ych.itunessearch.databinding.ItemMediaBinding

class FavAdapter(act: Activity, private val delegate: RemoveFavDelegate) :
    RecyclerView.Adapter<FavAdapter.MediaHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(act)

    var items = listOf<MediaItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MediaHolder(private val binding: ItemMediaBinding) : ViewHolder(binding.root), View.OnClickListener {

        var item: MediaItem? = null

        init {
            binding.btnFav.setOnClickListener(this)
            binding.btnFav.setImageResource(R.drawable.btn_fav)
        }

        override fun onClick(v: View) {
            if (item != null) {
                delegate.removeFav(item!!)
            }
        }

        fun bind(media: MediaItem) {
            this.item = media
            binding.txtName.text = media.name
            binding.txtArtist.text = media.artistName
            Glide.with(binding.imgPhoto).load(media.artwork).into(binding.imgPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        val binding = ItemMediaBinding.inflate(layoutInflater)
        binding.root.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        return MediaHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    interface RemoveFavDelegate {
        fun removeFav(item: MediaItem)
    }
}
