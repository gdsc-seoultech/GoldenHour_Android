package com.gdsc.goldenhour.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.view.main.MainActivity
import com.gdsc.goldenhour.databinding.ActivityLoginBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.SignInResponse
import com.gdsc.goldenhour.util.GoogleSignInClientObj
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mGoogleSignInClient = GoogleSignInClientObj.getInstance(this)

        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            // 로그인에 성공하면 idToken 값을 my_prefs 에 저장한다.
            val idToken = account.idToken.toString()
            saveUserIdTokenInPrefs(idToken)

            // 서버에 로그인 정보를 업로드한다.
            uploadUserInfoToServer(idToken)

            // 로그인 액티비티는 완전히 종료되고, 메인 액티비티가 루트가 될 수 있도록
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(GOOGLE_LOGIN_TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun saveUserIdTokenInPrefs(idToken: String) {
        val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().run {
            putString("idToken", idToken)
            apply()
        }
    }

    private fun uploadUserInfoToServer(idToken: String) {
        RetrofitObject.networkService.createUser(idToken)
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

    companion object {
        private const val RC_SIGN_IN = 10
        private const val GOOGLE_LOGIN_TAG = "GOOGLE_LOGIN"
        private const val RETROFIT_TAG = "RETROFIT"
    }
}