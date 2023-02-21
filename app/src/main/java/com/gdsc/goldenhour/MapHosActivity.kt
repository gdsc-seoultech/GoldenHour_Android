package com.gdsc.goldenhour

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.view.map.data.Hospital
import com.gdsc.goldenhour.view.map.data.hospitalItem
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class MapHosActivity : AppCompatActivity(), OnMapReadyCallback {

    var mgoogleMap: GoogleMap? = null
    lateinit var hospital_list: ArrayList<Hospital>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_hos)

        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment)!!.getMapAsync(this)

        hospital_list = xml_parse()
        val intent = Intent(this, MapHosActivity::class.java)
        intent.putExtra("hospital", hospital_list)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mgoogleMap = googleMap
        val context: Context = this
        val clusterManager: ClusterManager<hospitalItem> = ClusterManager(this, mgoogleMap)

        googleMap.setOnMapLoadedCallback {
            val latLng = LatLng(37.566, 126.978)
            mgoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mgoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(12f))
        }

        mgoogleMap!!.setOnCameraIdleListener(clusterManager)
        mgoogleMap!!.setOnMarkerClickListener(clusterManager)

        //for(i in 0 until (hospital_list?.size ?: 300)) {
        for(i in 0 until 100) {
            Log.d("tag", i.toString()+" ::" + hospital_list.get(i).address.toString())
            val location: Location = addrToPoint(context, hospital_list?.get(i)?.address)
            //var latLng = LatLng(location.latitude, location.longitude)
            val hospitalItems: hospitalItem = hospitalItem(location.latitude, location.longitude,
                hospital_list[i].name!!
            )
            clusterManager.addItem(hospitalItems)


            /*var markerOptions: MarkerOptions = MarkerOptions()
            markerOptions.position(latLng)
            googleMap!!.addMarker(markerOptions)*/
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
                    //Log.d("tag", "start tag : " + startTag)
                    when(tag_name) {
                        "text" -> {
                            hospital = Hospital()
                            //Log.d("tag", "추가")
                        }
                        "phone" -> {
                            hospital?.phone = parser.nextText()
                            //Log.d("tag", "1 "+hospital?.phone)
                        }
                        "address" -> {
                            hospital?.address = parser.nextText()
                            //Log.d("tag", "2 "+hospital?.address)
                        }

                        "code" -> {
                            hospital?.code = parser.nextText().toInt()
                            //Log.d("tag", "3 "+hospital?.code)
                        }
                        "name" -> {
                            hospital?.name = parser.nextText()
                            //Log.d("tag", "4 "+hospital?.name)
                        }
                        "x" -> {
                            hospital?.x = parser.nextText().toDouble()
                            //Log.d("tag", "5 "+hospital?.x)
                        }
                        "y" -> {
                            hospital?.y = parser.nextText().toDouble()
                            //Log.d("tag", "6 "+hospital?.y)
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
            addresses = geocoder.getFromLocationName(addr, 1)
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
}