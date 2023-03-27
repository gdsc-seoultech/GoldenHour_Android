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
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gdsc.goldenhour.databinding.ActivityMapAedBinding
import com.gdsc.goldenhour.network.model.AedResponse
import com.gdsc.goldenhour.network.model.DataRetrofit
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class MapAedActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMarkerClickListener {

    val binding by lazy { ActivityMapAedBinding.inflate(layoutInflater) }

    var mMap: GoogleMap? = null
    lateinit var mLayout: View
    var currentMarker: Marker? = null

    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val UPDATE_INTERVAL_MS = 1000
    private val FASTEST_UPDATE_INTERVAL_MS = 500
    private val PERMISSIONS_REQUEST_CODE = 100
    var needRequest = false

    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    lateinit var mCurrentLocatiion: Location
    var currentPosition: LatLng =LatLng(37.5, 127.0)

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    lateinit var location: Location

    val API_KEY = "lNSHWWuKKYF7knMXov2iRzSxkBRVM4KKXkdD93IyF%2Bx7Mq42e5SfU%2FsPJo3BfbT9LVXqPkndJi4xpQGgS%2B8Hsw%3D%3D"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
/*
        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        (supportFragmentManager.findFragmentById(com.gdsc.goldenhour.R.id.map) as SupportMapFragment).getMapAsync(this)

        if(checkPermissionForLocation(this)) {
            Log.d("tag", "check- true")
            startLocationUpdates()
        } else {
            loadAedList(127.0, 37.5, 1, 7, API_KEY)
        }


 */
        mLayout = binding.layoutMain

        locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder()

        builder.addLocationRequest(locationRequest!!)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        (supportFragmentManager.findFragmentById(com.gdsc.goldenhour.R.id.map) as SupportMapFragment).getMapAsync(this)

    }

    fun loadAedList(longitude: Double, latitude: Double, pageNo: Int, numOfRows: Int, serviceKey: String) {
        Log.d("tag", "IN")
        DataRetrofit.dataService.getAedList(longitude, latitude, pageNo, numOfRows, serviceKey)
            .enqueue(object : Callback<AedResponse> {
                override fun onResponse(call: Call<AedResponse>, response: Response<AedResponse>) {
                    val response = response.body()
                    Log.d("tag", response?.body?.itemList?.item?.size.toString())
                    for(i in 0 until (response?.body?.itemList?.item?.size ?: 0)) {
                        var pos = response?.body?.itemList?.item?.get(i)
                        var markerOptions = MarkerOptions().position(LatLng(pos?.wgs84Lat!!, pos?.wgs84Lon!!))
                            .title(pos?.org)
                            .snippet(pos?.buildAddress)
                        mMap?.addMarker(markerOptions)
                    }

                }

                override fun onFailure(call: Call<AedResponse>, t: Throwable) {
                    Log.d("Retrofit", "Fail : " + t.message.toString())
                    call.cancel()
                }
            })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("tag", "onMapReady")
        mMap = googleMap

        setDefaultLocation()

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
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
                    mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("확인") {
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

    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d("tag", "locationCallback")
            super.onLocationResult(locationResult)
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                location = locationList[locationList.size - 1]

                currentPosition = LatLng(location.latitude, location.longitude)
                val markerTitle = getCurrentAddress(currentPosition)
                val markerSnippet =
                    "위도:" + location.latitude.toString() + " 경도:" + location.longitude.toString()


                setCurrentLocation(location, markerTitle, markerSnippet)
                mCurrentLocatiion = location

                loadAedList(mCurrentLocatiion.longitude, mCurrentLocatiion.latitude, 1, 20, API_KEY)
                Log.d("tag", "callback: " + mCurrentLocatiion)
            }


        }
    }

    private fun startLocationUpdates() {
        Log.d("tag", "startLocaionUpdate")
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

            mFusedLocationClient!!.requestLocationUpdates(
                locationRequest!!,
                locationCallback,
                Looper.myLooper()
            )
            if (checkPermission()) mMap!!.isMyLocationEnabled = true
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermission()) {

            mFusedLocationClient?.requestLocationUpdates(locationRequest!!, locationCallback, null)
            if (mMap != null) mMap!!.isMyLocationEnabled = true
        }
    }

    override fun onStop() {
        super.onStop()
        mFusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    fun getCurrentAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?

        try {
            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
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
        Log.d("tag", "setDefault")
        val DEFAULT_LOCATION = LatLng(37.56, 126.97)
        val markerTitle = "위치정보 가져올 수 없음"
        val markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인하세요"
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
        Log.d("tag", "checkPermission")
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
                        mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "확인"
                    ) { finish() }.show()
                } else {

                    Snackbar.make(
                        mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "확인"
                    ) { finish() }.show()
                }
            }
        }
    }

    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this@MapAedActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            "앱을 사용하기 위해서는 위치 서비스가 필요합니다."
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(
                callGPSSettingIntent,
                GPS_ENABLE_REQUEST_CODE
            )
        }
        builder.setNegativeButton(
            "취소"
        ) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        needRequest = true
                        return
                    }
                }
        }
    }


    override fun onCameraIdle() {
        addMarkersToMap()
    }

    fun addMarkersToMap() {
        val cameraPosition = mMap?.cameraPosition
        val current = LatLng(cameraPosition!!.target.latitude, cameraPosition.target.longitude)

        loadAedList(current.longitude, current.latitude, 1, 20, API_KEY)
    }

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return true
    }
}