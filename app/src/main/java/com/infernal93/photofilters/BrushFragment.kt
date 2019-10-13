package com.infernal93.photofilters


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilters.Adapter.ColorAdapter
import com.infernal93.photofilters.Interface.BrushFragmentListener
import kotlinx.android.synthetic.main.fragment_brush.*


class BrushFragment : BottomSheetDialogFragment(), ColorAdapter.ColorSelectedListener {

    override fun onColorSelectedListener(color: Int) {

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

        colorAdapter = ColorAdapter(context, genColorList(), this@BrushFragment)
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

    private fun genColorList(): MutableList<Int>? {
            var colorList = ArrayList<Int>()

        colorList.add(Color.parseColor("#bada55"))
        colorList.add(Color.parseColor("#7fe5f0"))
        colorList.add(Color.parseColor("#ff0000"))
        colorList.add(Color.parseColor("#ff80ed"))
        colorList.add(Color.parseColor("#696969"))
        colorList.add(Color.parseColor("#133337"))
        colorList.add(Color.parseColor("#065535"))

        colorList.add(Color.parseColor("#c0c0c0"))
        colorList.add(Color.parseColor("#5ac18e"))
        colorList.add(Color.parseColor("#666666"))
        colorList.add(Color.parseColor("#dcedc1"))
        colorList.add(Color.parseColor("#f7347a"))
        colorList.add(Color.parseColor("#000000"))
        colorList.add(Color.parseColor("#ffffff"))

        colorList.add(Color.parseColor("#ffc0cb"))
        colorList.add(Color.parseColor("#420420"))
        colorList.add(Color.parseColor("#ffe4e1"))
        colorList.add(Color.parseColor("#008080"))
        colorList.add(Color.parseColor("#ffd700"))
        colorList.add(Color.parseColor("#e6e6fa"))
        colorList.add(Color.parseColor("#ff7373"))

        colorList.add(Color.parseColor("#00ffff"))
        colorList.add(Color.parseColor("#ffa500"))
        colorList.add(Color.parseColor("#40e0d0"))
        colorList.add(Color.parseColor("#d3ffce"))
        colorList.add(Color.parseColor("#f0f8ff"))
        colorList.add(Color.parseColor("#b0e0e6"))
        colorList.add(Color.parseColor("#0000ff"))

        colorList.add(Color.parseColor("#c6e2ff"))
        colorList.add(Color.parseColor("#faebd7"))
        colorList.add(Color.parseColor("#003366"))
        colorList.add(Color.parseColor("#fa8072"))
        colorList.add(Color.parseColor("#eeeeee"))
        colorList.add(Color.parseColor("#7fffd4"))
        colorList.add(Color.parseColor("#800000"))

        colorList.add(Color.parseColor("#ffff00"))
        colorList.add(Color.parseColor("#cccccc"))
        colorList.add(Color.parseColor("#ffb6c1"))
        colorList.add(Color.parseColor("#800080"))
        colorList.add(Color.parseColor("#00ff00"))
        colorList.add(Color.parseColor("#ffc3a0"))
        colorList.add(Color.parseColor("#333333"))

        colorList.add(Color.parseColor("#20b2aa"))
        colorList.add(Color.parseColor("#f08080"))
        colorList.add(Color.parseColor("#fff68f"))
        colorList.add(Color.parseColor("#4ca3dd"))
        colorList.add(Color.parseColor("#66cdaa"))
        colorList.add(Color.parseColor("#c39797"))
        colorList.add(Color.parseColor("#f6546a"))

        colorList.add(Color.parseColor("#468499"))
        colorList.add(Color.parseColor("#ff6666"))
        colorList.add(Color.parseColor("#ff7f50"))
        colorList.add(Color.parseColor("#ffdab9"))
        colorList.add(Color.parseColor("#ff00ff"))
        colorList.add(Color.parseColor("#00ced1"))
        colorList.add(Color.parseColor("#c0d6e4"))

        colorList.add(Color.parseColor("#660066"))
        colorList.add(Color.parseColor("#008000"))
        colorList.add(Color.parseColor("#0e2f44"))
        colorList.add(Color.parseColor("#101010"))
        colorList.add(Color.parseColor("#8b0000"))
        colorList.add(Color.parseColor("#f5f5f5"))
        colorList.add(Color.parseColor("#afeeee"))

        colorList.add(Color.parseColor("#990000"))
        colorList.add(Color.parseColor("#088da5"))
        colorList.add(Color.parseColor("#808080"))
        colorList.add(Color.parseColor("#b4eeb4"))
        colorList.add(Color.parseColor("#daa520"))
        colorList.add(Color.parseColor("#cbbeb5"))
        colorList.add(Color.parseColor("#f5f5dc"))

        colorList.add(Color.parseColor("#ffff66"))
        colorList.add(Color.parseColor("#dddddd"))
        colorList.add(Color.parseColor("#b6fcd5"))
        colorList.add(Color.parseColor("#6897bb"))
        colorList.add(Color.parseColor("#00ff7f"))
        colorList.add(Color.parseColor("#8a2be2"))
        colorList.add(Color.parseColor("#000080"))

        colorList.add(Color.parseColor("#ff4040"))
        colorList.add(Color.parseColor("#81d8d0"))
        colorList.add(Color.parseColor("#a0db8e"))
        colorList.add(Color.parseColor("#794044"))
        colorList.add(Color.parseColor("#3399ff"))
        colorList.add(Color.parseColor("#ccff00"))
        colorList.add(Color.parseColor("#66cccc"))


        return colorList
    }


}
