package com.vaibhavrawat.everythingqr

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.unity3d.ads.UnityAds
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

class AdsManager(private val context: Context) : BannerView.IListener {

    private val unityGameID = "5557841"
    private val testMode = false
    private val adUnitId = "Interstitial_Android" // Change this to your interstitial ad unit ID

    private lateinit var bannerView: BannerView
    var isBannerLoaded = false

    init {
        UnityAds.initialize(context, unityGameID, testMode)
    }

    fun loadBannerAd(fragment: Fragment, container: ViewGroup) {
        bannerView = BannerView(fragment.requireActivity(), "BannerAdsA", UnityBannerSize.getDynamicSize(fragment.requireContext()))
        bannerView.listener = this
        bannerView.load()
        container.addView(bannerView)
    }

    fun showInterstitialAd(fragment: Fragment) {
        if (UnityAds.isInitialized) {
            UnityAds.load(adUnitId)
            UnityAds.show(fragment.requireActivity(), adUnitId)
        } else {
            Log.d("UnityAds", "Unity Ads is not initialized.")
            // Handle the case where Unity Ads is not initialized
        }
    }

    override fun onBannerLoaded(bannerView: BannerView?) {
        Log.d("BannerView", "Banner loaded successfully")
        Toast.makeText(context, "Banner loaded successfully!", Toast.LENGTH_SHORT).show()
        isBannerLoaded = true
    }

    override fun onBannerShown(bannerAdView: BannerView?) {
        // Not implemented
    }

    override fun onBannerClick(bannerView: BannerView?) {
        Log.d("BannerView", "Banner clicked")
        Toast.makeText(context, "Banner clicked!", Toast.LENGTH_SHORT).show()
    }

    override fun onBannerFailedToLoad(bannerAdView: BannerView?, errorInfo: BannerErrorInfo?) {
        Log.e("BannerView", "Banner failed to load: ${errorInfo?.errorMessage}")
        Toast.makeText(context, "${errorInfo?.errorMessage}", Toast.LENGTH_SHORT).show()
    }

    override fun onBannerLeftApplication(bannerView: BannerView?) {
        Log.d("BannerView", "Banner left application")
    }
}
