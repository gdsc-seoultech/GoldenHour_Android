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
    val data: List<WebtoonItem>
)

data class WebtoonItem(
    val id: Int,
    val imgUrl: String
)

data class DisasterList(
    val success: Boolean,
    val data: List<Disaster>,
    val error: Error
)

data class Disaster(
    val id: Int,
    val name: String
)

data class DisasterWebtoonList(
    val data: List<WebtoonItem>
)

data class ContactList(
    val success: Boolean,
    val data: List<Contact>,
    val error: Error
)

data class Contact(
    val id: Int,
    val name: String,
    val phoneNumber: String
)