package com.infernal93.photofilters.Interface

import com.zomato.photofilters.imageprocessors.Filter

interface FilterListFragmentListener {

    fun onFilterSelected(filter: Filter)
}