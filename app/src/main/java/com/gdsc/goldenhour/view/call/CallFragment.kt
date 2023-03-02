package com.gdsc.goldenhour.view.call

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.FragmentCallBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.util.*

class CallFragment : Fragment() {

    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mLastLocation: Location
    lateinit var mLocationRequest: LocationRequest
    val REQUEST_PERMISSION_LOCATION = 10
    lateinit var binding: FragmentCallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCallBinding.inflate(layoutInflater)
        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        return inflater.inflate(R.layout.fragment_call, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(checkPermissionForLocation(requireContext())) {
            startLocationUpdates()
        }
    }

    fun startLocationUpdates() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation!!)
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        var currentLatLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)

        binding.callAddress.text = "   : " + getCurrentAddress(currentLatLng)
        Log.d("tag", getCurrentAddress(currentLatLng))

    }



    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    fun getCurrentAddress(latlng: LatLng): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?

        addresses = geocoder.getFromLocation(
            latlng.latitude,
            latlng.longitude,
            1
        )

        if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(requireContext(), "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        } else {
            val address = addresses[0]
            return address.getAddressLine(0).toString()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("tag", "onCRequestPermissionResults")
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Toast.makeText(requireContext(), "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
                binding.callAddress.text = "주소를 확인할 수 없습니다."
            }
        }
    }

}