package com.vaibhavrawat.everythingqr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.client.android.BuildConfig
import java.io.File
import java.io.FileOutputStream

class GenerateQRCodeForCustom : AppCompatActivity() {

    private lateinit var editTextCustomInput: EditText
    private lateinit var imageViewQRCode: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qrcode_for_custom)

        editTextCustomInput = findViewById(R.id.editTextCustom)
        imageViewQRCode = findViewById(R.id.imageView)
        val buttonGenerateQR = findViewById<Button>(R.id.buttonGenerateQR)
        val buttonShare = findViewById<Button>(R.id.buttonShare)

        buttonGenerateQR.setOnClickListener {
            generateQRCode()
        }

        buttonShare.setOnClickListener {
            shareQRCode()
        }
    }

    private fun generateQRCode() {
        val text = editTextCustomInput.text.toString()
        if (text.isNotEmpty()) {
            val bitmap = generateQRCodeBitmap(text)
            imageViewQRCode.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Please enter any text to generate QR code", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateQRCodeBitmap(text: String): Bitmap? {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
                }
            }
            return bmp
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    private fun shareQRCode() {
        val drawable = imageViewQRCode.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            try {
                // Save bitmap to cache directory
                val cachePath = File(applicationContext.cacheDir, "images")
                cachePath.mkdirs() // Create the cache directory if it doesn't exist
                val file = File(cachePath, "qr_code.png")
                val stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()

                // Log file path
                println("File path: ${file.absolutePath}")

                // Share bitmap
                val uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", file)

                // Log URI
                println("URI: $uri")

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(intent, "Share QR Code via"))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to share QR code", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "QR code image not available", Toast.LENGTH_SHORT).show()
        }
    }

}
