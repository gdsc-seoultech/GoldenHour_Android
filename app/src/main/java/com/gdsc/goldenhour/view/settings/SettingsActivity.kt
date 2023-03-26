package com.gdsc.goldenhour.view.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivitySettingsBinding
import com.gdsc.goldenhour.util.GoogleSignInClientObj
import com.gdsc.goldenhour.view.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mGoogleSignInClient = GoogleSignInClientObj.getInstance(this)
        loadFragment(SettingsFragment())

        updateUserInfo()

        binding.btnLogout.setOnClickListener {
            logOut()
        }
    }

    private fun updateUserInfo() {
        val gsa = GoogleSignIn.getLastSignedInAccount(this)
        Glide.with(this)
            .load(gsa?.photoUrl)
            .override(100)
            .into(binding.userProfile)

        binding.userName.text = gsa?.displayName
        binding.userEmail.text = gsa?.email
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