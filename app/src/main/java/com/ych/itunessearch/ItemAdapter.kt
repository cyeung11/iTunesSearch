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

class ItemAdapter(private val act: HomeAct) :
    RecyclerView.Adapter<ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(act)

    var entities = listOf<Entity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isLoading = false
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    notifyItemInserted(entities.size)
                } else {
                    notifyItemRemoved(entities.size)
                }
            }
        }

    inner class LoadingHolder(binding: ItemLoadingBinding): ViewHolder(binding.root)
    inner class EntityHolder(private val binding: ItemEntryBinding) : ViewHolder(binding.root), View.OnClickListener {

        var entity: Entity? = null

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (entity != null) {

            }
        }

        fun bind(entity: Entity) {
            this.entity = entity
            binding.txtName.text = entity.trackName ?: entity.collectionName
            binding.txtArtist.text = entity.artistName
            Glide.with(binding.imgPhoto).load(entity.artworkUrl100).into(binding.imgPhoto)
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
        return if (isLoading && position == entities.size) 0 else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < entities.size && holder is EntityHolder) {
            holder.bind(entities[position])
        }
    }

    override fun getItemCount(): Int {
        return entities.size + (if (isLoading) 1 else 0)
    }

    override fun getItemId(position: Int): Long {
        if (position < entities.size) {
            return entities[position].trackId.toLong()
        } else {
            return -1
        }
    }
}
