package com.gdsc.goldenhour.view.map.data

data class Hospital(
    var phone: String?,
    var address: String?,
    var code: Int?,
    var name: String?,
    var x: Double?,
    var y: Double?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
    )
}
