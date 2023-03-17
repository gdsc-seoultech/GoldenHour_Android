package com.gdsc.goldenhour.network.model

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