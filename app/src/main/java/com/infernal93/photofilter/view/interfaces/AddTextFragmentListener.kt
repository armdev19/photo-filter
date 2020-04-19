package com.infernal93.photofilter.view.interfaces

import android.graphics.Typeface

/**
 * Created by Armen Mkhitaryan on 14.10.2019.
 */
interface AddTextFragmentListener {

    fun onAddTextListener(typeFace: Typeface, text: String, color: Int)
}