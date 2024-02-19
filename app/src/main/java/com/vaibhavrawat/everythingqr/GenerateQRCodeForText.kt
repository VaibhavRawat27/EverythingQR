package com.vaibhavrawat.everythingqr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.client.android.BuildConfig
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.vaibhavrawat.everythingqr.databinding.ActivityGenerateQrcodeForTextBinding
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.EnumMap

class GenerateQRCodeForText : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateQrcodeForTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateQrcodeForTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGenerateQR.setOnClickListener {
            generateQRCode()
        }

        binding.buttonShare.setOnClickListener {
            shareQRCode()
        }
    }

    private fun generateQRCode() {
        val name = binding.editTextName.text.toString()
        val phoneNumber = binding.editTextPhoneNumber.text.toString()
        val email = binding.editTextEmail.text.toString()
        val bio = binding.editTextBio.text.toString()
        val websiteUrl = binding.editTextWebsite.text.toString()
        val imageUrl = binding.editTextImageUrl.text.toString()

        // Generate profile data
        val profileData = createProfileData(
            name,
            phoneNumber,
            email,
            bio,
            websiteUrl,
            imageUrl
        )

        // Generate QR Code bitmap
        val qrCodeBitmap = generateQRCode(profileData, 500, 500)

        // Display QR code in ImageView
        binding.imageView.setImageBitmap(qrCodeBitmap)
    }

    private fun createProfileData(
        name: String,
        phoneNumber: String,
        email: String,
        bio: String,
        websiteUrl: String,
        imageUrl: String
    ): String {
        val profileJson = JSONObject()
        profileJson.put("name", name)
        profileJson.put("phone_number", phoneNumber)
        profileJson.put("email", email)
        profileJson.put("bio", bio)
        profileJson.put("website_url", websiteUrl)
        profileJson.put("image_url", imageUrl)
        return profileJson.toString()
    }

    private fun generateQRCode(content: String, width: Int, height: Int): Bitmap? {
        try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
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
        val drawable = binding.imageView.drawable
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
