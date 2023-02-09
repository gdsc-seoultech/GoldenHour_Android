package com.gdsc.goldenhour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gdsc.goldenhour.databinding.ActivityGuideItemBinding

class GuideItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        binding.guideTitle.text = title
    }
}