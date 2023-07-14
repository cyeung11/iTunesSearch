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
import com.ych.itunessearch.databinding.ItemMediaBinding
import com.ych.itunessearch.databinding.ItemLoadingBinding
import com.ych.itunessearch.model.MediaDetail

class SearchResultAdapter(act: Activity, private val favDelegate: FavToggleDelegate) :
    RecyclerView.Adapter<ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(act)

    // List of track ids that was saved as favorites
    var favIds = listOf<Int>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var items = listOf<MediaDetail>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // When true, add a loading view on the bottom of the list
    var isLoading = false
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    notifyItemInserted(items.size)
                } else {
                    notifyItemRemoved(items.size)
                }
            }
        }

    inner class LoadingHolder(binding: ItemLoadingBinding): ViewHolder(binding.root)
    inner class MediaHolder(private val binding: ItemMediaBinding) : ViewHolder(binding.root), View.OnClickListener {

        var item: MediaDetail? = null

        init {
            binding.btnFav.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (item != null) {
                favDelegate.toggleFavourite(item!!, favIds.contains(item!!.id))
            }
        }

        fun bind(media: MediaDetail) {
            this.item = media
            binding.txtName.text = media.name
            binding.txtArtist.text = media.artistName
            Glide.with(binding.imgPhoto).load(media.artworkUrl100).into(binding.imgPhoto)

            binding.btnFav.setImageResource(
                if (favIds.contains(media.id)) R.drawable.btn_fav else R.drawable.btn_unfav
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 0) {
            val binding = ItemLoadingBinding.inflate(layoutInflater)
            binding.root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            LoadingHolder(binding)
        } else {
            val binding = ItemMediaBinding.inflate(layoutInflater)
            binding.root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            MediaHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == items.size) 0 else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < items.size && holder is MediaHolder) {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size + (if (isLoading) 1 else 0)
    }

    override fun getItemId(position: Int): Long {
        if (position < items.size) {
            return items[position].id.toLong()
        } else {
            return -1
        }
    }

    interface FavToggleDelegate {
        fun toggleFavourite(item: MediaDetail, wasFav: Boolean)
    }
}
