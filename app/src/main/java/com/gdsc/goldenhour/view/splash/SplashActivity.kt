package com.gdsc.goldenhour.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.MainActivity
import com.gdsc.goldenhour.databinding.ActivitySplashBinding
import com.gdsc.goldenhour.util.GoogleSignInClientObj
import com.gdsc.goldenhour.view.permission.PermActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.splashIcon.playAnimation()

        Handler().postDelayed({
            // 로그인 된 유저가 있으면 메인 화면으로 진입
            val gsa = GoogleSignIn.getLastSignedInAccount(this)
            if (gsa != null) {
                navigateScreen(MainActivity::class.java)
            } else {
                // 그렇지 않으면 필수 권한 여부 확인 후 로그인 화면 진입
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