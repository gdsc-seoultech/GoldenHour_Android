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

    fun checkPermission(): MutableList<String> {
        val deniedPermissions = mutableListOf<String>()

        for (perm in permissions) {
            val result = ContextCompat.checkSelfPermission(context, perm)
            if (result != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(perm)
            }
        }

        return deniedPermissions
    }

    fun requestPermission(deniedPermissions: Array<String>) {
        ActivityCompat.requestPermissions(
            context as Activity,
            deniedPermissions,
            PERMISSION_REQUEST_CODE
        )
    }

    fun checkPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )  {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            val deniedPermissions = mutableListOf<String>()
            for ((idx, result) in grantResults.withIndex()) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    deniedPermissions.add(permissions[idx])
                }
            }

            if (deniedPermissions.isNotEmpty()) {
                if(!checkDontAskAgainCase()){
                    // 거부된 권한 재요청
                    Toast.makeText(context, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    requestPermission(deniedPermissions.toTypedArray())
                }else {
                    // 권한을 거부하고 다시 묻지 않음까지 선택한 경우, 설정창에서 직접 허용하도록
                    showSettingsWindow()
                }
            } else {
                Log.d("PERMISSION", "All Permission Granted...")
            }
        }
    }

    private fun checkDontAskAgainCase(): Boolean {
        for(perm in permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, perm)){
                return false
            }
        }

        // 모든 퍼미션에 대해서 shouldShowRequestPermissionRationale 응답이 false인 경우
        return true
    }

    private fun showSettingsWindow() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("권한 설정")
            .setMessage("골든아워의 모든 기능을 사용하기 위해 권한 허용이 필요합니다. 확인을 눌러 설정창으로 이동한 뒤 설정을 완료해주세요.")
            .setPositiveButton("확인"){ _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            .setNegativeButton("취소"){ _, _ ->
                Toast.makeText(context, "앱의 일부 기능이 제한될 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        val dialog = builder.create()
        dialog.show()
    }
}