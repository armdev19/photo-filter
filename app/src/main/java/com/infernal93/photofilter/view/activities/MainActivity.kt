package com.infernal93.photofilter.view.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.infernal93.photofilter.*
import com.infernal93.photofilter.view.adapter.ViewPagerAdapter
import com.infernal93.photofilter.view.interfaces.*
import com.infernal93.photofilter.utils.BitMapUtils
import com.infernal93.photofilter.utils.NonSwipeViewPager
import com.infernal93.photofilter.view.fragments.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yalantis.ucrop.UCrop
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity(), FilterListFragmentListener, EditImageFragmentListener,
    BrushFragmentListener, EmojiFragmentListener, AddTextFragmentListener,
    AddFrameFragmentListener {

    override fun onFrameSelected(frame: Int) {
        val bitmap = BitmapFactory.decodeResource(resources, frame)
        photoEditor.addImage(bitmap)
    }

    override fun onAddTextListener(typeFace: Typeface, text: String, color: Int) {
        photoEditor.addText(typeFace, text, color)
    }

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
    val PICTURE_IMAGE_GALLERY_PERMISSION = 1001

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

    override fun onContrastChanged(contrast: Float) {
        contrastFinal = contrast
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrast))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onEditStarted() {}

    override fun onEditCompleted() {
        val bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        finalImage = myFilter.processFilter(bitmap)
    }

    override fun onFilterSelected(filter: Filter) {
        //resetControls()
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

    private var originalImage: Bitmap? = null
    private lateinit var filteredImage: Bitmap
    private lateinit var finalImage: Bitmap

    private lateinit var filterListFragment: FilterListFragment
    private lateinit var editImageFragment: EditImageFragment
    private lateinit var brushFragment: BrushFragment
    private lateinit var emojiFragment: EmojiFragment
    private lateinit var addTextFragment: AddTextFragment
    private lateinit var frameFragment: FrameFragment

    private var brightnessFinal = 0
    private var saturationFinal = 1.0F
    private var contrastFinal = 1.0F

    private var imageSelectedUri: Uri? = null

    internal var imageUri: Uri? = null
    internal val CAMERA_REQUEST: Int = 9999

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
        filterListFragment =
            FilterListFragment.getInstance(
                bitmapSave = null
            )
        editImageFragment =
            EditImageFragment.getInstance()
        brushFragment =
            BrushFragment.getInstance()
        emojiFragment =
            EmojiFragment.getInstance()
        addTextFragment =
            AddTextFragment.getInstance()
        frameFragment =
            FrameFragment.getInstance()

        btn_filters.setOnClickListener {
            if (filterListFragment != null) {
                filterListFragment.show(supportFragmentManager, filterListFragment.tag)
            } else {
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

        btn_add_text.setOnClickListener {
            if (addTextFragment != null) {
                addTextFragment.setListener(this@MainActivity)
                addTextFragment.show(supportFragmentManager, addTextFragment.tag)
            }
        }

        btn_add_image.setOnClickListener {
            addImageToPicture()
        }

        btn_add_frame.setOnClickListener {
            if (frameFragment != null) {
                frameFragment.setListener(this@MainActivity)
                frameFragment.show(supportFragmentManager, frameFragment.tag)
            }
        }

        btn_crop_image.setOnClickListener {
            startCrop(imageSelectedUri)
        }
    }

    private fun startCrop(uri: Uri?) {
        val destinationFileName = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
        val uCrop = UCrop.of(uri!!, Uri.fromFile(File(cacheDir, destinationFileName)))
        uCrop.start(this@MainActivity)
    }

    private fun addImageToPicture() {
        Dexter.withActivity(this@MainActivity)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    if (report!!.areAllPermissionsGranted()) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICTURE_IMAGE_GALLERY_PERMISSION)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {

                    Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }).check()
    }

    private fun setupViewPager(viewPager: NonSwipeViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // add filter list fragment
        filterListFragment =
            FilterListFragment()
        filterListFragment.setListener(this)

        // add edit image fragment
        editImageFragment =
            EditImageFragment()
        editImageFragment.setListener(this)

        adapter.addFragment(filterListFragment, "FILTERS")
        adapter.addFragment(editImageFragment, "EDIT")
        viewPager.adapter = adapter
    }

    private fun loadImage() {
        originalImage = BitMapUtils.getBitmapFromAssets(this@MainActivity,
            Main.IMAGE_NAME, width = 300, height = 300)
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

        when (id) {
            R.id.action_open -> {
                openImageFromGallery()
                return true
            }
            R.id.action_save -> saveImageToGallery()
            R.id.action_camera -> openCamera()
        }
        return true
    }

    private fun openCamera() {
        Dexter.withActivity(this@MainActivity)
            .withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val values = ContentValues()
                        values.put(MediaStore.Images.Media.TITLE, "New Picture")
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
                        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                        // Get best quality photo
                        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST)
                    } else {
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {
                    token!!.continuePermissionRequest()
                }
            }).check()
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
                                    // Fix error restore bitmap to default
                                    image_preview.source.setImageBitmap(saveBitmap)
                                } else {
                                    val snackBar = Snackbar.make(coordinator,"Unable to save image", Snackbar.LENGTH_LONG)
                                    snackBar.show()
                                }
                            }

                        })
                    } else {
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
        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                SELECT_GALLERY_PERMISSION -> {
                    val bitmap = BitMapUtils.getBitmapFromGallery(this@MainActivity, data!!.data!!, width = 800, height = 800)
                    imageSelectedUri = data.data!!
                    // clear bitmap memory
                    originalImage!!.recycle()
                    finalImage.recycle()
                    filteredImage.recycle()

                    originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                    finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                    image_preview.source.setImageBitmap(originalImage)
                    bitmap.recycle()
                    // Fix crush when the photo selection
                    filterListFragment =
                        FilterListFragment.getInstance(
                            originalImage!!
                        )
                    filterListFragment.setListener(this)

                }
                CAMERA_REQUEST -> {
                    val bitmap = BitMapUtils.getBitmapFromGallery(this@MainActivity, imageUri!!, width = 800, height = 800)
                    imageSelectedUri = imageUri!!
                    // clear bitmap memory
                    originalImage!!.recycle()
                    finalImage.recycle()
                    filteredImage.recycle()

                    originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                    finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                    image_preview.source.setImageBitmap(originalImage)
                    bitmap.recycle()
                    // Fix crush when the photo selection
                    filterListFragment =
                        FilterListFragment.getInstance(
                            originalImage!!
                        )
                    filterListFragment.setListener(this)

                }
                PICTURE_IMAGE_GALLERY_PERMISSION -> {

                    val bitmap = BitMapUtils.getBitmapFromGallery(this@MainActivity, data!!.data!!, width = 200, height = 200)
                    photoEditor.addImage(bitmap)
                }
                UCrop.REQUEST_CROP -> handleCropResult(data)
            }
        } else if (resultCode == UCrop.RESULT_ERROR)
                    handleCropError(data)
    }

    private fun handleCropError(data: Intent?) {
        val cropError = UCrop.getError(data!!)
        if (cropError != null) {
            Toast.makeText(this@MainActivity, "" + cropError.message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "Unexpected Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCropResult(data: Intent?) {
        val resultUri = UCrop.getOutput(data!!)
        if (resultUri != null) {
            image_preview.source.setImageURI(resultUri)
        } else {
            Toast.makeText(this@MainActivity, "Cannot retrieve crop image", Toast.LENGTH_SHORT).show()
        }
    }
}