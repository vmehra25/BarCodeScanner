package com.example.googlelensclone.barcode

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class BarCodeAnalyzer(
        val changeActivity:(url: String) -> Unit
): ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("BARCODE_ANALYZER", "Image Analysed")
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(inputImage)
                .addOnSuccessListener { codes ->
                    codes.forEach { barcode ->
                        Log.d("BARCODE_ANALYZER", """
                            Format = ${barcode.format}
                            Value = ${barcode.rawValue}
                        """.trimIndent())
                        changeActivity(barcode.rawValue!!)
                    }
                }.addOnFailureListener { exc ->
                    Log.d("BARCODE_ANALYZER", """
                        Failure exception - $exc
                    """.trimIndent())
                    imageProxy.close()

                }.addOnCanceledListener {
                        Log.d("BARCODE_ANALYZER", """
                        Cancelled
                    """.trimIndent())
                        imageProxy.close()
                }

        }else {
            Log.d("BARCODE_ANALYZER", "Null imageProxy")
            imageProxy.close()
        }
    }


}