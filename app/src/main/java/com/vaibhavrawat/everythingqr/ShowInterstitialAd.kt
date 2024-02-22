package com.vaibhavrawat.everythingqr

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions

class ShowInterstitialAd(requireContext: Context) : AppCompatActivity(), IUnityAdsInitializationListener {

    private val unityGameID = "5557841"
    private val testMode = false
    private val adUnitId = "Interstitial_Android"

    private val loadListener = object : IUnityAdsLoadListener {
        override fun onUnityAdsAdLoaded(placementId: String) {
            UnityAds.show(this@ShowInterstitialAd, adUnitId, UnityAdsShowOptions(), showListener)
        }

        override fun onUnityAdsFailedToLoad(placementId: String, error: UnityAds.UnityAdsLoadError, message: String) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for $placementId with error: [$error] $message")
        }
    }

    private val showListener = object : IUnityAdsShowListener {
        override fun onUnityAdsShowFailure(placementId: String, error: UnityAds.UnityAdsShowError, message: String) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for $placementId with error: [$error] $message")
        }

        override fun onUnityAdsShowStart(placementId: String) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: $placementId")
        }

        override fun onUnityAdsShowClick(placementId: String) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: $placementId")
        }

        override fun onUnityAdsShowComplete(placementId: String, state: UnityAds.UnityAdsShowCompletionState) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: $placementId")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UnityAds.initialize(applicationContext, unityGameID, testMode, this)
    }

    override fun onInitializationComplete() {
        displayInterstitialAd()
    }

    override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError, message: String) {
        Log.e("UnityAdsExample", "Unity Ads initialization failed with error: [$error] $message")
    }

    fun displayInterstitialAd() {
        UnityAds.load(adUnitId, loadListener)
    }
}
