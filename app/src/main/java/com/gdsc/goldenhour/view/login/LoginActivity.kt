package com.gdsc.goldenhour.view.login

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.BuildConfig
import com.gdsc.goldenhour.MainActivity
import com.gdsc.goldenhour.databinding.ActivityLoginBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.SignInResponse
import com.gdsc.goldenhour.util.PermissionSupport
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleLoginResult: ActivityResultLauncher<Intent>
    private lateinit var permissionSupport: PermissionSupport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
        showOverlayPermissionDialog()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_LOGIN_KEY)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signInButton.setOnClickListener {
            signIn()
        }

        // 로그인 결과
        googleLoginResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val account = task.result
                uploadUserInfo(account)

                // 로그인 액티비티는 완전히 종료되고, 메인 액티비티가 루트가 될 수 있도록
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Log.d(GOOGLE_LOGIN_TAG, "fail")
            }
        }
    }

    private fun checkPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionSupport = PermissionSupport(this)

            // 최초 실행 시 퍼미션 허용 여부 확인 (거부된 권한은 재요청)
            val deniedPermissions = permissionSupport.checkPermission()
            if(deniedPermissions.isNotEmpty()){
                permissionSupport.requestPermission(deniedPermissions.toTypedArray())
            }else{
                Log.d("PERMISSION", "All Permission Granted...")
            }
        }
    }

    private fun showOverlayPermissionDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.canDrawOverlays(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("권한 설정")
                .setMessage("재난문자가 왔을 때 바로 체크리스트가 뜰 수 있도록 [다른 앱 위에 표시] 권한을 허용해주세요.")
                .setPositiveButton("확인"){ _, _ ->
                    // 시스템 권한 창에서 유저가 직접 권한을 허용할 수 있도록
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                }
                .setNegativeButton("취소"){ _, _ ->
                    Toast.makeText(this, "앱의 일부 기능이 제한될 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            val dialog = builder.create()
            dialog.show()
        }
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

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        googleLoginResult.launch(signInIntent)
    }

    private fun uploadUserInfo(gsa: GoogleSignInAccount?) {
        if (gsa != null) {
            // 유저의 로그인 정보를 서버에 업로드 한다.
            RetrofitObject.networkService.createUser(gsa.idToken.toString())
                .enqueue(object : Callback<SignInResponse> {
                    override fun onResponse(
                        call: Call<SignInResponse>,
                        response: Response<SignInResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(RETROFIT_TAG, "success upload user info")
                            Log.d(RETROFIT_TAG, response.body()!!.data)
                        } else {
                            Log.d(RETROFIT_TAG, response.message())
                        }
                    }

                    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                        Log.d("Retrofit", t.message.toString())
                        call.cancel()
                    }
                })
        }
    }

    companion object {
        private const val GOOGLE_LOGIN_TAG = "GOOGLE_LOGIN"
        private const val RETROFIT_TAG = "RETROFIT"
    }
}