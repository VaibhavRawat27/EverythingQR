package com.vaibhavrawat.everythingqr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class GenerateQRCodeForImage : AppCompatActivity() {

    private lateinit var mobileNumberEditText: EditText
    private lateinit var selectedImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qrcode_for_image)

        mobileNumberEditText = findViewById(R.id.mobileNumberEditText)
        selectedImageView = findViewById(R.id.selectedImageView)
    }

    fun generateQRCodeForImage(view: android.view.View) {
        val mobileNumber = mobileNumberEditText.text.toString().trim()

        if (mobileNumber.isNotEmpty()) {
            val encoder = BarcodeEncoder()
            try {
                val barcodeBitmap = encoder.encodeBitmap(mobileNumber, BarcodeFormat.QR_CODE, 900, 900)
                selectedImageView.setImageBitmap(barcodeBitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        } else {
            // Handle case where mobile number is empty
        }
    }
}
