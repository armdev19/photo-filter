package com.infernal93.photofilters

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilters.Adapter.ColorAdapter
import com.infernal93.photofilters.Adapter.FontAdapter
import com.infernal93.photofilters.Interface.AddTextFragmentListener
import kotlinx.android.synthetic.main.fragment_add_text.*
import java.lang.StringBuilder

class AddTextFragment : BottomSheetDialogFragment(), ColorAdapter.ColorAdapterClickListener,
    FontAdapter.FontAdapterClickListener {

    override fun onFontSelected(fontName: String) {
        typeFace = Typeface.createFromAsset(context!!.assets, StringBuilder("fonts/")
            .append(fontName).toString())
    }

    var colorSelected: Int = Color.parseColor("#000000") // Default color
    private var typeFace = Typeface.DEFAULT

    internal var listener: AddTextFragmentListener? = null

    fun setListener(listener: AddTextFragmentListener) {
        this.listener = listener
    }

    var colorAdapter: ColorAdapter? = null
    var fontAdapter: FontAdapter? = null

    override fun onColorItemSelected(color: Int) {
        colorSelected = color
    }

    companion object {
        private var instance: AddTextFragment? = null

        fun getInstance(): AddTextFragment{
            if (instance == null)
                instance = AddTextFragment()

            return instance!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_color_text.setHasFixedSize(true)
        recycler_color_text.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recycler_font.setHasFixedSize(true)
        recycler_font.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        colorAdapter = ColorAdapter(context!!,this@AddTextFragment)
        recycler_color_text.adapter = colorAdapter

        fontAdapter = FontAdapter(context!!, this@AddTextFragment)
        recycler_font.adapter = fontAdapter

        btn_done.setOnClickListener {
            listener!!.onAddTextListener(typeFace = typeFace, text = edt_add_text.text.toString(), color = colorSelected)
        }
    }
}