package com.infernal93.photofilter.view.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilter.view.adapter.ThumbnailAdapter
import com.infernal93.photofilter.view.interfaces.FilterListFragmentListener
import com.infernal93.photofilter.R
import com.infernal93.photofilter.utils.BitMapUtils
import com.infernal93.photofilter.utils.SpaceItemDecoration
import com.infernal93.photofilter.view.activities.MainActivity
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager
import kotlinx.android.synthetic.main.fragment_filter_list.recycler_filter_list

class FilterListFragment : BottomSheetDialogFragment(), FilterListFragmentListener {

    internal var listener: FilterListFragmentListener? = null
    internal lateinit var adapter: ThumbnailAdapter
    private lateinit var thumbnailItemList: MutableList<ThumbnailItem>

    companion object {

        private var instance: FilterListFragment? = null
        internal var bitmap: Bitmap? = null

        fun getInstance(bitmapSave: Bitmap?): FilterListFragment {
            bitmap = bitmapSave
            if (instance == null) {
                instance =
                    FilterListFragment()
            }
            return instance!!
        }
    }

    fun setListener(listFragmentListener: FilterListFragmentListener) {
        this.listener = listFragmentListener
    }

    override fun onFilterSelected(filter: Filter) {
        if (listener != null)
            listener!!.onFilterSelected(filter = filter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thumbnailItemList = ArrayList()
        adapter = ThumbnailAdapter(activity!!, thumbnailItemList, this)

        recycler_filter_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_filter_list.itemAnimator = DefaultItemAnimator()
        val space = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics)
            .toInt()
        recycler_filter_list.addItemDecoration(SpaceItemDecoration(space = space))
        recycler_filter_list.adapter = adapter

        displayImage(bitmap = bitmap)
    }

     private fun displayImage(bitmap: Bitmap?) {

        val r = Runnable {
            val thumbImage: Bitmap?

            if (bitmap == null)
                thumbImage = BitMapUtils.getBitmapFromAssets(activity!!,
                    filename = MainActivity.Main.IMAGE_NAME, width = 100, height = 100)
            else
                thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false)

            if (thumbImage == null)
                return@Runnable

            ThumbnailsManager.clearThumbs()
            thumbnailItemList.clear()
            // add normal bitmap first
            val thumbnailItem = ThumbnailItem()
            thumbnailItem.image = thumbImage
            thumbnailItem.filterName = "Normal"
            ThumbnailsManager.addThumb(thumbnailItem)
            // add Filter Pack
            val filters = FilterPack.getFilterPack(activity!!)
            for (filter in filters) {
                val item = ThumbnailItem()
                item.image = thumbImage
                item.filter = filter
                item.filterName = filter.name
                ThumbnailsManager.addThumb(item)
            }
            thumbnailItemList.addAll(ThumbnailsManager.processThumbs(activity))
            activity!!.runOnUiThread{
                adapter.notifyDataSetChanged()
            }
        }
        Thread(r).start()
    }
}