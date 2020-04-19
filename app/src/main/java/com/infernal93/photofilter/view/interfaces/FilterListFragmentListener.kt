package com.infernal93.photofilter.view.interfaces

import com.zomato.photofilters.imageprocessors.Filter

interface FilterListFragmentListener {

    fun onFilterSelected(filter: Filter)
}