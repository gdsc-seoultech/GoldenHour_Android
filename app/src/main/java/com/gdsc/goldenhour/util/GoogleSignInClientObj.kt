package com.gdsc.goldenhour.util

import android.content.Context
import com.gdsc.goldenhour.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

object GoogleSignInClientObj {
    fun getInstance(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_LOGIN_KEY)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }
}