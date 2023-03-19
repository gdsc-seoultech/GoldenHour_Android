package com.gdsc.goldenhour.view.guide

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivityAiAssistBinding

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

        val builder = AlertDialog.Builder(this)
        builder.setTitle("안내 가이드")
            .setMessage(guideMessage)
            .setPositiveButton("확인") { _, _ ->
                // 다이얼로그에서 확인 버튼을 누르면 카메라 띄우기
                startCamera()
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 10
    }
}