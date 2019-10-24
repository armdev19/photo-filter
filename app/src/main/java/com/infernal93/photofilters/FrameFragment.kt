package com.infernal93.photofilters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilters.Adapter.FrameAdapter
import com.infernal93.photofilters.Interface.AddFrameFragmentListener


class FrameFragment : BottomSheetDialogFragment(), FrameAdapter.FrameAdapterClickListener {
    override fun onFrameItemSelected(frame: Int) {
        frame_selected = frame
    }

    internal lateinit var recyclerFrame: RecyclerView
    internal lateinit var btnAddFrame: Button
    internal var listener: AddFrameFragmentListener? = null
    internal lateinit var adapter: FrameAdapter

    internal var frame_selected = -1

    companion object {

        internal var instance: FrameFragment? = null

        fun getInstance(): FrameFragment {
            if (instance == null)
                instance = FrameFragment()
            return instance!!
        }

    }

    fun setListener(fragmentListener: AddFrameFragmentListener) {

        this.listener = fragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView =  inflater.inflate(R.layout.fragment_frame, container, false)

        recyclerFrame = itemView.findViewById(R.id.recycler_frames)
        recyclerFrame.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerFrame.setHasFixedSize(true)
        recyclerFrame.adapter = (FrameAdapter(context!!, this))

        btnAddFrame = itemView.findViewById(R.id.btn_add_frames)
        btnAddFrame.setOnClickListener {
            listener!!.onFrameSelected(frame = frame_selected)
        }

        return itemView
    }


}
