package com.infernal93.photofilters


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilters.Adapter.EmojiAdapter
import com.infernal93.photofilters.Interface.EmojiFragmentListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView

class EmojiFragment : BottomSheetDialogFragment(), EmojiAdapter.EmojiAdapterListener {

    internal var emojiRecycler: RecyclerView? = null
    internal var listener: EmojiFragmentListener? = null

    fun setListener(listener: EmojiFragmentListener) {

        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_emoji, container, false)

        emojiRecycler= itemView.findViewById(R.id.recycler_emoji)
        emojiRecycler!!.setHasFixedSize(true)
        emojiRecycler!!.layoutManager = GridLayoutManager(activity, 5)

        val adapter = EmojiAdapter(context!!, PhotoEditor.getEmojis(context), this)
        emojiRecycler!!.adapter = adapter

        return itemView
    }

    override fun onEmojiItemSelected(emoji: String) {

        listener!!.onEmojiItemSelected(emoji = emoji)
    }

    companion object {
        internal var instance: EmojiFragment? = null

        fun getInstance(): EmojiFragment{
            if (instance == null)
                instance = EmojiFragment()

            return instance!!
        }
    }

}
