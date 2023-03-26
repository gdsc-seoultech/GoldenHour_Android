package com.gdsc.goldenhour.view.guide.pose.model

data class TargetShape(
    val firstLandmarkType: Int,
    val middleLandmarkType: Int,
    val lastLandmarkType: Int,
    val angle: Double
)

data class TargetPose(
    val targets: List<TargetShape>
)
