package com.infernal93.photofilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilters.Adapter.FrameAdapter
import com.infernal93.photofilters.Interface.AddFrameFragmentListener
import kotlinx.android.synthetic.main.fragment_frame.*

class FrameFragment : BottomSheetDialogFragment(), FrameAdapter.FrameAdapterClickListener {
    override fun onFrameItemSelected(frame: Int) {
        frameSelected = frame
    }

    internal var listener: AddFrameFragmentListener? = null
    internal lateinit var adapter: FrameAdapter
    private var frameSelected = -1

    companion object {

        private var instance: FrameFragment? = null

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
        return inflater.inflate(R.layout.fragment_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_frames.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_frames.setHasFixedSize(true)
        recycler_frames.adapter = (FrameAdapter(context!!, this))

        btn_add_frames.setOnClickListener {
            listener!!.onFrameSelected(frame = frameSelected)
        }
    }
}