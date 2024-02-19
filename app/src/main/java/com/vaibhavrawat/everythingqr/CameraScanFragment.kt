package com.vaibhavrawat.everythingqr

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.text))
                        startActivity(intent)


                    } else {
                        // Show the content of the QR code in a pop-up
                        showQRContentPopup(result.text)
                        isScanningEnabled = false
                    }
                }
            }

            @SuppressLint("ServiceCast")
            private fun showQRContentPopup(content: String) {
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
