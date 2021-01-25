package com.example.googlelensclone.textrecognition

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition

class TextAnalyzer:ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient()

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("TEXT_ANALYZER", "Image Analyzed")
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            recognizer.process(inputImage)
                .addOnSuccessListener { text ->
                    text.textBlocks.forEach{ block ->
                        Log.d("TEXT_ANALYZER_RESULT", """
                            LINES = ${block.lines.joinToString("\n"){ it.text } }                            
                        """.trimIndent())
                    }
                }.addOnFailureListener { ex ->
                    Log.d("TEXT_ANALYZER", "Exception - ${ex.message}")
                }.addOnCompleteListener {
                    imageProxy.close()
                }

        }else {
            Log.d("TEXT_ANALYZER", "Null imageProxy")
            imageProxy.close()
        }
    }
}