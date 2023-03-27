package com.gdsc.goldenhour.network.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class AedResponse(
    @Element
    var body: AedBody
)

@Xml(name = "header")
data class AedHeader(
    @PropertyElement(name="resultCode")
    var resultCode: String,

    @PropertyElement(name="resultMsg")
    var resultMsg: String
)

@Xml(name = "body")
data class AedBody(
    @Element(name = "items")
    val itemList: AedItemList? = null,
)

@Xml
data class AedItemList(
    @Element(name="item")
    val item: List<AedItem>? = null
)

@Xml
data class AedItem(
    @PropertyElement(name = "buildAddress")
    var buildAddress: String?,

    @PropertyElement(name = "buildPlace")
    var buildPlace: String?,

    @PropertyElement(name = "org")
    var org: String?,

    @PropertyElement(name = "clerkTel")
    var clerkTel: String?,

    @PropertyElement(name = "wgs84Lat")
    var wgs84Lat: Double,

    @PropertyElement(name = "wgs84Lon")
    var wgs84Lon: Double,
)