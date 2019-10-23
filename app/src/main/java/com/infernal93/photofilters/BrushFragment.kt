package com.infernal93.photofilters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilters.Adapter.ColorAdapter
import com.infernal93.photofilters.Interface.BrushFragmentListener


class BrushFragment : BottomSheetDialogFragment(), ColorAdapter.ColorAdapterClickListener {

    override fun onColorItemSelected(color: Int) {
        listener!!.onBrushColorChangedListener(color = color)
    }

    var seekBarBrushSize: SeekBar? = null
    var seekBarBrushOpacity: SeekBar? = null
    var btnBrushState: ToggleButton? = null
    var recyclerColor: RecyclerView? = null

    var colorAdapter: ColorAdapter? = null

    companion object {

        internal var instance: BrushFragment? = null

        fun getInstance(): BrushFragment{
            if (instance == null)
                instance = BrushFragment()
            return instance!!
        }
    }

    internal var listener: BrushFragmentListener? = null

    fun setListener(listener: BrushFragmentListener) {

        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_brush, container, false)

        seekBarBrushOpacity = itemView.findViewById(R.id.seekBar_brush_opacity)
        seekBarBrushSize = itemView.findViewById(R.id.seekBar_brush_size)
        btnBrushState = itemView.findViewById(R.id.btn_brush_state)
        recyclerColor = itemView.findViewById(R.id.recycler_color)

        recyclerColor!!.setHasFixedSize(true)
        recyclerColor!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        colorAdapter = ColorAdapter(context!!,this@BrushFragment)
        recyclerColor!!.adapter = colorAdapter

        //Event
        seekBarBrushSize!!.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener!!.onBrushSizeChangedListener(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        seekBarBrushOpacity!!.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener!!.onBrushOpacityChangedListener(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        btnBrushState!!.setOnCheckedChangeListener (object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                listener!!.onBrushStateChangedListener(isChecked)
            }


        })

        return itemView
    }
}
