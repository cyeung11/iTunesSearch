package com.ych.itunessearch

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ych.itunessearch.databinding.ItemFilterBinding


class FilterAdapter(
    act: Activity,
    private val dataList: List<Filter>,
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
        val binding: ItemFilterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        var filter: Filter? = null

        init {
            binding.container.setOnClickListener {
                if (filter != null) {
                    listener.onFilterSelect(filter!!)
                }
            }
        }

        fun onBind(filter: Filter) {
            this.filter = filter
            binding.txtName.text = filter.getDisplayText()
        }
    }

    interface Filter {
        fun getDisplayText(): String
        fun getRequestValue(): String
    }

    interface OnFilterSelectListener{
        fun onFilterSelect(filter: Filter)
    }
}