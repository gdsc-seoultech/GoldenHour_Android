package com.gdsc.goldenhour.view.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.FileObserver.ACCESS
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.view.map.data.Hospital
import com.gdsc.goldenhour.view.map.data.hospitalItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.VisibleRegion
import com.google.maps.android.clustering.ClusterManager
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class MapHosActivity : AppCompatActivity(), OnMapReadyCallback {

    var mgoogleMap: GoogleMap? = null
    lateinit var hospital_list: ArrayList<Hospital>
    lateinit var hospital_address: ArrayList<Location>
    var context = this
    lateinit var clusterManager: ClusterManager<hospitalItem>
    lateinit var locationPermission: ActivityResultLauncher<Array<String>>

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_hos)
        hospital_list = xml_parse()
        hospital_address = ArrayList<Location>()

        locationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if(result.all { it.value}) {
                startProcess()
            } else {
                Toast.makeText(this, "권한 승인이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        }

        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mgoogleMap = googleMap
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()

        clusterManager = ClusterManager(this, mgoogleMap)

        mgoogleMap!!.setOnCameraIdleListener(clusterManager)
        mgoogleMap!!.setOnMarkerClickListener(clusterManager)

        mgoogleMap!!.setOnCameraMoveListener {
            var vr: VisibleRegion = mgoogleMap!!.projection.visibleRegion
            var left = vr.latLngBounds.southwest.longitude
            var top = vr.latLngBounds.northeast.latitude
            var right = vr.latLngBounds.northeast.longitude
            var bottom = vr.latLngBounds.southwest.latitude

            if(clusterManager != null) clusterManager.clearItems()
            findMarker(left, top, right, bottom)
        }

        mgoogleMap!!.setOnInfoWindowClickListener {
            // 병원 정보 띄우기
        }

        clusterManager.setOnClusterClickListener {
            val latLng = LatLng(it.position.latitude, it.position.longitude)
            val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            mgoogleMap!!.moveCamera(cameraUpdate)
            return@setOnClusterClickListener false
        }
    }

    fun startProcess() {
        for(i in 0 until 50) {
            Log.d("tag", i.toString()+" ::" + hospital_list[i].address.toString())

            hospital_address.add(addrToPoint(this, hospital_list[i].address))
        }

        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)!!.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.let {
                    for((i, location) in it.locations.withIndex()) {
                        Log.d("Location", "$i ${location.latitude}, ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun setLastLocation(lastLocation: Location) {
        val latlng = LatLng(lastLocation.latitude, lastLocation.longitude)
        val markerOptions = MarkerOptions()
            .position(latlng)

        val cameraPosition = CameraPosition.Builder()
            .target(latlng)
            .zoom(20.0f)
            .build()

        mgoogleMap?.clear()
        mgoogleMap?.addMarker(markerOptions)
        mgoogleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun xml_parse(): ArrayList<Hospital> {
        val hospital_List = ArrayList<Hospital>()
        val inputStream = resources.openRawResource(R.raw.hospital)
        val inputStreamReader = InputStreamReader(inputStream)
        val reader = BufferedReader(inputStreamReader)

        var factory: XmlPullParserFactory? = null
        var parser: XmlPullParser? = null


        factory = XmlPullParserFactory.newInstance()
        parser = factory.newPullParser()
        parser.setInput(reader)

        var event = parser.eventType
        var hospital: Hospital? = null

        while(event != XmlPullParser.END_DOCUMENT) {
            val tag_name = parser.name
            when(event) {
                XmlPullParser.START_TAG -> {
                    var startTag = parser.name

                    when(tag_name) {
                        "text" -> {
                            hospital = Hospital()
                        }
                        "phone" -> {
                            hospital?.phone = parser.nextText()
                        }
                        "address" -> {
                            hospital?.address = parser.nextText()
                        }

                        "code" -> {
                            hospital?.code = parser.nextText().toInt()
                        }
                        "name" -> {
                            hospital?.name = parser.nextText()
                        }

                    }

                }
                XmlPullParser.END_TAG -> {
                    if(parser.name == "text") {
                        if (hospital != null) {
                            hospital_List.add(hospital)
                        }
                    }
                }

            }
            event = parser.next()

        }
        return hospital_List
    }

    private fun addrToPoint(context: Context, addr: String?): Location {
        val location = Location("")
        val geocoder = Geocoder(context)
        var addresses: List<Address>? = null

        try {
            addresses = geocoder.getFromLocationName(addr!!, 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if(addresses != null) {
            for(i in addresses.indices) {
                val lating: Address = addresses[i]
                location.latitude = lating.latitude
                location.longitude = lating.longitude
            }
        }
        return location
    }

    private fun findMarker(left: Double, top: Double, right: Double, bottom: Double) {
        for(i in 0 until 50) {
            if(hospital_address[i].longitude >= left && hospital_address[i].longitude<=right) {
                if(hospital_address[i].latitude >= bottom && hospital_address[i].latitude <= top) {
                    var location = hospital_address[i]
                    val hospitalItems: hospitalItem = hospitalItem(location.latitude, location.longitude,
                        hospital_list[i].name!!)
                    clusterManager.addItem(hospitalItems)
                }
            }
        }
    }


}