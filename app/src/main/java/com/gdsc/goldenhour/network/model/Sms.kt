
package com.gdsc.goldenhour.network.model

data class Situation(
    val id: Int,
    val name: String
)

data class SituationList(
    val success: Boolean,
    val data: List<Situation>,
    val error: Error
)

data class TypeSituation(
    val id: Int,
    val name: String,
)

data class TypeSituationList(
    val success: Boolean,
    val data: List<TypeSituation>,
    val error: Error
)

data class DetailSituation(
    val id: Int,
    val name: String
)

data class DetailSituationList(
    val success: Boolean,
    val data: List<DetailSituation>,
    val error: Error
)

data class Information(
    val id: Int,
    val question: String,
    val answerList: List<String>
)

data class InformationList(
    val success: Boolean,
    val data: List<Information>,
    val error: Error
)

