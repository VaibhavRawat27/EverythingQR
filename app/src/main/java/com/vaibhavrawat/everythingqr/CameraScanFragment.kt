package com.vaibhavrawat.everythingqr

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.vaibhavrawat.everythingqr.databinding.FragmentCameraScanBinding
import java.net.URL

class CameraScanFragment : Fragment() {
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var binding: FragmentCameraScanBinding
    private var isScanningEnabled = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraScanBinding.inflate(inflater, container, false)
        barcodeView = binding.barcodeScanner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the decoder factory to scan both QR codes and barcodes
        barcodeView.decoderFactory = DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39))

        // Set result callback
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {

                if (!isScanningEnabled) {
                    return // Stop scanning if it's disabled
                }
                // Check if the result contains data
                if (result.text != null) {
                    // Check if the data is a valid URL
                    if (result.text.startsWith("http://") || result.text.startsWith("https://")) {
                        // Open the URL in a web browser
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.text))
//                        startActivity(intent)

                        showURLConfirmationDialog(result.text)
                        isScanningEnabled = false

                    } else {
                        // Show the content of the QR code in a pop-up
                        showQRContentPopup(result.text)
                        isScanningEnabled = false
                    }
                }
            }

            private fun showURLConfirmationDialog(url: String) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setMessage("This QR code is redirecting to:\n$url\nDo you want to continue?")
                dialogBuilder.setCancelable(false)
                dialogBuilder.setPositiveButton("Open") { dialog, _ ->
                    dialog.dismiss()
                    // Open the URL in a web browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
                dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    isScanningEnabled = true
                }
                val dialog = dialogBuilder.create()
                dialog.show()
            }

            @SuppressLint("ServiceCast")
            private fun showQRContentPopup(content: String) {

//                if (content.endsWith(".png") || content.endsWith(".jpg") || content.endsWith(".jpeg") || content.endsWith(".gif")) {
//                    // Show the image in a dialog
//                    showImageDialog(content)
//                    return
//                }
                // Create a dialog or alert dialog to show the content
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setMessage(content)
                dialogBuilder.setCancelable(false)
                dialogBuilder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    isScanningEnabled = true
                }
//                dialogBuilder.setNegativeButton("Select All") { dialog, _ ->
//                    dialog.dismiss()
//                    isScanningEnabled = true
//                }
                dialogBuilder.setNeutralButton("Copy All") { _, _ ->
                    // Copy all the text to clipboard
                    val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("QR Content", content)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(requireContext(), "Content copied to clipboard", Toast.LENGTH_SHORT).show()
                }


                val dialog = dialogBuilder.create()

                dialog.setOnShowListener {
                    dialog.findViewById<TextView>(android.R.id.message)?.setTextIsSelectable(true)
                }

                dialog.show()
            }

//            private fun showImageDialog(imageUrl: String) {
//                // Load the image from the URL and display it in a dialog
//                val dialogBuilder = AlertDialog.Builder(requireContext())
//                val dialogView = layoutInflater.inflate(R.layout.dialog_image, null)
//                val imageView = dialogView.findViewById<ImageView>(R.id.imageView)
//                dialogBuilder.setView(dialogView)
//                dialogBuilder.setCancelable(true)
//
//                // Load image using Glide, Picasso, or any other image loading library
//                // Here's an example using BitmapFactory to load the image
//                val bitmap = BitmapFactory.decodeStream(URL(imageUrl).openConnection().getInputStream())
//                imageView.setImageBitmap(bitmap)
//
//                val dialog = dialogBuilder.create()
//                dialog.show()
//            }


            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}
