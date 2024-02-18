package com.vaibhavrawat.everythingqr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.vaibhavrawat.everythingqr.databinding.ActivityQrdashboardBinding

class QRDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityQrdashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_item1 -> {
                    loadFragment(CameraScanFragment()) // Load CameraScanFragment when item 1 is selected
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_item2 -> {
                    loadFragment(QRCodeGenerateFragment()) // Load QRCodeGenerateFragment when item 2 is selected
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        // Set the default selected item to load CameraScanFragment
        binding.bottomNavigationView.selectedItemId = R.id.action_item1
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}
