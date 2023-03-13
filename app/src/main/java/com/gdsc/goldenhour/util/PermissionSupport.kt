package com.gdsc.goldenhour.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionSupport(
    private val context: Context
) {
    private val permissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.CALL_PHONE,
        android.Manifest.permission.SEND_SMS,
        android.Manifest.permission.RECEIVE_SMS,
    )
    private val PERMISSION_REQUEST_CODE = 999
    private val deniedPermissions = mutableListOf<String>()

    fun checkPermission(): Boolean {
        for (perm in permissions) {
            val result = ContextCompat.checkSelfPermission(context, perm)
            if (result != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(perm)
            }
        }
        if(deniedPermissions.isEmpty()) return true
        return false
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            deniedPermissions.toTypedArray(),
            PERMISSION_REQUEST_CODE
        )
    }

    fun checkPermissionResult(
        requestCode: Int,
        grantResults: IntArray
    )  {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            for(result in grantResults){
                if(result == PackageManager.PERMISSION_DENIED){
                    Log.d("PERMISSION", "현재 거부된 권한은 해당 기능을 사용할 때 다시 요청해야 합니다.")
                    return
                }
            }
            Log.d("PERMISSION", "모든 권한이 허용되었습니다.")
        }
    }
}