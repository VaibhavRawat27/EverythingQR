package com.vaibhavrawat.everythingqr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.client.android.BuildConfig
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream

class GenerateQRCodeForContactShare : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qrcode_for_contact_share)

        val buttonShare = findViewById<Button>(R.id.ShareButton)
        val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)
        val buttonGenerateQRCode = findViewById<Button>(R.id.buttonGenerateQR)
        val imageViewQRCode = findViewById<ImageView>(R.id.imageView)

        buttonGenerateQRCode.setOnClickListener {
            val phoneNumber = editTextPhoneNumber.text.toString()
            val email = editTextEmail.text.toString()
            val address = editTextAddress.text.toString()

            val contactInfo = "PHONE:$phoneNumber\nEMAIL:$email\nADDRESS:$address"
            val bitmap = generateQRCode(contactInfo)

            imageViewQRCode.setImageBitmap(bitmap)
        }

        buttonShare.setOnClickListener {
            shareQRCode()
        }
    }

    private fun generateQRCode(text: String): Bitmap? {
        val width = 500
        val height = 500
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height,
                null
            )
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(
                    x, y,
                    if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                )
            }
        }
        return bmp
    }

    private fun shareQRCode() {
        val imageViewQRCode = findViewById<ImageView>(R.id.imageView)
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
