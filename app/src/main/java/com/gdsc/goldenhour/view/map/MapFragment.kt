package com.gdsc.goldenhour.view.map

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment(
    private var isDisasterMode: Boolean
) : BindingFragment<FragmentMapBinding>(FragmentMapBinding::inflate), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isDisasterMode){
            binding.btnAED.setBackgroundResource(R.drawable.disaster_btn_background)
            binding.btnFire.setBackgroundResource(R.drawable.disaster_btn_background)
            binding.btnHos.setBackgroundResource(R.drawable.disaster_btn_background)
            binding.btnPhar.setBackgroundResource(R.drawable.disaster_btn_background)
            binding.btnRest.setBackgroundResource(R.drawable.disaster_btn_background)
            binding.btnShelter.setBackgroundResource(R.drawable.disaster_btn_background)
            binding.btnTmp.setBackgroundResource(R.drawable.disaster_btn_background)
        }

        binding.btnHos.setOnClickListener {
            val intent = Intent(activity, MapHosActivity::class.java)
            startActivity(intent)
        }

        binding.btnPhar.setOnClickListener {
            val intent = Intent(activity, MapPharActivity::class.java)
            startActivity(intent)
        }

        binding.btnAED.setOnClickListener {
            val intent = Intent(activity, MapAedActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marker = LatLng(35.241615, 128.695587)
        mMap.addMarker(MarkerOptions().position(marker).title("Marker LAB"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }
}