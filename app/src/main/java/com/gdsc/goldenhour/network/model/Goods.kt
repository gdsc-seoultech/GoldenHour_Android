package com.gdsc.goldenhour.network.model

// todo: CREATE, UPDATE 요청을 보낼 때 필요한 래퍼 클래스
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

// todo: 항목을 새로 등록할 때는 id와 name을 모두 리턴받는다.
data class GoodsCreateResponse(
    val success: Boolean,
    val data: Goods,
    val error: Error
)

// todo: 항목을 수정할 때는, 응답값으로 name만 받는다.
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