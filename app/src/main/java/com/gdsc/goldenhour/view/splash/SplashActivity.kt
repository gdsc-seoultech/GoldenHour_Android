package com.gdsc.goldenhour.view.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gdsc.goldenhour.MainActivity
import com.gdsc.goldenhour.databinding.ActivitySplashBinding
import com.gdsc.goldenhour.util.GoogleSignInClientObj
import com.gdsc.goldenhour.view.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashIcon.playAnimation()

        Handler().postDelayed({
            val googleSignInClient = GoogleSignInClientObj.getInstance(this)
            val googleSignInAccountTask = googleSignInClient.silentSignIn()
            if (googleSignInAccountTask.isSuccessful) {
                navigateScreen(MainActivity::class.java)
            } else {
                navigateScreen(LoginActivity::class.java)
            }
        }, 2500)
    }

    private fun navigateScreen(activityName: Class<*>) {
        val intent = Intent(this, activityName)
        startActivity(intent)
        finish()
    }
}