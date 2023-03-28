package com.gdsc.goldenhour.view.call

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivityEmergencySmsBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.SituationList
import com.gdsc.goldenhour.network.model.TypeSituationList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmergencySmsActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmergencySmsBinding
    var message: String = ""

    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencySmsBinding.inflate(layoutInflater)
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
            bundle.putString("key", "위치 : " + intent.extras?.getString("addr"))
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
            bundle.putString("key", "위치 : " + intent.extras?.getString("addr"))
            val btn110Fragment = btn110Fragment()
            btn110Fragment.arguments = bundle
            transaction.replace(R.id.container, btn110Fragment)
                .commit()

            binding.btn112.isEnabled = false
            binding.btn110.isEnabled = false
            binding.btn119.isEnabled = false

        }

    }
}