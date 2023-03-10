package com.gdsc.goldenhour.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.BuildConfig
import com.gdsc.goldenhour.MainActivity
import com.gdsc.goldenhour.databinding.ActivityLoginBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.SignInResponse
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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