package com.gdsc.goldenhour.network.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import org.simpleframework.xml.Element


@Xml(name = "response")
data class AedResponse(
    @Element(name = "header")
    val header: AedHeader,

    @Element(name = "body")
    val body: AedBody
) {
    constructor(): this(AedHeader(), AedBody())
}

@Xml(name = "header")
data class AedHeader(
    @PropertyElement(name="resultCode")
    val resultCode: Int,

    @PropertyElement(name="resultMsg")
    val resultMsg: String
) {
    constructor() : this(0, "")
}

@Xml(name = "body")
data class AedBody(
    @field:Element(name = "items")
    val itemList: AedItemList?,

    @field:PropertyElement(name = "numOfRows")
    var numOfRows: Int,

    @field:PropertyElement(name = "pageNo")
    var pageNo: Int,

    @field:PropertyElement(name = "totalCount")
    var totalCount: Int
) {
    constructor(): this(null, 0, 0, 0)
}


@Xml(name = "items")
data class AedItemList(
    @Element(name="item")
    val item: List<AedItem>?
) {
    constructor() : this(null)
}

@Xml(name = "item")
data class AedItem(
    @PropertyElement(name = "buildAddress")
    var buildAddress: String?,

    @PropertyElement(name = "buildPlace")
    var buildPlace: String?,

    @PropertyElement(name = "clerkTel")
    var clerkTel: String?,

    @PropertyElement(name = "wgs84Lat")
    var wgs84Lat: Double,

    @PropertyElement(name = "wgs84Lon")
    var wgs84Lon: Double,
){
    constructor(): this("", "", "", 0.0, 0.0)
}