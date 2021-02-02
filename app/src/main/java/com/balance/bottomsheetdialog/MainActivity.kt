package com.balance.bottomsheetdialog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MainActivity : AppCompatActivity() {

    //#1 Defining a BottomSheetBehavior instance
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    val PICK_FROM_GALLERY = 34

    private lateinit var recyclerView: RecyclerView
    private lateinit var mContext : Context

    private lateinit var imageAdapter: ImageAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this


        //#2 Initializing the BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet))

        recyclerView = findViewById(R.id.recycler_view)
        imageAdapter = ImageAdapter()

        recyclerView.apply {
            layoutManager = GridLayoutManager(mContext, 4)
            adapter = imageAdapter
        }

        //#3 Listening to State Changes of BottomSheet
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                findViewById<Button>(R.id.buttonBottomSheetPersistent).text = when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> "Close Persistent Bottom Sheet"
                    BottomSheetBehavior.STATE_COLLAPSED -> "Open Persistent Bottom Sheet"
                    else -> "Persistent Bottom Sheet"
                }
            }
        })

        //#4 Changing the BottomSheet State on ButtonClick
        findViewById<Button>(R.id.buttonBottomSheetPersistent).setOnClickListener {
            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
        }





        if (hasPermission())
            getAllImages()



    }

    fun hasPermission() : Boolean{

        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PICK_FROM_GALLERY
            )
            false
        } else true
    }


    var isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    private fun getAllImages(){
        val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
        //val orderBy = MediaStore.Images.Media.DEFAULT_SORT_ORDER
        val orderBy = MediaStore.Images.Media._ID + " DESC"
        //Stores all the images from the gallery in Cursor
        //Stores all the images from the gallery in Cursor
        val cursor: Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            columns,
            null,
            null,
            orderBy
        )
        //Total number of images
        //Total number of images

        cursor?.let {
            val count: Int = it.count

            //Create an array to store path to all the images
            val arrPath = arrayOfNulls<String>(count)

            for (i in 0 until count) {
                it.moveToPosition(i)
                val dataColumnIndex: Int = it.getColumnIndex(MediaStore.Images.Media.DATA)
                //Store the path of the image
                arrPath[i] = it.getString(dataColumnIndex)
            }
            if (arrPath.isNotEmpty()){
                imageAdapter.setData(arrPath.toMutableList())
            }


        }

        cursor?.close()
    }


}