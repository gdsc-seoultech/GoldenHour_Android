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
) {
}

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
){

}



/*
@Root(name = "response", strict = false)
data class AedResponse(
    @Element(name = "header")
    val header: AedHeader,

    @Element(name = "body")
    val body: AedBody
)

@Root(name = "header", strict=false)
data class AedHeader(
    @Element(name="resultCode")
    val resultCode: Int,

    @Element(name="resultMsg")
    val resultMsg: String
)

@Root(name = "body", strict = false)
data class AedBody(
    @ElementList(entry = "items")
    val itemList: List<AedItem>?,

    @Element(name = "numOfRows")
    var numOfRows: Int,

    @Element(name = "pageNo")
    var pageNo: Int,

    @Element(name = "totalCount")
    var totalCount: Int
)

/*
@Root(name = "items", strict=false)
data class AedItemList(
    @Element(name="item")
    val item: List<AedItem>?
)

 */

@Root(name = "item", strict = false)
data class AedItem(
    @Element(name = "buildAddress")
    var buildAddress: String?,

    @Element(name = "buildPlace")
    var buildPlace: String?,

    @Element(name = "clerkTel")
    var clerkTel: String?,

    @Element(name = "wgs84Lat")
    var wgs84Lat: Double,

    @Element(name = "wgs84Lon")
    var wgs84Lon: Double,
)
*/