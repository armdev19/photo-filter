package com.infernal93.photofilters


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    var typeFace = Typeface.DEFAULT

    internal var listener: AddTextFragmentListener? = null
    fun setListener(listener: AddTextFragmentListener) {

        this.listener = listener
    }

    var edtAddText: EditText? = null
    var recyclerColor: RecyclerView? = null
    var recyclerFont: RecyclerView? = null
    var btnDone: Button? = null
    var colorAdapter: ColorAdapter? = null
    var fontAdapter: FontAdapter? = null

    override fun onColorItemSelected(color: Int) {

        colorSelected = color
    }

    companion object {
        internal var instance: AddTextFragment? = null

        fun getInstance(): AddTextFragment{
            if (instance == null)
                instance = AddTextFragment()

            return instance!!
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_add_text, container, false)

        edtAddText = itemView.findViewById(R.id.edt_add_text)
        btnDone = itemView.findViewById(R.id.btn_done)
        recyclerColor = itemView.findViewById(R.id.recycler_color_text)
        recyclerColor!!.setHasFixedSize(true)
        recyclerColor!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recyclerFont = itemView.findViewById(R.id.recycler_font)
        recyclerFont!!.setHasFixedSize(true)
        recyclerFont!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        colorAdapter = ColorAdapter(context!!,this@AddTextFragment)
        recyclerColor!!.adapter = colorAdapter

        fontAdapter = FontAdapter(context!!, this@AddTextFragment)
        recyclerFont!!.adapter = fontAdapter

        btnDone!!.setOnClickListener {

            listener!!.onAddTextListener(typeFace = typeFace, text = edtAddText!!.text.toString(), color = colorSelected)
        }

        return itemView
    }



}
