package com.gdsc.goldenhour.view.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.gdsc.goldenhour.BuildConfig
import com.gdsc.goldenhour.databinding.ActivityMapPharBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import noman.googleplaces.*
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class MapHosActivity() : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    OnRequestPermissionsResultCallback, PlacesListener {

    var mMap: GoogleMap? = null
    var currentMarker: Marker? = null
    var needRequest = false

    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    lateinit var mCurrentLocatiion: Location
    var currentPosition: LatLng =LatLng(37.5, 127.0)

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var location: Location
    lateinit var mLayout : View

    val GPS_ENABLE_REQUEST_CODE = 2001
    val PERMISSIONS_REQUEST_CODE = 100

    var previous_marker: MutableList<Marker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        val binding = ActivityMapPharBinding.inflate(layoutInflater)
        setContentView(binding.root)


        showPlaceInformation(currentPosition)


        mLayout = binding.layoutMain

        locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        (supportFragmentManager.findFragmentById(com.gdsc.goldenhour.R.id.map) as SupportMapFragment).getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setDefaultLocation()

        //????????? ????????? ??????
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                Snackbar.make(
                    mLayout, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("??????") {
                        ActivityCompat.requestPermissions(
                            this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        mMap!!.setOnCameraIdleListener(this)

    }

    override fun onPlacesFailure(e: PlacesException?) {
    }

    override fun onPlacesStart() {
    }

    override fun onPlacesSuccess(places: List<Place>?) {

        runOnUiThread {
            run {
                for (place: noman.googleplaces.Place in places!!) {
                    val latLng = LatLng(place.latitude, place.longitude)
                    val markerSnippet = getCurrentAddress(latLng)

                    val markerOptions = MarkerOptions()
                        .position(latLng)
                        .title(place.name)
                        .snippet(markerSnippet)
                    val item = mMap?.addMarker(markerOptions)
                    previous_marker.add(item!!)
                }

                val hashSet: HashSet<Marker> = HashSet(previous_marker)
                hashSet.addAll(previous_marker)
                previous_marker.clear()
                previous_marker.addAll(hashSet)
            }

        }
    }

    override fun onPlacesFinished() {
    }

    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                location = locationList[locationList.size - 1]

                currentPosition = LatLng(location.latitude, location.longitude)
                val markerTitle = getCurrentAddress(currentPosition)
                val markerSnippet =
                    "??????:" + location.latitude.toString() + " ??????:" + location.longitude.toString()


                setCurrentLocation(location, markerTitle, markerSnippet)
                mCurrentLocatiion = location
            }
        }
    }

    private fun startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
            if (checkPermission()) mMap!!.isMyLocationEnabled = true
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermission()) {

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            if (mMap != null) mMap!!.isMyLocationEnabled = true
        }
    }

    override fun onStop() {
        super.onStop()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        needRequest = true
                        return
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grandResults: IntArray) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults)

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {

            var check_result = true

            for (result: Int in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {

                startLocationUpdates()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    )
                ) {


                    Snackbar.make(
                        mLayout, "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "??????"
                    ) { finish() }.show()
                } else {

                    Snackbar.make(
                        mLayout, "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "??????"
                    ) { finish() }.show()
                }
            }
        }
    }

    fun getCurrentAddress(latlng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?

        try {
            addresses = geocoder.getFromLocation(
                latlng.latitude,
                latlng.longitude,
                1
            )
        } catch (ioException: IOException) {
            //???????????? ??????
            Toast.makeText(this, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show()
            return "???????????? ????????? ????????????"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "????????? GPS ??????", Toast.LENGTH_LONG).show()
            return "????????? GPS ??????"
        }

        if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(this, "?????? ?????????", Toast.LENGTH_LONG).show()
            return "?????? ?????????"
        } else {
            val address = addresses[0]
            return address.getAddressLine(0).toString()
        }
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    fun setCurrentLocation(location: Location?, markerTitle: String?, markerSnippet: String?) {
        if (currentMarker != null) currentMarker!!.remove()
        val currentLatLng = LatLng(location!!.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(currentLatLng)
        markerOptions.title(markerTitle)
        markerOptions.snippet(markerSnippet)
        markerOptions.draggable(true)
        currentMarker = mMap?.addMarker(markerOptions)!!
        val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        mMap!!.moveCamera(cameraUpdate)
    }

    fun setDefaultLocation() {
        val DEFAULT_LOCATION = LatLng(37.56, 126.97)
        val markerTitle = "???????????? ????????? ??? ??????"
        val markerSnippet = "?????? ???????????? GPS ?????? ?????? ???????????????"
        if (currentMarker != null) currentMarker!!.remove()
        val markerOptions = MarkerOptions()
        markerOptions.position(DEFAULT_LOCATION)
        markerOptions.title(markerTitle)
        markerOptions.snippet(markerSnippet)
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        currentMarker = mMap?.addMarker(markerOptions)!!
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15f)
        mMap!!.moveCamera(cameraUpdate)
    }

    private fun checkPermission(): Boolean {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this@MapHosActivity)
        builder.setTitle("?????? ????????? ????????????")
        builder.setMessage(
            "?????? ???????????? ???????????? ?????? ???????????? ???????????????."
        )
        builder.setCancelable(true)
        builder.setPositiveButton("??????") { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(
                callGPSSettingIntent,
                GPS_ENABLE_REQUEST_CODE
            )
        }
        builder.setNegativeButton(
            "??????"
        ) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    fun showPlaceInformation(location: LatLng) {
        previous_marker.clear()
        val apiKey = BuildConfig.GOOGLE_MAP_KEY
        NRPlaces.Builder()
            .listener(this@MapHosActivity)
            .key(apiKey)
            .latlng(location.latitude,  location.longitude)
            .radius(500)
            .type(PlaceType.HOSPITAL)
            .build()
            .execute()
    }

    override fun onCameraIdle() {
        addMarkersToMap()
    }

    fun addMarkersToMap() {
        val cameraPosition = mMap?.cameraPosition
        val current = LatLng(cameraPosition!!.target.latitude, cameraPosition.target.longitude)

        showPlaceInformation(current)
    }


}