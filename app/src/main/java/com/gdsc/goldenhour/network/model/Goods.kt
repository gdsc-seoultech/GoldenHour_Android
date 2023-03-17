package com.gdsc.goldenhour.network.model

data class GoodsRequest(
    val name: String
)

data class GoodsGetResponse(
    val success: Boolean,
    val data: List<Goods>,
    val error: Error
)

data class Goods(
    val id: Int,
    val name: String
)

data class GoodsPostResponse(
    val success: Boolean,
    val data: DataObject,
    val error: Error
)

data class GoodsPutResponse(
    val success: Boolean,
    val data: DataObject,
    val error: Error
)

data class DataObject(
    val name: String
)

data class GoodsDeleteResponse(
    val success: Boolean,
    val data: String,
    val error: Error
)