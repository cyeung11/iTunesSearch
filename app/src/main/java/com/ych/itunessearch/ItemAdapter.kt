package com.ych.itunessearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ych.itunessearch.databinding.ItemEntryBinding

class ItemAdapter(private val act: HomeAct) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(act)

    var entities = listOf<Entity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: ItemEntryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

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
            binding.txtName.text = entity.trackName
            binding.txtArtist.text = entity.artistName
            Glide.with(binding.imgPhoto).load(entity.artworkUrl100).into(binding.imgPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEntryBinding.inflate(layoutInflater)
        binding.root.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(entities[position])
    }

    override fun getItemCount(): Int {
        return entities.size
    }

}
