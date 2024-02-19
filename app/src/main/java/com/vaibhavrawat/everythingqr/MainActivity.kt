package com.vaibhavrawat.everythingqr

import android.Manifest
import android.R
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vaibhavrawat.everythingqr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PERMISSION_REQUEST_CODE = 100
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color)
//        window.statusBarColor = statusBarColor

        animateText("Everything QR")
        // Request camera permission
        requestPermissions()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            PERMISSIONS,
            PERMISSION_REQUEST_CODE
        )
    }

    private fun hasAllPermissionsGranted(): Boolean {
        return PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted, proceed with navigating to QRDashboard activity
                navigateToQRDashboard()
            } else {
                // Permissions denied, inform the user
                Toast.makeText(
                    this,
                    "Camera permission is required to generate QR code",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToQRDashboard() {
        Handler().postDelayed({
            val intent = Intent(this, QRDashboard::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2000 milliseconds = 2 seconds
    }


    private fun animateText(text: String) {
        val spannableString = SpannableString(text)
        val animator = ValueAnimator.ofInt(0, text.length)
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            spannableString.setSpan(
                TextAppearanceSpan(null, 0, 150, ColorStateList.valueOf(Color.BLUE), null),
                0, value, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textView.text = spannableString
        }
        animator.duration = 2000
        animator.start()
    }


}
