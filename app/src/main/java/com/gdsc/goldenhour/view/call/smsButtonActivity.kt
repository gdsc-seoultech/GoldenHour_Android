package com.gdsc.goldenhour.view.call

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivitySmsButtonBinding

class smsButtonActivity : AppCompatActivity() {
    lateinit var binding: ActivitySmsButtonBinding
    var message: String = ""


    val situationFragment = SituationFragment()

    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val situationFragment = SituationFragment()
        binding.btn112.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "112")
            bundle.putString("key", "위치 : " + intent.extras?.getString("addr"))
            situationFragment.arguments = bundle

            transaction.replace(R.id.container, situationFragment)
                .commit()

            binding.btn112.isEnabled = false
            binding.btn110.isEnabled = false
            binding.btn119.isEnabled = false

        }
        binding.btn119.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "119")
            bundle.putString("key", "위치 : " +intent.extras?.getString("addr"))
            situationFragment.arguments = bundle


            transaction.replace(R.id.container, situationFragment)
                .commit()

            binding.btn112.isEnabled = false
            binding.btn110.isEnabled = false
            binding.btn119.isEnabled = false
        }

        binding.btn110.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "110")

        }

    }

}