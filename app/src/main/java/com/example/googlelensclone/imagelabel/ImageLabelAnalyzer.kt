package com.example.googlelensclone.imagelabel

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageLabelAnalyzer: ImageAnalysis.Analyzer {

    val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("IMAGE_LABEL_ANALYZER", "Image Analyzed")
        val mediaImage = imageProxy.image
        if(mediaImage != null){
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            labeler.process(inputImage)
                    .addOnSuccessListener { image_labels ->
                        Log.d("IMAGE_LABEL_ANALYZER", "Image labels = ${image_labels.size}")
                        image_labels.forEach{ label ->
                            Log.d("IMAGE_LABEL_ANALYZER", """
                                Confidence = ${label.confidence}
                                Index = ${label.index}
                                Text = ${label.text}
                            """.trimIndent())
                        }
                    }.addOnFailureListener { ex ->
                        Log.d("IMAGE_LABEL_ANALYZER", "Exception - ${ex.message}")
                    }.addOnCompleteListener {
                        imageProxy.close()
                    }
        }else{
            Log.d("IMAGE_LABEL_ANALYZER", "Null image")
            imageProxy.close()
        }

    }
}