package com.example.googlelensclone

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.example.googlelensclone.barcode.BarCodeActivity
import com.example.googlelensclone.facedetect.FaceDetectActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        @JvmStatic val REQUEST_IMAGE_CAPTURE = 1
        @JvmStatic val EXTRA_DATA = "data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTakePhoto.setOnClickListener {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE)
            }catch (e: Exception){
                Log.d("ERR_MSG", "Error $e")
            }
        }

        btnScanBarCode.setOnClickListener {
            val intent = Intent(this, BarCodeActivity::class.java)
            startActivity(intent)
        }

        btnFaceDetect.setOnClickListener {
            val intent = Intent(this, FaceDetectActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            val bitmap = data?.extras?.get(EXTRA_DATA) as Bitmap
            imgThumb.setImageBitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}