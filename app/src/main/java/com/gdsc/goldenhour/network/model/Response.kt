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
    val imgUrl: String
)

data class GuideWebtoonList(
    val data: List<GuideWebtoon>
)

data class GuideWebtoon(
    val id: Int,
    val imgUrl: String
)

data class DisasterWebtoonList(
    val data: List<DisasterWebtoon>
)

data class DisasterWebtoon(
    val name: String,
    val imgUrl: String
)
