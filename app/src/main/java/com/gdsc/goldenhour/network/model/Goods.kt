package com.gdsc.goldenhour.network.model

// todo: 조회 -> 요청: x         응답: id, name
// todo: 추가 -> 요청: name      응답: id, name
// todo: 수정 -> 요청: id, name  응답: name
// todo: 삭제 -> 요청: id        응답: message
data class GoodsRequest(
    val name: String
)

data class Goods(
    val id: Int,
    val name: String
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