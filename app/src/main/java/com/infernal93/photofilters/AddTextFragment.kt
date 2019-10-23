package com.infernal93.photofilters


import android.graphics.Color
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
import com.infernal93.photofilters.Interface.AddTextFragmentListener
import kotlinx.android.synthetic.main.fragment_add_text.*


class AddTextFragment : BottomSheetDialogFragment(), ColorAdapter.ColorAdapterClickListener {

    var colorSelected: Int = Color.parseColor("#000000") // Default color

    internal var listener: AddTextFragmentListener? = null
    fun setListener(listener: AddTextFragmentListener) {

        this.listener = listener
    }

    var edtAddText: EditText? = null
    var recyclerColor: RecyclerView? = null
    var btnDone: Button? = null
    var colorAdapter: ColorAdapter? = null

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

        colorAdapter = ColorAdapter(context!!,this@AddTextFragment)
        recyclerColor!!.adapter = colorAdapter

        btnDone!!.setOnClickListener {

            listener!!.onAddTextListener(edtAddText!!.text.toString(), colorSelected)
        }

        return itemView
    }



}
