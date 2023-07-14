package com.ych.itunessearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.ych.itunessearch.databinding.ItemEntryBinding
import com.ych.itunessearch.databinding.ItemLoadingBinding

class ItemAdapter(act: HomeAct, private val delegate: FavToggleDelegate) :
    RecyclerView.Adapter<ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(act)

    var favIds = listOf<Int>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var items = listOf<ITunesDetail>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
    inner class EntityHolder(private val binding: ItemEntryBinding) : ViewHolder(binding.root), View.OnClickListener {

        var item: ITunesDetail? = null

        init {
            binding.btnFav.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (item != null) {
                delegate.toggleFavourite(item!!, favIds.contains(item!!.trackId))
            }
        }

        fun bind(entity: ITunesDetail) {
            this.item = entity
            binding.txtName.text = entity.trackName ?: entity.collectionName
            binding.txtArtist.text = entity.artistName
            Glide.with(binding.imgPhoto).load(entity.artworkUrl100).into(binding.imgPhoto)

            binding.btnFav.setImageResource(
                if (favIds.contains(entity.trackId)) R.drawable.btn_fav else R.drawable.btn_unfav
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
            val binding = ItemEntryBinding.inflate(layoutInflater)
            binding.root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            EntityHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == items.size) 0 else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < items.size && holder is EntityHolder) {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size + (if (isLoading) 1 else 0)
    }

    override fun getItemId(position: Int): Long {
        if (position < items.size) {
            return items[position].trackId.toLong()
        } else {
            return -1
        }
    }

    interface FavToggleDelegate {
        fun toggleFavourite(item: ITunesDetail, wasFav: Boolean)
    }
}
