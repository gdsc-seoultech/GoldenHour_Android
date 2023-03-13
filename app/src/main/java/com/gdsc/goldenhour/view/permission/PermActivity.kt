package com.gdsc.goldenhour.view.permission

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.databinding.ActivityPermBinding
import com.gdsc.goldenhour.util.PermissionSupport
import com.gdsc.goldenhour.view.login.LoginActivity

class PermActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermBinding
    private lateinit var permissionSupport: PermissionSupport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionSupport = PermissionSupport(this)

        binding.btnPerm.setOnClickListener {
            checkInitialPermissionStatus()
            requestOverlayPermission()
        }
    }

    // 화면이 처음 or 다시 뜰 때 필수 권한이 허용되어 있어야만 로그인 화면 진입
    override fun onResume() {
        super.onResume()
        if (checkOverlayPermission()) {
            navigateLoginScreen()
        }
    }

    private fun checkInitialPermissionStatus() {
        // 최초 실행 시 퍼미션 여부 확인 (거부된 권한에 대해 요청)
        if (!permissionSupport.checkPermission()) {
            permissionSupport.requestPermission()
        }
    }

    // 권한 요청에 대한 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionSupport.checkPermissionResult(requestCode, grantResults)
    }

    private fun checkOverlayPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && Settings.canDrawOverlays(this)
        ) {
            return true
        }
        return false
    }

    private fun requestOverlayPermission() {
        // 시스템 권한 창에서 유저가 직접 권한을 허용할 수 있도록
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }

    private fun navigateLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}