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
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.client.android.BuildConfig
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream
import java.util.EnumMap

class QRCodeGeneratorActivity : AppCompatActivity() {

    private lateinit var editTextLink: EditText
    private lateinit var buttonGenerateQR: Button
    private lateinit var buttonShare: Button
    private lateinit var imageViewQRCode: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_generator)

        // Initialize views
        editTextLink = findViewById(R.id.editTextLink)
        buttonGenerateQR = findViewById(R.id.buttonGenerateQR)
        buttonShare = findViewById(R.id.ShareButton)
        imageViewQRCode = findViewById(R.id.imageView)

        // Handle QR code generation button click
        buttonGenerateQR.setOnClickListener {
            generateQRCode()
        }

        // Handle QR code sharing button click
        buttonShare.setOnClickListener {
            shareQRCode()
        }
    }

    private fun generateQRCode() {
        val link = editTextLink.text.toString().trim()
        if (link.isEmpty()) {
            Toast.makeText(this, "Please enter a link", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the link starts with "http://" or "https://", if not, prepend it
        val formattedLink = if (!link.startsWith("http://") && !link.startsWith("https://")) {
            "http://$link"
        } else {
            link
        }

        // Generate QR Code bitmap
        val qrCodeBitmap = generateQRCodeBitmap(formattedLink)

        // Display QR code in ImageView
        imageViewQRCode.setImageBitmap(qrCodeBitmap)
    }

    private fun generateQRCodeBitmap(content: String): Bitmap? {
        try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 500, 500, hints)

            val bmp = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565)
            for (x in 0 until 500) {
                for (y in 0 until 500) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) -0x1000000 else -0x1)
                }
            }
            return bmp
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show()
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
