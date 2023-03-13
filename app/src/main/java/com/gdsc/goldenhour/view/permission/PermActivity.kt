package com.gdsc.goldenhour.view.permission

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.gdsc.goldenhour.MainActivity
import com.gdsc.goldenhour.databinding.ActivityPermBinding
import com.gdsc.goldenhour.util.PermissionSupport
import com.gdsc.goldenhour.view.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    // 시스템 권한창에서 다시 PermActivity로 돌아오는 경우
    override fun onResume() {
        super.onResume()
        navigateLoginScreen()
    }

    private fun navigateLoginScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkCurrentPermissionStatus() && Settings.canDrawOverlays(this)){
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.canDrawOverlays(this)
        ) {
            // 시스템 권한 창에서 유저가 직접 권한을 허용할 수 있도록
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }

    private fun checkInitialPermissionStatus() {
        // 최초 실행 시 퍼미션 여부 확인 (거부된 권한은 재요청)
        val deniedPermissions = permissionSupport.checkPermission()
        if (deniedPermissions.isNotEmpty()) {
            permissionSupport.requestPermission(deniedPermissions.toTypedArray())
        }
    }

    private fun checkCurrentPermissionStatus(): Boolean {
        if (permissionSupport.checkPermission().isEmpty()) {
            return true
        }
        return false
    }

    // 거부된 권한 재요청에 대한 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionSupport.checkPermissionResult(requestCode, permissions, grantResults)
    }
}