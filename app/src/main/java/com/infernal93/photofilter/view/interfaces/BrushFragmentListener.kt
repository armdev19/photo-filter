package com.infernal93.photofilter.view.interfaces

/**
 * Created by Armen Mkhitaryan on 13.10.2019.
 */
interface BrushFragmentListener {

    fun onBrushSizeChangedListener(size: Float)
    fun onBrushOpacityChangedListener(size: Int)
    fun onBrushColorChangedListener(color: Int)
    fun onBrushStateChangedListener(isEraser: Boolean)
}