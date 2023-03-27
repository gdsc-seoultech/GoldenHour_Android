package com.gdsc.goldenhour.view.guide

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivityAiAssistBinding
import com.gdsc.goldenhour.view.guide.blood.BloodDetectorFragment
import com.gdsc.goldenhour.view.guide.pose.PoseDetectorFragment

class AiAssistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAiAssistBinding
    private lateinit var guideName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiAssistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        guideName = intent.getStringExtra("name").toString()

        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        val status = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (status == PackageManager.PERMISSION_GRANTED) {
            showGuideDialog()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showGuideDialog()
            } else {
                Log.d("PERMISSION", "CAMERA Permission Denied...")
            }
        }
    }

    private fun showGuideDialog() {
        val aiAssistNames = resources.getStringArray(R.array.ai_assist_name)
        val aiAssistGuides = resources.getStringArray(R.array.ai_assist_guide)

        val bloodItems = mutableListOf<String>()
        for (i in 0..3) {
            bloodItems.add(aiAssistNames[i])
        }
        val pressureItems = listOf(aiAssistNames[4], aiAssistNames[5])

        val guideMessage = when (guideName) {
            in bloodItems -> aiAssistGuides[0] // 지혈점
            in pressureItems -> aiAssistGuides[1] // 압박점
            else -> "동상, 골절 안내 가이드"
        }

        val aiModelNumber = when (guideName) {
            in bloodItems -> 1
            in pressureItems -> 2
            else -> 0
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("안내 가이드")
            .setMessage(guideMessage)
            .setPositiveButton("확인") { _, _ ->
                when(aiModelNumber){
                    1 -> loadFragment(BloodDetectorFragment())
                    2 -> loadFragment(PoseDetectorFragment())
                }
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


    companion object {
        private const val PERMISSION_REQUEST_CODE = 10
    }
}