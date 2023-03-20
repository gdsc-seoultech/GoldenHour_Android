package com.gdsc.goldenhour.network.model

data class GoodsRequest(
    val name: String
)

data class Goods(
    var id: Int,
    var name: String
)

data class GoodsReadResponse(
    val success: Boolean,
    val data: List<Goods>,
    val error: Error
)

data class GoodsCreateResponse(
    val success: Boolean,
    val data: Goods,
    val error: Error
)

data class GoodsUpdateResponse(
    val success: Boolean,
    val data: NameWrapper,
    val error: Error
)

data class NameWrapper(
    val name: String
)

data class GoodsDeleteResponse(
    val success: Boolean,
    val data: String,
    val error: Error
)