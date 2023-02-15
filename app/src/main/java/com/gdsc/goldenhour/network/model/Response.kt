package com.gdsc.goldenhour.network.model

data class SignInResponse(
    val success: Boolean,
    val error: Error,
    val data: String
)

data class Guide(
    var id: Int,
    var name: String,
    var imgUrl: String
)

data class GuideList(
    var data: List<Guide>
)

data class GuideImageList(
    var data: List<String>
)
