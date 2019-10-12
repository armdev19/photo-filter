package com.infernal93.photofilters


import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infernal93.photofilters.Adapter.ThumbnailAdapter
import com.infernal93.photofilters.Interface.FilterListFragmentListener
import com.infernal93.photofilters.Utils.BitMapUtils
import com.infernal93.photofilters.Utils.SpaceItemDecoration
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager
import kotlinx.android.synthetic.main.fragment_filter_list.*

class FilterListFragment : BottomSheetDialogFragment(), FilterListFragmentListener {

    internal lateinit var recycler_view: RecyclerView
    internal var listener: FilterListFragmentListener? = null
    internal lateinit var adapter: ThumbnailAdapter
    internal lateinit var thumbnailItemList: MutableList<ThumbnailItem>

    companion object {

        internal var instance: FilterListFragment? = null

        fun getInstance(): FilterListFragment {
            if (instance == null)
                instance = FilterListFragment()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_filter_list, container, false)

        thumbnailItemList = ArrayList()
        adapter = ThumbnailAdapter(activity!!, thumbnailItemList, this)

        recycler_view = itemView.findViewById(R.id.recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.itemAnimator = DefaultItemAnimator()
        val space = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics)
            .toInt()
        recycler_view.addItemDecoration(SpaceItemDecoration(space = space))
        recycler_view.adapter = adapter

        displayImage(null)

        return itemView
    }

     fun displayImage(bitmap: Bitmap?) {

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
