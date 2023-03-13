package com.gdsc.goldenhour.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gdsc.goldenhour.MainActivity
import com.gdsc.goldenhour.databinding.ActivitySplashBinding
import com.gdsc.goldenhour.view.login.LoginActivity
import com.gdsc.goldenhour.view.permission.PermActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashIcon.playAnimation()

        Handler().postDelayed({
            val gsa = GoogleSignIn.getLastSignedInAccount(this)
            if (gsa != null) {
                navigateScreen(MainActivity::class.java)
            }else {
                navigateScreen(PermActivity::class.java)
            }
        }, 2500)
    }

    private fun navigateScreen(activityName: Class<*>) {
        val intent = Intent(this, activityName)
        startActivity(intent)
        finish()
    }
}