package com.ych.itunessearch

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ych.itunessearch.databinding.BottomsheetFilterBinding


class FilterDialog private constructor(
    private val act: Activity,
    private val data: List<FilterAdapter.Filter>
) : BottomSheetDialog(act, R.style.BaseBottomSheetTheme), FilterAdapter.OnFilterSelectListener {

    var listener: FilterAdapter.OnFilterSelectListener? = null

    private val adapter: FilterAdapter by lazy {
        FilterAdapter(act, data, this)
    }

    private var viewBinding: BottomsheetFilterBinding =
        BottomsheetFilterBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(viewBinding.root)

        setupBottomSheet()
    }

    override fun onFilterSelect(filter: FilterAdapter.Filter) {
        if (listener != null) {
            listener!!.onFilterSelect(filter)
        }
    }

    private fun setupBottomSheet() {

        dismissWithAnimation = true

        this.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let {

            it.setBackgroundColor(Color.TRANSPARENT)

            val behavior = BottomSheetBehavior.from(it)
            behavior.peekHeight = 10
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        viewBinding.rvFilter.adapter = adapter

        viewBinding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }


    companion object {

        fun newInstance(act: Activity, data: List<FilterAdapter.Filter>): FilterDialog {
            return FilterDialog(act, data)
        }
    }


}