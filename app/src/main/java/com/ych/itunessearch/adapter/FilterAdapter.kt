package com.ych.itunessearch.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ych.itunessearch.databinding.ItemFilterBinding
import com.ych.itunessearch.model.MediaFilter


class FilterAdapter(
    act: Activity,
    private val dataList: List<MediaFilter>,
    private val listener: OnFilterSelectListener
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private val layoutInflater = LayoutInflater.from(act)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(layoutInflater, parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(vh: FilterViewHolder, position: Int) {
        vh.onBind(dataList.get(position))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class FilterViewHolder(
        private val binding: ItemFilterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        var filter: MediaFilter? = null

        init {
            binding.container.setOnClickListener {
                if (filter != null) {
                    listener.onFilterSelect(filter!!)
                }
            }
        }

        fun onBind(filter: MediaFilter) {
            this.filter = filter
            binding.txtName.text = filter.getDisplayText()
        }
    }

    interface OnFilterSelectListener{
        fun onFilterSelect(filter: MediaFilter)
    }
}