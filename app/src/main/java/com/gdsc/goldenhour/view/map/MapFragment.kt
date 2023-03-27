package com.gdsc.goldenhour.view.map

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gdsc.goldenhour.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), View.OnClickListener, OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
/*
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapview) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
*/
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

        return binding.root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marker = LatLng(35.241615, 128.695587)
        mMap.addMarker(MarkerOptions().position(marker).title("Marker LAB"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }


    override fun onClick(v: View?) {

    }


}