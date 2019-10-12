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

    private var seekBarBrightness: SeekBar? = null
    private var seekBarSaturation: SeekBar? = null
    private var seekBarContrast: SeekBar? = null

    companion object {
        internal var instance: EditImageFragment?= null

        fun getInstance(): EditImageFragment{
            if (instance == null)
                instance = EditImageFragment()
            return instance!!
        }

    }

    fun resetControls() {

//        seekBarBrightness!!.progress = 100
//        seekBarContrast!!.progress = 0
//        seekBarSaturation!!.progress = 10
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        var progress = progress
        if (listener != null) {

            if (seekBar!!.id == R.id.seekbar_brightness) {

                listener!!.onBrightnessChanged(progress - 100)

            } else if (seekBar.id == R.id.seekbar_constrant) {

                progress += 10
                val floatVal = .10f * progress

                listener!!.onConstrantChanged(floatVal)

            } else if (seekBar.id == R.id.seekbar_saturation) {

                val floatVal = .10f * progress

                listener!!.onSaturationChanged(floatVal)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_image, container, false)


        // View
        seekBarBrightness = view.findViewById(R.id.seekbar_brightness)
        seekBarSaturation = view.findViewById(R.id.seekbar_saturation)
        seekBarContrast = view.findViewById(R.id.seekbar_constrant)

        seekBarBrightness!!.max = 200
        seekBarBrightness!!.progress = 100

        seekBarContrast!!.max = 20
        seekBarContrast!!.progress = 0

        seekBarSaturation!!.max = 30
        seekBarSaturation!!.progress = 10

        seekBarBrightness!!.setOnSeekBarChangeListener(this)
        seekBarContrast!!.setOnSeekBarChangeListener(this)
        seekBarSaturation!!.setOnSeekBarChangeListener(this)
        return view
    }


}
