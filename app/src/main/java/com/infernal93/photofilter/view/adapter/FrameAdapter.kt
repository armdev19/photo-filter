package com.infernal93.photofilter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.infernal93.photofilter.R

/**
 * Created by Armen Mkhitaryan on 24.10.2019.
 */
class FrameAdapter(internal var context: Context,
                   internal var listener: FrameAdapterClickListener) : RecyclerView.Adapter<FrameAdapter.FrameViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.frame_item, parent, false)
        return FrameViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return frameList.size
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.imgFrame.setImageResource(frameList.get(position))
        if (rowSelected == position)
            holder.imgCheck.visibility = View.VISIBLE
        else
            holder.imgCheck.visibility = View.INVISIBLE
    }

    internal var frameList: List<Int>

    internal var rowSelected = -1

    init {
        this.frameList = getFrameList()
    }

    private fun getFrameList(): List<Int> {

        val result = ArrayList<Int>()
        result.add(R.drawable.frame_1)
        result.add(R.drawable.frame_2)
        result.add(R.drawable.frame_3)
        result.add(R.drawable.frame_4)
        result.add(R.drawable.frame_5)
        result.add(R.drawable.frame_6)
        result.add(R.drawable.frame_7)
        result.add(R.drawable.frame_8)
        result.add(R.drawable.frame_9)
        result.add(R.drawable.frame_10)

        return result
    }

    inner class FrameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        internal var imgCheck: ImageView = itemView.findViewById(R.id.img_check_frame)
        internal var imgFrame: ImageView = itemView.findViewById(R.id.img_frame)

        init {
            itemView.setOnClickListener {
                listener.onFrameItemSelected(frame = frameList.get(adapterPosition))
                rowSelected = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    interface FrameAdapterClickListener{

        fun onFrameItemSelected(frame: Int)
    }
}