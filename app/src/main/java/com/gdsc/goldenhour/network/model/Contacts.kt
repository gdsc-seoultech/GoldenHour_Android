package com.gdsc.goldenhour.network.model

data class ContactRequest(
    val name: String,
    val phoneNumber: String
)

data class ContactCreateResponse(
    val success: Boolean,
    val data: Contact,
    val error: Error
)

data class Contact(
    val id: Int,
    val name: String,
    val phoneNumber: String
)

data class ContactReadResponse(
    val success: Boolean,
    val data: List<Contact>,
    val error: Error
)