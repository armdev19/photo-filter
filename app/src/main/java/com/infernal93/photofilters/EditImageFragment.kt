package com.infernal93.photofilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilters.Interface.EditImageFragmentListener
import kotlinx.android.synthetic.main.fragment_edit_image.*

class EditImageFragment : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener {

    private var listener: EditImageFragmentListener? = null

    companion object {
        private var instance: EditImageFragment?= null

        fun getInstance(): EditImageFragment{
            if (instance == null)
                instance = EditImageFragment()
            return instance!!
        }
    }

    fun resetControls() {
        seekBar_brightness.progress = 100
        seekBar_contrast.progress = 0
        seekBar_saturation.progress = 10
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        var progressConstant = progress
        if (listener != null) {

            when {
                seekBar!!.id == R.id.seekBar_brightness -> listener!!.onBrightnessChanged(progressConstant - 100)
                seekBar.id == R.id.seekBar_contrast -> {
                    progressConstant += 10
                    val floatVal = .10F * progressConstant
                    listener!!.onContrastChanged(floatVal)
                }
                seekBar.id == R.id.seekBar_saturation -> {
                    val floatVal = .10F * progressConstant
                    listener!!.onSaturationChanged(floatVal)
                }
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if (listener != null) {
            listener!!.onEditStarted()
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (listener != null) {
            listener!!.onEditCompleted()
        }
    }

    fun setListener(listener: EditImageFragmentListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekBar_brightness.max = 200
        seekBar_brightness.progress = 100

        seekBar_contrast.max = 20
        seekBar_contrast.progress = 0

        seekBar_saturation.max = 30
        seekBar_saturation.progress = 10

        seekBar_brightness.setOnSeekBarChangeListener(this)
        seekBar_contrast.setOnSeekBarChangeListener(this)
        seekBar_saturation.setOnSeekBarChangeListener(this)
    }
}