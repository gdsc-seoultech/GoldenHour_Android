package com.gdsc.goldenhour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.databinding.ActivityLoginBinding
import com.gdsc.goldenhour.network.RetrofitClass
import com.gdsc.goldenhour.network.model.SignInResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var google_login_btn: Button
    private lateinit var google_logout_btn: Button
    private lateinit var google_signout_btn: Button
    private lateinit var google_login_id: TextView
    private lateinit var google_login_name: TextView
    private lateinit var google_login_email: TextView

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleLoginResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        google_login_btn = findViewById(R.id.google_login_button)
        google_login_btn.setOnClickListener(this)
        google_logout_btn = findViewById(R.id.google_logout_button)
        google_logout_btn.setOnClickListener(this)
        google_signout_btn = findViewById(R.id.google_signout_button)
        google_signout_btn.setOnClickListener(this)
        google_login_id = findViewById(R.id.google_login_id)
        google_login_name = findViewById(R.id.google_login_name)
        google_login_email = findViewById(R.id.google_login_email)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // 로그인 결과
        googleLoginResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val account = task.result

                // 서버 확인
                authenticateUser(account)

                // ui 변경
                updateUI(true, account)
            } else {
                Log.d(GOOGLE_LOGIN_TAG, "fail")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // 기존에 로그인 했던 계정을 확인한다.
        val gsa = GoogleSignIn.getLastSignedInAccount(this)
        if (gsa != null) {
            // 기존에 로그인 되어있는 경우
            Log.d(GOOGLE_LOGIN_TAG, "기존에 로그인이 되어있습니다.")

            // 서버 계정 확인
            authenticateUser(gsa)

            // ui 변경
            updateUI(true, gsa)
        } else {
            updateUI(false, null)
        }
    }

    private fun authenticateUser(gsa: GoogleSignInAccount?) {
        if (gsa != null) {
            Log.d(GOOGLE_LOGIN_TAG, gsa.idToken.toString())
            val call = RetrofitClass.networkService.createUser(gsa.idToken.toString())

            call.enqueue(object : Callback<SignInResponse> {
                override fun onResponse(
                    call: Call<SignInResponse>,
                    response: Response<SignInResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d(RETROFIT_TAG, "success 1")
                        Log.d(RETROFIT_TAG, response.body()!!.data)
                    } else {
                        Log.d(RETROFIT_TAG, response.message())
                    }
                }

                override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                    Log.d(RETROFIT_TAG, "fail 1")
                    Log.d(RETROFIT_TAG, t.message.toString())
                }
            })
        }
    }

    private fun updateUI(didLogin: Boolean, gsa: GoogleSignInAccount?) {
        if (didLogin) {
            google_login_btn.visibility = View.GONE
            google_login_id.text = gsa?.id
            google_login_name.text = gsa?.displayName
            google_login_email.text = gsa?.email

            google_logout_btn.visibility = View.VISIBLE
            google_login_email.visibility = View.VISIBLE
            google_login_id.visibility = View.VISIBLE
            google_login_name.visibility = View.VISIBLE
            google_signout_btn.visibility = View.VISIBLE
        } else {
            google_login_btn.visibility = View.VISIBLE

            google_logout_btn.visibility = View.GONE
            google_signout_btn.visibility = View.GONE
            google_login_id.visibility = View.GONE
            google_login_email.visibility = View.GONE
            google_login_name.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.google_login_button -> {
                signIn()
            }
            R.id.google_logout_button -> {
                logOut()
            }
            R.id.google_signout_button -> {
                signOut()
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        googleLoginResult.launch(signInIntent)
    }

    private fun logOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(false, null)
            Log.d(Companion.GOOGLE_LOGIN_TAG, "logout")
        }
    }

    private fun signOut() {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this) {
            // 서버 회원 탈퇴
            updateUI(false, null)
            Log.d(Companion.GOOGLE_LOGIN_TAG, "signOut")
        }
    }

    companion object {
        private const val GOOGLE_LOGIN_TAG = "GOOGLE_LOGIN"
        private const val RETROFIT_TAG = "RETROFIT"
    }
}