package com.gdsc.goldenhour.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gdsc.goldenhour.BuildConfig
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivitySettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(SettingsFragment())

        val gsa = GoogleSignIn.getLastSignedInAccount(this)
        if(gsa != null){
            // 로그인 된 경우
            Glide.with(this)
                .load(gsa.photoUrl)
                .override(100)
                .into(binding.userProfile)
            binding.userName.text = gsa.displayName
            binding.userEmail.text = gsa.email
            binding.btnLogout.setOnClickListener {
                logOut()
            }
        }else{
            // 로그아웃 한 경우
            binding.userInfoContainer.visibility = View.GONE
            binding.btnLogout.visibility = View.GONE
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun logOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_API_KEY)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener(this) {
            Log.d("GOOGLE_LOGIN", "logout")
            binding.userInfoContainer.visibility = View.GONE
            binding.btnLogout.visibility = View.GONE
        }
    }
}