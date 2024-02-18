package com.vaibhavrawat.everythingqr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.vaibhavrawat.everythingqr.databinding.FragmentQRCodeGenerateBinding
import org.json.JSONObject
import java.util.EnumMap

class QRCodeGenerateFragment : Fragment() {
    private var _binding: FragmentQRCodeGenerateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQRCodeGenerateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGenerateQRLink.setOnClickListener {
            val intent = Intent(requireContext(), QRCodeGeneratorActivity::class.java)
            startActivity(intent)
        }
        binding.buttonGenerateQRText.setOnClickListener {
            val intent = Intent(requireContext(), GenerateQRCodeForText::class.java)
            startActivity(intent)
        }
        binding.buttonGenerateQRContactShare.setOnClickListener {
            val intent = Intent(requireContext(), GenerateQRCodeForContactShare::class.java)
            startActivity(intent)
        }
        binding.buttonGenerateQRAudio.setOnClickListener {
            val intent = Intent(requireContext(), GenerateQRCodeForAudio::class.java)
            startActivity(intent)
        }
        binding.buttonGenerateQRCustom.setOnClickListener {
            val intent = Intent(requireContext(), GenerateQRCodeForCustom::class.java)
            startActivity(intent)
        }
        binding.buttonGenerateQRImage.setOnClickListener {
            val intent = Intent(requireContext(), GenerateQRCodeForImage::class.java)
            startActivity(intent)
        }
    }
}
