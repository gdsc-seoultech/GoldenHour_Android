package com.gdsc.goldenhour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gdsc.goldenhour.databinding.ActivityMainBinding
import com.gdsc.goldenhour.view.call.CallFragment
import com.gdsc.goldenhour.view.checklist.normal.ChecklistFragment
import com.gdsc.goldenhour.view.map.MapFragment
import com.gdsc.goldenhour.view.guide.GuideFragment
import com.gdsc.goldenhour.view.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(GuideFragment())
        changeFragment()
        navigateSettingsScreen()
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    private fun changeFragment(){
        binding.bottomNavBar.setOnItemSelectedListener {
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

    private fun navigateSettingsScreen() {
        binding.toolBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}