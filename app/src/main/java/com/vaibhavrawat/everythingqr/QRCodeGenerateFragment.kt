package com.vaibhavrawat.everythingqr

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vaibhavrawat.everythingqr.databinding.FragmentQRCodeGenerateBinding

class QRCodeGenerateFragment : Fragment() {

    private var _binding: FragmentQRCodeGenerateBinding? = null
    private val binding get() = _binding!!
//
//    private lateinit var adsManager: AdsManager
//
//    private lateinit var showInterstitialAd: ShowInterstitialAd

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

//        showInterstitialAd = ShowInterstitialAd(requireContext())
//        adsManager = AdsManager(requireContext())

//        binding.buttonAbout.setOnClickListener {
//            adsManager.showInterstitialAd(this)
//            showAboutDialog()
//        }

        binding.buttonAbout.setOnClickListener {
//            showInterstitialAd.displayInterstitialAd()
            showAboutDialog()
        }

        binding.buttonPolicy.setOnClickListener {
            showPolicyDialog()
//            if (adsManager.isBannerLoaded) {
//                showPolicyDialog()
//            } else {
//                Toast.makeText(requireContext(), "Banner ad is not loaded yet!", Toast.LENGTH_SHORT).show()
//            }
        }

        // Load banner ad
//        adsManager.loadBannerAd(this, view.findViewById(R.id.bannerAdContainer))

        // Other button click listeners...
        binding.buttonGenerateQRLink.setOnClickListener {
            startActivity(Intent(requireContext(), QRCodeGeneratorActivity::class.java))
        }

        binding.buttonGenerateQRText.setOnClickListener {
            startActivity(Intent(requireContext(), GenerateQRCodeForText::class.java))
        }

        binding.buttonGenerateQRContactShare.setOnClickListener {
            startActivity(Intent(requireContext(), GenerateQRCodeForContactShare::class.java))
        }

        binding.buttonGenerateQRCustom.setOnClickListener {
            startActivity(Intent(requireContext(), GenerateQRCodeForCustom::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAboutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.about_layout, null)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)
        dialog.show()
    }

    private fun showPolicyDialog() {
        val dialogView = layoutInflater.inflate(R.layout.policy_layout, null)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)
        dialog.show()
    }
}
