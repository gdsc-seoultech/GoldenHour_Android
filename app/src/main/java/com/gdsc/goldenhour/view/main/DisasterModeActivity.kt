package com.gdsc.goldenhour.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivityDisasterModeBinding
import com.gdsc.goldenhour.view.call.CallFragment
import com.gdsc.goldenhour.view.checklist.disaster.DisasterChecklistFragment
import com.gdsc.goldenhour.view.guide.GuideFragment
import com.gdsc.goldenhour.view.map.MapFragment
import com.gdsc.goldenhour.view.settings.SettingsActivity

class DisasterModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisasterModeBinding
    private lateinit var disasterSMS: String
    private lateinit var disasterName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisasterModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectSecondBottomItem()
        showDisasterSMS(intent)

        changeFragment()
        navigateSettingsScreen()
    }

    // 두번째 체크리스트 탭이 선택되도록
    private fun selectSecondBottomItem() {
        val view = binding.bottomNavBar.findViewById<View>(R.id.checklist)
        view.performClick()
    }

    // 브로드캐스트 리시버로부터 문자 데이터 전달 받기
    private fun showDisasterSMS(intent: Intent?) {
        disasterSMS = intent?.getStringExtra("content").toString()
        disasterName = intent?.getStringExtra("name").toString()
        loadFragment(DisasterChecklistFragment(disasterSMS, disasterName))
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
                    loadFragment(DisasterChecklistFragment(disasterSMS, disasterName))
                    true
                }
                R.id.call -> {
                    loadFragment(CallFragment(true))
                    true
                }
                R.id.map -> {
                    loadFragment(MapFragment(true))
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