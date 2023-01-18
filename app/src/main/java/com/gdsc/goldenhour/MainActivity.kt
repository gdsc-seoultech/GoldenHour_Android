package com.gdsc.goldenhour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gdsc.goldenhour.databinding.ActivityMainBinding
import com.gdsc.goldenhour.ui.CallFragment
import com.gdsc.goldenhour.ui.ChecklistFragment
import com.gdsc.goldenhour.ui.GuideFragment
import com.gdsc.goldenhour.ui.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(GuideFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.guide -> {
                    loadFragment(GuideFragment())
                    true
                }
                R.id.checklist -> {
                    loadFragment(ChecklistFragment())
                    true
                }
                R.id.call -> {
                    loadFragment(CallFragment())
                    true
                }
                R.id.map -> {
                    loadFragment(MapFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}