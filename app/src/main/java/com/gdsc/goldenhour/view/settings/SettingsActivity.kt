package com.gdsc.goldenhour.view.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gdsc.goldenhour.BuildConfig
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivitySettingsBinding
import com.gdsc.goldenhour.util.GoogleSignInClientObj
import com.gdsc.goldenhour.view.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(SettingsFragment())

        mGoogleSignInClient = GoogleSignInClientObj.getInstance(this)
        val googleSignInAccountTask = mGoogleSignInClient.silentSignIn()
        if (googleSignInAccountTask.isSuccessful) {
            val taskResult = googleSignInAccountTask.result

            Glide.with(this)
                .load(taskResult.photoUrl)
                .override(100)
                .into(binding.userProfile)

            binding.userName.text = taskResult.displayName
            binding.userEmail.text = taskResult.email
        } else {
            Log.e(GOOGLE_LOGIN_TAG, "GoogleSignInClient 객체를 다시 얻어야 합니다.")
        }

        binding.btnLogout.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            Log.d(GOOGLE_LOGIN_TAG, "logout")

            // Activity Task 비우면서 로그인 화면으로 이동하도록
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    companion object {
        private const val GOOGLE_LOGIN_TAG = "GOOGLE_LOGIN"
    }
}