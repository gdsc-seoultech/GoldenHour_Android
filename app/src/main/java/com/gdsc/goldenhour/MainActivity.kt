package com.gdsc.goldenhour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gdsc.goldenhour.databinding.ActivityMainBinding
import com.gdsc.goldenhour.ui.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(GuideFragment())
        changeFragment()

        // todo: 설정 버튼을 눌렀을 때 SettingsFragment로 이동
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
}