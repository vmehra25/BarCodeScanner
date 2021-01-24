package com.example.googlelensclone.barcode

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.googlelensclone.BaseLensActivity
import com.example.googlelensclone.R
import kotlinx.android.synthetic.main.activity_lens.*

class BarCodeActivity : BaseLensActivity() {

    override val imageAnalyzer = BarCodeAnalyzer(
        changeActivity = { url ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    )

    override fun startScanner() {
        scanBarcode()
    }

    private fun scanBarcode() {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )
    }
}