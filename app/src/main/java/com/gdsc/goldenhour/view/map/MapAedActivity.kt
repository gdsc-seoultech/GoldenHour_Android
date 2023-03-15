package com.gdsc.goldenhour.view.map

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.databinding.ActivityMapAedBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.view.map.data.AedItem
import com.gdsc.goldenhour.view.map.data.AedItemList
import com.gdsc.goldenhour.view.map.data.AedResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapAedActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMarkerClickListener {

    var apiRequestCount: Int? = null
    var mMap: GoogleMap? = null
    var startFlagForAedApi: Boolean? = null
    var markerList: ArrayList<Marker> = ArrayList()
    // var AedList: ArrayList<AedInfoItem> = ArrayList()

    lateinit var binding: ActivityMapAedBinding
    // var mBottomSheetBehavior: BottomSheetBehavior = null

    val API_KEY = "lNSHWWuKKYF7knMXov2iRzSxkBRVM4KKXkdD93IyF%2Bx7Mq42e5SfU%2FsPJo3BfbT9LVXqPkndJi4xpQGgS%2B8Hsw%3D%3D"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapAedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (supportFragmentManager.findFragmentById(com.gdsc.goldenhour.R.id.map) as SupportMapFragment).getMapAsync(this)

        loadAedList(127.5, 37.8, 1, 5, API_KEY)


    }



    fun loadAedList(longitude: Double, latitude: Double, pageNo: Int, numOfRows: Int, serviceKey: String) {
        Log.d("Retrofit", "IN")
        RetrofitObject.getApiService().getAedList(longitude, latitude, pageNo, numOfRows, serviceKey)
            .enqueue(object : Callback<AedItemList> {
                override fun onResponse(call: Call<AedItemList>, response: Response<AedItemList>) {
                    if (response.isSuccessful) {
                        /*
                        Log.d("Retrofit", "1" + response.body()?.body.toString())
                        Log.d("Retrofit", "2" + response.body()?.body?.itemList.toString())
                        Log.d("Retrofit", "3" + response.body()?.body?.itemList?.item.toString())
                        Log.d("Retrofit", "4" + response.body()?.body?.itemList?.item?.get(0)?.wgs84Lat.toString())


                         */
                        Log.d("Retrofit", "1" + response.body().toString())
                        Log.d("Retrofit", "2" + response.body()?.item.toString())
                        Log.d("Retrofit", "3" + response.body()?.item?.get(0)?.wgs84Lat.toString())
                    }
                }

                override fun onFailure(call: Call<AedItemList>, t: Throwable) {
                    Log.d("Retrofit", "Fail : " + t.message.toString())
                    call.cancel()
                }
            })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap!!.setOnCameraIdleListener(this)
        mMap!!.setOnMarkerClickListener(this)
        mMap!!.setOnCameraMoveStartedListener(this)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5, 126.9), 15f))
    }

    override fun onCameraIdle() {
        /*
        removeMarkerAll()

        val lat = mMap!!.cameraPosition.target.latitude.toString()
        val lon = mMap!!.cameraPosition.target.longitude.toString()

        startFlagForAedApi = true
        val aedApi: AedApi = AedApi()
        aedApi.execute(lat, lon, "")

        apiRequestCount = 0
        val temp_handler: Handler = Handler()
        temp_handler.postDelayed(Runnable {
            run {
                if(apiRequestCount!! < 100) {
                    if(startFlagForAedApi == true){
                        apiRequestCount = apiRequestCount!! + 1
                        temp_handler.postDelayed(this, 100)
                    } else {
                        drawMarker()
                    }
                } else {
                    Toast.makeText(this, "호출에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }, 100)

         */
    }
/*
    fun removeMarkerAll() {
        for(marker in markerList) {
            marker.remove()
        }
    }

    fun drawMarker() {
        for(i in 0 until AedList.size) {
            val item: AedItem = AedList[i]
            var remain_stat: String = item.remain_stat

            when(remain_stat) {
                "plenty" -> {
                    remain_stat = "100개 이상"
                }
                "some" -> {
                    remain_stat = "30개 이상 100개 미만"
                }
                "few" -> {
                    remain_stat = "2개 이상 30개 미만"
                }
                "empty" -> {
                    remain_stat = "1개 이하"
                }
            }

            val marker: Marker? = mMap?.addMarker(MarkerOptions()
                .position(LatLng(item.lat.toDouble(), item.lng.toDouble()))
                .title(item.name)
                .snippet(item.addr+"@"+item.created_at+"@"+item.remain_stat+"@"+item.stock_at+"@"+item.type))
            markerList.add(marker!!)
        }
    }

 */
    override fun onCameraMoveStarted(p0: Int) {
        //mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("Test", "click")
        return true
    }

}