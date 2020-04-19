package com.infernal93.photofilter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infernal93.photofilter.R
import io.github.rockerhieu.emojicon.EmojiconTextView

/**
 * Created by Armen Mkhitaryan on 14.10.2019.
 */
class EmojiAdapter(private val context: Context,
                   private val emojiItemList: List<String>,
                   private val listener: EmojiAdapterListener) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    inner class EmojiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        internal var emojiTextView: EmojiconTextView = itemView.findViewById(R.id.emoji_text_view)

        init {
            itemView.setOnClickListener {
                listener.onEmojiItemSelected(emoji = emojiItemList[adapterPosition])
            }
        }
    }

    interface EmojiAdapterListener {
        fun onEmojiItemSelected(emoji: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.emoji_item, parent, false)
        return EmojiViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return emojiItemList.size
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.emojiTextView.text = emojiItemList[position]
    }
}