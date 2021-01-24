package com.example.googlelensclone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_camera.previewView
import kotlinx.android.synthetic.main.activity_lens.*
import java.lang.Exception

abstract class BaseLensActivity:AppCompatActivity() {

    companion object{
        @JvmStatic
        val CAMERA_PERMISSION_CODE = 422
    }

    abstract val imageAnalyzer:ImageAnalysis.Analyzer
    protected lateinit var imageAnalysis: ImageAnalysis

    private fun askCameraPermission(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_PERMISSION_CODE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lens)
        askCameraPermission()
        btnStartScan.setOnClickListener {
            startScanner()
        }
    }

    abstract fun startScanner()

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
                Runnable {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                            .build()
                            .also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                    imageAnalysis = ImageAnalysis.Builder()
                            .build()

                    val imageCapture = ImageCapture.Builder().build()

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    try{
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                    }catch (err: Exception){
                        Log.e("CAM", "Error - ${err.message}")
                    }
                }
                ,
                ContextCompat.getMainExecutor(this)
        )

    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {

        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startCamera()
            }else{
                AlertDialog.Builder(this)
                        .setTitle("Permission Error")
                        .setMessage("Camera Permission not granted")
                        .setCancelable(false)
                        .setPositiveButton("OK"){ _, _ ->
                            finish()
                        }
                        .show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}