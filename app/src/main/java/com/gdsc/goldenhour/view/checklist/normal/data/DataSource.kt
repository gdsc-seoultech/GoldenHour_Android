package com.gdsc.goldenhour.view.checklist.normal.data

class DataSource {
    fun loadEmergencyContacts(): ArrayList<HashMap<String, String>> {
        val data: ArrayList<HashMap<String, String>> = ArrayList()
        var map: HashMap<String, String> = HashMap()
        map["name"] = "이하은"
        map["phoneNumber"] = "010-1234-5678"
        data.add(map)

        map = HashMap()
        map["name"] = "백송희"
        map["phoneNumber"] = "010-1234-5678"
        data.add(map)

        map = HashMap()
        map["name"] = "이유석"
        map["phoneNumber"] = "010-1234-5678"
        data.add(map)

        map = HashMap()
        map["name"] = "황재영"
        map["phoneNumber"] = "010-1234-5678"
        data.add(map)

        return data
    }
}