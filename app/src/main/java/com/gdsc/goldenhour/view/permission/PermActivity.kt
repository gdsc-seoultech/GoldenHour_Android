package com.gdsc.goldenhour.view.permission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivityPermBinding

class PermActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}