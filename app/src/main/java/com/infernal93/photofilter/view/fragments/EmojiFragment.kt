package com.infernal93.photofilter.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilter.view.adapter.EmojiAdapter
import com.infernal93.photofilter.view.interfaces.EmojiFragmentListener
import com.infernal93.photofilter.R
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.fragment_emoji.*

class EmojiFragment : BottomSheetDialogFragment(), EmojiAdapter.EmojiAdapterListener {

    companion object {
        private var instance: EmojiFragment? = null

        fun getInstance(): EmojiFragment {
            if (instance == null)
                instance =
                    EmojiFragment()
            return instance!!
        }
    }

    internal var listener: EmojiFragmentListener? = null

    fun setListener(listener: EmojiFragmentListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_emoji, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_emoji.setHasFixedSize(true)
        recycler_emoji.layoutManager = GridLayoutManager(activity, 5)

        val adapter = EmojiAdapter(context!!, PhotoEditor.getEmojis(context), this)
        recycler_emoji.adapter = adapter
    }

    override fun onEmojiItemSelected(emoji: String) {
        listener!!.onEmojiItemSelected(emoji = emoji)
    }
}