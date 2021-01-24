package com.example.googlelensclone.facedetect

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectAnalyzer: ImageAnalysis.Analyzer {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
    )

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("FACE_DETECT_ANALYZER", "Image Analyzed")
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    Log.d("FACE_DETECT_ANALYZER", "Faces = ${faces.size}")
                    faces.forEach { face ->
                        Log.d("FACE_DETECT_ANALYZER", """
                            Smile = ${face.smilingProbability},
                            Left Eye = ${face.leftEyeOpenProbability}
                            Right Eye = ${face.rightEyeOpenProbability}
                    """.trimIndent())
                    }
                }.addOnFailureListener { ex ->
                    Log.d("FACE_DETECT_ANALYZER", "Exception - ${ex.message}")
                }.addOnCompleteListener {
                    imageProxy.close()
                }

        }else {
            Log.d("FACE_DETECT_ANALYZER", "Null imageProxy")
            imageProxy.close()
        }
    }
}