package com.infernal93.photofilters

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.accessibility.AccessibilityEventSource
import android.webkit.PermissionRequest
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.infernal93.photofilters.Adapter.ViewPagerAdapter
import com.infernal93.photofilters.Interface.BrushFragmentListener
import com.infernal93.photofilters.Interface.EditImageFragmentListener
import com.infernal93.photofilters.Interface.EmojiFragmentListener
import com.infernal93.photofilters.Interface.FilterListFragmentListener
import com.infernal93.photofilters.Utils.BitMapUtils
import com.infernal93.photofilters.Utils.NonSwipeViewPager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception
import kotlin.jvm.internal.MutablePropertyReference

class MainActivity : AppCompatActivity(), FilterListFragmentListener, EditImageFragmentListener,
    BrushFragmentListener, EmojiFragmentListener {

    override fun onEmojiItemSelected(emoji: String) {
        photoEditor.addEmoji(emoji)
    }

    override fun onBrushSizeChangedListener(size: Float) {
        photoEditor.brushSize = (size)
    }

    override fun onBrushOpacityChangedListener(size: Int) {
        photoEditor.setOpacity(size)
    }

    override fun onBrushColorChangedListener(color: Int) {
        photoEditor.brushColor = color
    }

    override fun onBrushStateChangedListener(isEraser: Boolean) {
        if (isEraser)
            photoEditor.brushEraser()
        else
            photoEditor.setBrushDrawingMode(true)
    }

    val SELECT_GALLERY_PERMISSION = 1000

    lateinit var photoEditor: PhotoEditor

    init {
        System.loadLibrary("NativeImageProcessor")
    }

    override fun onBrightnessChanged(brightness: Int) {
        brightnessFinal = brightness
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onConstrantChanged(constrant: Float) {
        contrastFinal = constrant
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(constrant))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onEditStarted() {

    }

    override fun onEditCompleted() {
        val bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        finalImage = myFilter.processFilter(bitmap)
    }

    override fun onFilterSelected(filter: Filter) {
        resetControls()
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        image_preview.source.setImageBitmap(filter.processFilter(filteredImage))
        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun resetControls() {
        if (editImageFragment != null)
            editImageFragment.resetControls()

        brightnessFinal = 0
        saturationFinal = 1.0F
        contrastFinal = 1.0F
    }

    internal var originalImage: Bitmap? = null
    internal lateinit var filteredImage: Bitmap
    internal lateinit var finalImage: Bitmap

    internal lateinit var filterListFragment: FilterListFragment
    internal lateinit var editImageFragment: EditImageFragment
    internal lateinit var brushFragment: BrushFragment
    internal lateinit var emojiFragment: EmojiFragment

    internal var brightnessFinal = 0
    internal var saturationFinal = 1.0F
    internal var contrastFinal = 1.0F


    object Main {
        val IMAGE_NAME = "flash.jpg"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set toolbar
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Photo Filters"

        photoEditor = PhotoEditor.Builder(this@MainActivity, image_preview)
            .setPinchTextScalable(true)
            .setDefaultEmojiTypeface(Typeface.createFromAsset(assets, "emojione-android.ttf"))
            .build()

        loadImage()

        // Init
        filterListFragment = FilterListFragment.getInstance()
        editImageFragment = EditImageFragment.getInstance()
        brushFragment = BrushFragment.getInstance()
        emojiFragment = EmojiFragment.getInstance()

        btn_filters.setOnClickListener {
            if (filterListFragment != null) {
                filterListFragment.setListener(this@MainActivity)
                filterListFragment.show(supportFragmentManager, filterListFragment.tag)
            }
        }

        btn_edit.setOnClickListener {
            if (editImageFragment != null) {
                editImageFragment.setListener(this@MainActivity)
                editImageFragment.show(supportFragmentManager, editImageFragment.tag)
            }
        }

        btn_brush.setOnClickListener {
            if (brushFragment != null) {

                photoEditor.setBrushDrawingMode(true)

                brushFragment.setListener(this@MainActivity)
                brushFragment.show(supportFragmentManager, brushFragment.tag)
            }
        }

        btn_emoji.setOnClickListener {
            if (emojiFragment != null) {

                emojiFragment.setListener(this@MainActivity)
                emojiFragment.show(supportFragmentManager, emojiFragment.tag)
            }
        }
    }

    private fun setupViewPager(viewPager: NonSwipeViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // add filter list fragment
        filterListFragment = FilterListFragment()
        filterListFragment.setListener(this)

        // add edit image fragment
        editImageFragment = EditImageFragment()
        editImageFragment.setListener(this)

        adapter.addFragment(filterListFragment, "FILTERS")
        adapter.addFragment(editImageFragment, "EDIT")

        viewPager.adapter = adapter

    }


    private fun loadImage() {
        originalImage = BitMapUtils.getBitmapFromAssets(this@MainActivity, Main.IMAGE_NAME, width = 300, height = 300)
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888,true)
        finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888,true)
        image_preview.source.setImageBitmap(originalImage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_open) {

            openImageFromGallery()
            return true
        } else if (id == R.id.action_save) {
            saveImageToGallery()
        }

        return true
    }

    private fun saveImageToGallery() {
        Dexter.withActivity(this@MainActivity)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    if (report!!.areAllPermissionsGranted()) {

                        photoEditor.saveAsBitmap(object: OnSaveBitmap{
                            override fun onFailure(e: Exception?) {

                                val snackBar = Snackbar.make(coordinator,e!!.message.toString(), Snackbar.LENGTH_LONG)
                                snackBar.show()
                            }

                            override fun onBitmapReady(saveBitmap: Bitmap?) {

                                val path = BitMapUtils.insertImage(contentResolver = contentResolver,
                                    source = saveBitmap,
                                    title = System.currentTimeMillis().toString() +  "_profile.jpg",
                                    description = "")

                                if (!TextUtils.isEmpty(path)) {

                                    val snackBar = Snackbar.make(coordinator, "Image saved to gallery", Snackbar.LENGTH_LONG)
                                        .setAction("OPEN") {
                                            openImage(path)
                                        }
                                    snackBar.show()
                                } else {

                                    val snackBar = Snackbar.make(coordinator,"Unable to save image", Snackbar.LENGTH_LONG)
                                    snackBar.show()
                                }
                            }

                        })


                    }
                    else {
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {

                    token!!.continuePermissionRequest()
                }

            }).check()
    }

    private fun openImage(path: String?) {

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(path), "image/*")
        startActivity(intent)
    }

    private fun openImageFromGallery() {
    // We will use Dexter to request runtime permission and process
        val withListener = Dexter.withActivity(this@MainActivity)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {

                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, SELECT_GALLERY_PERMISSION)
                    } else {
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {
                    token!!.continuePermissionRequest()

                }

            }).check()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_GALLERY_PERMISSION) {

            val bitmap = BitMapUtils.getBitmapFromGallery(this@MainActivity, data!!.data!!, width = 800, height = 800)

            // clear bitmap memory
            originalImage!!.recycle()
            finalImage.recycle()
            filteredImage.recycle()

            originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
            finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)

            bitmap.recycle()

            // render select image thumb
             filterListFragment.displayImage(bitmap = bitmap)
        }
    }
}
