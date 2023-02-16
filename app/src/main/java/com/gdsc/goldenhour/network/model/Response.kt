package com.gdsc.goldenhour.network.model

data class SignInResponse(
    val success: Boolean,
    val data: String,
    val error: Error
)

data class GuideList(
    val success: Boolean,
    val data: List<Guide>,
    val error: Error
)

data class Guide(
    val id: Int,
    val name: String,
//  val imgUrl: String
)

data class GuideImageList(
    val data: List<GuideImage>
)

data class GuideImage(
    val id: Int,
    val imgUrl: String
)

