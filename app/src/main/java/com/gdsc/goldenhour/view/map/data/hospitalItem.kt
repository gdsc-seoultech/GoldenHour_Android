package com.gdsc.goldenhour.view.map.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class hospitalItem : ClusterItem {
    lateinit var mPosition: LatLng
    lateinit var mTitle: String

    constructor(lat: Double, lng: Double, title: String) {
        mPosition = LatLng(lat, lng)
        mTitle = title
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String? {
        return mTitle
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getZIndex(): Float? {
        return null
    }
}