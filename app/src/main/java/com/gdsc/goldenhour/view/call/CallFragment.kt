package com.gdsc.goldenhour.view.call

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentCallBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.util.*

class CallFragment : BindingFragment<FragmentCallBinding>(FragmentCallBinding::inflate) {
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10
    private var addr: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())
        binding.tvUserName.text = "${gsa?.displayName}λμ μμΉ"

        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if(checkPermissionForLocation(requireContext())) {
            startLocationUpdates()
        }

        binding.btnSms.setOnClickListener {
            val intent = Intent(requireContext(), EmergencySmsActivity::class.java)
            startActivity(intent)
        }

        binding.btn112.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$112")))
        }

        binding.btn119.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$119")))
        }

        binding.btn110.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$110")))
        }
    }

    fun startLocationUpdates() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location?) {
        mLastLocation = location!!
        val currentLatLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?

        addresses = geocoder.getFromLocation(
            currentLatLng.latitude,
            currentLatLng.longitude,
            1
        )
        if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(requireContext(), "μ£Όμ λ―Έλ°κ²¬", Toast.LENGTH_LONG).show()
            binding.tvUserLocation.text = " :  μ£Όμλ₯Ό μ°Ύμ μ μμ΅λλ€."
        } else {
            val address = addresses[0]
            binding.tvUserLocation.text = " :  ${address.getAddressLine(0)}"
        }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Toast.makeText(requireContext(), "κΆνμ΄ μμ΄ ν΄λΉ κΈ°λ₯μ μ€νν  μ μμ΅λλ€.", Toast.LENGTH_SHORT).show()
                addr = "   : μ£Όμλ₯Ό νμΈν  μ μμ΅λλ€."
                binding.tvUserLocation.text = addr
            }
        }
    }
}